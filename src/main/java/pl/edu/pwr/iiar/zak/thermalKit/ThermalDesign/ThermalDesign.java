/*
 * Copyright (c) 2014 Wroclaw University of Technology
 *
 * This file is part of the pl.edu.pwr.iiar.zak.jGenerilo.JGenerilo
 * Thermal Emulation Tools.
 *
 * pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo Thermal Emulation
 * Tools are free software: you may redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * A copy of the GNU General Public License is available
 * at <http://www.gnu.org/licenses/>.
 *
 */

package pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign;

import edu.byu.ece.rapidSmith.design.*;
import edu.byu.ece.rapidSmith.device.Device;
import edu.byu.ece.rapidSmith.device.PrimitiveSite;
import edu.byu.ece.rapidSmith.device.PrimitiveType;
import edu.byu.ece.rapidSmith.device.Tile;
import edu.byu.ece.rapidSmith.util.FileConverter;
import org.jdom2.JDOMException;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloException;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloExceptionType;
import pl.edu.pwr.iiar.zak.thermalKit.parser.ThermalDesignParser;
import pl.edu.pwr.iiar.zak.thermalKit.placer.ThermalPlacer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The design class houses an entire XDL thermal design. It extends the Device
 * RapidSmith class for more information see edu.byu.ece.rapidSmith.design.Design.
 * It tracks of all of its members: heaterInstances, thermometers, other instances,
 * nets, modules and attributes and can load/import save/export XDL files.
 * When an XDL design is loaded into this class it also populates the Device
 * and WireEnumerator classes that correspond to the part this design targets.
 *
 * @author Pawel Weber
 * @version 0.1
 * @see edu.byu.ece.rapidSmith.design.Design
 * Created on: Mar 28, 2014
 */

public class ThermalDesign extends Design {

    /**
     * Type of design
     */
    private ThermalDesignType type;
    /**
     * This is a list of all the heaterInstances instances in the design
     */
    private ArrayList<HeaterUnit> heaterUnits;
    /**
     * This is a list of all the thermometer in the design
     */
    private ArrayList<ThermometerUnit> thermometerUnits;
    /**
     * This is a file name of input XDL file
     */
    private String xdlInFile;
    /**
     * This is a file name of output XDL file
     */
    private String xdlOutFile;
    /**
     * This is a frequency generator net for heaters
     */
    private String generatorNet;


    /**
     * Constructor which initializes all member data structures.
     * Sets class members and removes clock nets connected with
     * thermometer counters.
     */
    public ThermalDesign() {
        super();
        //Class members
        type = null;
        heaterUnits = new ArrayList<HeaterUnit>();
        thermometerUnits = new ArrayList<ThermometerUnit>();
    }

    /**
     * Override method which loads XDL. Additionally it also
     * remove redundant connections to thermometer counter.
     *
     * @param fileName The name of the XDL file to load.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadXDLFile(String fileName) {
        super.loadXDLFile(fileName);
        //Remove all thermometer nets from design
        //Delete clock pips
        try {
            Net clockNet = this.getNet("clk50Mhz");

            ArrayList<PIP> pipsBuffer = (ArrayList<PIP>) clockNet.getPIPs().clone();
            for (PIP pip : pipsBuffer) {
                clockNet.removePIP(pip);
            }
            //Delete clock pins connected to thermometer counters
            ArrayList<Pin> pinsBuffer = (ArrayList<Pin>) clockNet.getPins().clone();
            for (Pin pin : pinsBuffer) {
                if (!pin.isOutPin()) {
                    if (pin.getInstanceName().contains("termometr<"))
                        clockNet.removePin(pin);
                }
            }

        } catch (JGeneriloException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadNCDFile(String filename) {
        FileConverter.convertNCD2XDL(filename);
        loadXDLFile(filename.substring(0, filename.length() - 3) + "xdl");
    }

    /**
     * Method loads and parses the control XML file with the description of thermal floorplan
     * of device which would be emulated. It fills up the collection of heater units as well
     * as thermometer units. It also read the frequency generator settings and locate proper
     * net from frequency generator.
     *
     * @param fileName parameter with the path to control XML file
     * @throws JDOMException
     * @throws IOException
     * @throws JGeneriloException
     */
    public void loadXMLFile(String fileName) throws CloneNotSupportedException {
        ThermalDesignParser parser = new ThermalDesignParser(fileName);
        parser.setThermalDesign(this);
        try {
            parser.parseXML();
            this.generateFrequency(parser.getFrequency());
            if (parser.getFrequency() > 350)
                generatorNet = "CLK2XOUT_OBUF";
            else
                generatorNet = "CLKMULOUT_OBUF";
        } catch (JGeneriloException e) {
            System.err.format("XML parser error: %s\n", e.getMessage());
        }
    }

    /**
     * Adds a heaterUnit to the design
     *
     * @param heaterUnit This Heater Unit to add
     */
    public void addHeater(final HeaterUnit heaterUnit) throws JGeneriloException {
        heaterUnits.add(heaterUnit);
    }

    /**
     * Adds a collection of heaters to the design
     *
     * @param heaterUnits This ArrayList of heaterInstances to add
     */
    public void addHeaters(ArrayList<HeaterUnit> heaterUnits) throws JGeneriloException {
        for (HeaterUnit heaterUnit : heaterUnits)
            addHeater(heaterUnit);
    }

