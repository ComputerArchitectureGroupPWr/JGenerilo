/*
 * Copyright (c) 2014 Wroclaw University of Technology
 *
 * This file is part of the pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo Thermal Emulation Tools.
 *
 * pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo Thermal Emulation Tools are free software: you
 * may redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2 of the License, or (at your
 * option) any later version.
 *
 * pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * A copy of the GNU General Public License is available
 * at <http://www.gnu.org/licenses/>.
 *
 */
package pl.edu.pwr.iiar.zak.thermalKit.parser;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalDesign;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalUnitsFactory;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The design class supports ThermalDesignClass in paring the control xml file with the
 * configuration with thermal design/floorplan. Configuration of thermal floorplan indicates
 * the location and position of heaters and thermometers. The example of such control file
 * is avialable on the project web site and in doc directory.
 *
 * @author Pawel Weber
 * @version 0.1
 * @see pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalDesign
 * Created on: Mar 28, 2014
 */

public class ThermalDesignParser {

    /**
     * Name of file to parse
     */
    private String filename = null;
    /**
     * Thermal design instance for parsed file
     */
    private ThermalDesign thermalDesign = null;
    /**
     * XML reader instance
     */
    private Document reader = null;

    private ThermalUnitsFactory factory;

    private ArrayList<ThermalElement> thermometers = new ArrayList<ThermalElement>();
    private ArrayList<ThermalElement> heaters = new ArrayList<ThermalElement>();
    private String input_file;
    private String output_file;
    private String deviceFamily;
    private int frequency;

    public ThermalDesignParser(String filename) {
        this.filename = filename;
        try {
            SAXBuilder jdombuilder = new SAXBuilder();
            reader = jdombuilder.build(filename);
        } catch (JDOMException e) {
            System.out.println("XML Thermal Desig/n parser ERROR: Selected filename %s is incorrect xml file.");
        } catch (IOException e) {
            System.out.println("XML Thermal Design parser ERROR: Could not find file:" + filename);
        }
    }

    public void setThermalDesign(ThermalDesign thermalDesign) {
        this.thermalDesign = thermalDesign;
    }

    public void parseXML() throws JGeneriloException, CloneNotSupportedException {
        Element control_file = reader.getRootElement();

        if (control_file.getName().equals("board"))
            parseBoard(control_file);

        for (Element child : control_file.getChildren()) {
            if (child.getName().equals("input"))
                parseInput(child);
            if (child.getName().equals("output"))
                parseOutput(child);
            if (child.getName().equals("thermometer")) {
                parseThermometer(child);
            } else if (child.getName().equals("heater")) {
                parseHeater(child);
            }
        }
    }

    private void parseBoard(Element element) {
        deviceFamily = element.getAttributeValue("device").toLowerCase();
        factory = new ThermalUnitsFactory(deviceFamily);
        try {
            frequency = new Integer(element.getAttributeValue("frequency"));
        } catch (Exception e) {
            System.out.println("WARNING: You have not declared a frequency for heaters: default" +
                    " value set to 100 MHz.");
            frequency = 100;
        }
    }

    private void parseInput(Element element) {
        System.out.println("Converting NCD file to XDL...");
        thermalDesign.loadNCDFile(element.getAttributeValue("name"));
    }

    private void parseOutput(Element element) {
        thermalDesign.setXDLOutFile(element.getAttributeValue("name"));
    }

    private void parseThermometer(Element element) throws JGeneriloException, CloneNotSupportedException {

        Pattern pattern;
        pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(element.getAttributeValue("name"));
        matcher.find();

        thermalDesign.addThermometer(factory.createThermometer(
                element.getAttributeValue("type"),
                Integer.parseInt(element.getAttributeValue("col")),
                Integer.parseInt(element.getAttributeValue("row")),
                element.getAttributeValue("name"),
                new Integer(matcher.group(0))));
    }

    private void parseHeater(Element heaterHead) throws JGeneriloException, CloneNotSupportedException {
        String name = heaterHead.getAttributeValue("name");
        String type = heaterHead.getAttributeValue("type");

        Pattern pattern;
        pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(name);
        matcher.find();

        for (Element heater : heaterHead.getChildren())
            try {
                int col = Integer.parseInt(heater.getAttributeValue("col"));
                int row = Integer.parseInt(heater.getAttributeValue("row"));
                thermalDesign.addHeater(factory.createHeater(
                        type,
                        col,
                        row,
                        String.format("%s_x%d_y%d", name, col, row),
                        new Integer(matcher.group(0))));
            } catch (JGeneriloException e) {
                System.out.format("XML parser ERROR: %s \n", e.getMessage());
            }
    }

    //GET METHODS//
    public ArrayList<ThermalElement> getHeaters() {
        return heaters;
    }

    public ArrayList<ThermalElement> getThermometers() {
        return thermometers;
    }

    public String getInput_file() {
        return input_file;
    }

    public String getOutput_file() {
        return output_file;
    }

    public int getFrequency() {
        return this.frequency;
    }
}