    /**
     * Adds a thermometer to the design
     *
     * @param thermUnit This thermometer to add
     */
    public void addThermometer(ThermometerUnit thermUnit) throws JGeneriloException {
        //if (checkTileSuitability(thermUnit)) {
        thermometerUnits.add(thermUnit);
        //}
    }

    /**
     * Method check if selected thermal unit is suitable with tile where user would like
     * to add heater or thermometer unit. If isn't method throws exception.
     *
     * @param thermalUnit Thermal Unit to check passing with chosen tile
     * @return boolean information about tile suitability.
     * @throws JGeneriloException
     */
    private boolean checkTileSuitability(ThermalUnit thermalUnit) throws JGeneriloException {
        final int row = thermalUnit.getRow();
        final int col = thermalUnit.getCol();
        final Tile thermalTile = getDevice().getTile(row, col);
        if (thermalTile.getPrimitiveSites() == null) {

            JGeneriloExceptionType errorType;

            HashMap<String, Object> errorMessageFormater = new HashMap<String, Object>();
            errorMessageFormater.put("col", col);
            errorMessageFormater.put("row", row);
            errorMessageFormater.put("tile_name", thermalTile.getName());

            if (true) {
                errorType = JGeneriloExceptionType.HeaterPlacementError;
                ThermalUnit heaterUnit = thermalUnit;
                errorMessageFormater.put("heater_type", heaterUnit.toString());
            } else {
                errorType = JGeneriloExceptionType.ThermometerPlacementError;
                ThermometerUnit thermUnit = (ThermometerUnit) thermalUnit;
                errorMessageFormater.put("therm_type", thermUnit.getType().toString());
            }

            throw new JGeneriloException(errorType, errorMessageFormater);
        } else {

            Iterator<PrimitiveType> primTypeIt = thermalUnit.getRequiredPrimitiveSites().iterator();

            PrimitiveType req = null;
            if (primTypeIt.hasNext()) {
                req = primTypeIt.next();
            }

            for (PrimitiveSite primitiveSite : thermalTile.getPrimitiveSites()) {
                if (primitiveSite.isCompatiblePrimitiveType(req))
                    if (primTypeIt.hasNext())
                        req = primTypeIt.next();
                    else
                        return true;
            }
            //TODO: raise an exception about unappropriated tile
            return false;
        }
    }

    /**
     * Adds a ArrayList of nets to the design
     *
     * @param nets Those nets to add
     */
    public void addNets(ArrayList<Net> nets) {
        for (Net net : nets) {
            addNet(net);
        }
    }

    /**
     * Gets a ArrayList of Heaters
     *
     * @return The heater called name, if exists, rises error otherwise
     */
    public ArrayList<HeaterUnit> getHeaterUnits() throws JGeneriloException {
        return heaterUnits;
    }

    /**
     * Gets a ArrayList of Thermometers
     *
     * @return ArrayList of Thermometer Units
     */
    public ArrayList<ThermometerUnit> getThermometerUnits() {
        return thermometerUnits;
    }

    /**
     * >
     * Get a Thermometer by its name
     *
     * @param name The name of heater to get
     * @return The heater called name, if exists, rises error otherwise
     */
    public ThermometerUnit getThermometer(String name) {
        return null;
    }

    public void placement() throws JGeneriloException {
        ThermalPlacer placer = new ThermalPlacer(this);
        placer.placeHeaters();
        placer.placeThermometers();

        System.out.println("Placement done!");
    }

    public void setXDLInFile(String xdlInFile) {
        this.xdlInFile = xdlInFile;
    }

    public void setXDLOutFile(String xdlOutFile) {
        this.xdlOutFile = xdlOutFile;
    }

    public String getXDLOutFile() {
        return xdlOutFile;
    }

    public void saveXDLFile() {
        saveXDLFile(xdlOutFile);
        System.out.format("Design saved in %s file\n", xdlOutFile);
    }

    public void generateFrequency(int frequency) {
        if (frequency > 500) {
            System.out.println("Maximum frequency is 500 Mhz");
            System.exit(0);
        } else if (frequency < 50) {
            System.out.println("Minimum frequency is 50 Mhz");
            System.exit(0);
        } else if (frequency%50 != 0) {
            System.out.println("Frequency must be value modulo 50 Mhz");
            System.exit(0);
        }

        Instance clockGenerator = this.getInstance("Inst_SystemClockGenerator/DCM_ADV_INST");

        if (frequency <= 350) {
            if (frequency%50 == 0) {
                clockGenerator.getAttribute("CLKFX_MULTIPLY").setValue(Integer.toString(frequency/50));
                clockGenerator.getAttribute("CLKFX_DIVIDE").setValue("2");
            } else {
                clockGenerator.getAttribute("CLKFX_MULTIPLY").setValue(Integer.toString(frequency/100));
            }
        } else {
            if (frequency==400)
                clockGenerator.getAttribute("CLKFX_MULTIPLY").setValue("2");
            else if (frequency==450) {
                clockGenerator.getAttribute("CLKFX_MULTIPLY").setValue("9");
                clockGenerator.getAttribute("CLKFX_DIVIDE").setValue("4");
            } else if (frequency==500) {
                clockGenerator.getAttribute("CLKFX_MULTIPLY").setValue("5");
                clockGenerator.getAttribute("CLKFX_DIVIDE").setValue("2");
            }
        }
    }

    public void removeHeaterUnit(HeaterUnit heaterUnit) {
        this.heaterUnits.remove(heaterUnit);
    }

    public String getGeneratorNet() {
        return generatorNet;
    }

}
