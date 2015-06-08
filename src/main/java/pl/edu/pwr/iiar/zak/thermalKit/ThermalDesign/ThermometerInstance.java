/*
 * Copyright (c) 2014 Wroclaw University of Technology
 *
 * This file is part of the pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo Thermal Emulation Tools.
 *
 * pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo Thermal Emulation Tools are free software: you may redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License is available
 * at <http://www.gnu.org/licenses/>.
 *
 */
package pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign;

import edu.byu.ece.rapidSmith.design.Attribute;
import edu.byu.ece.rapidSmith.design.Net;
import edu.byu.ece.rapidSmith.design.Pin;
import edu.byu.ece.rapidSmith.device.PrimitiveType;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloException;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloExceptionType;

import java.util.ArrayList;

/**
 * Created by pawel on 19.03.14.
 */
public class ThermometerInstance extends ThermalInstance {

    /**
     * Thermometer type *
     */
    private ThermometerInstanceType thermometerType;
    /**
     * ThermometerEnablePin *
     */
    private ArrayList<Pin> thermometerEnablePins;

    /**
     * Constructor of class Thermometer. Initializes the class
     * members like collections and other object types.
     */
    public ThermometerInstance() {
        super();
        thermometerEnablePins = new ArrayList<Pin>();
    }

    public ThermometerInstance(ThermometerInstanceType thermometerType) {
        super();
        this.thermometerType = thermometerType;

    }

    /**
     * Set type method set the type of thermometer as well as
     * set additional attributes which describes selected type
     * of thermometer.
     *
     * @param type This type of thermometer to set.
     * @throws JGeneriloException If selected type doesn't
     *                            exists throws the pl.edu.pwr.iiar.tywin.tywin.pl.edu.pwr.iiar.zak.thermalKit.util.JGenerilo exception.
     */
    public void setThermType(ThermometerInstanceType type) throws JGeneriloException {
        setType(PrimitiveType.SLICEL);
        this.thermometerType = type;

        switch (type) {
            case RO7u:
                //Set attributes for upperSlice of thermometer unit RO7
                super.addAttribute(new Attribute("A5LUT", "", "#LUT:O5=~A4"));
                super.addAttribute(new Attribute("B5LUT", "", "#LUT:O5=~A4"));
                super.addAttribute(new Attribute("C5LUT", "", "#LUT:O5=~A4"));
                super.addAttribute(new Attribute("D5LUT", "", "#LUT:O5=~A4"));
                super.addAttribute(new Attribute("AOUTMUX", "", "O5"));
                super.addAttribute(new Attribute("BOUTMUX", "", "O5"));
                super.addAttribute(new Attribute("COUTMUX", "", "O5"));
                super.addAttribute(new Attribute("DOUTMUX", "", "O5"));
                break;
            case RO7l:
                //Set attributes for lowerSlice of thermometer unit RO7
                super.addAttribute(new Attribute("A5LUT", "", "#LUT:O5=A3*A4"));
                super.addAttribute(new Attribute("B5LUT", "", "#LUT:O5=~A4"));
                super.addAttribute(new Attribute("C5LUT", "", "#LUT:O5=~A4"));
                super.addAttribute(new Attribute("D5LUT", "", "#LUT:O5=~A4"));
                super.addAttribute(new Attribute("AOUTMUX", "", "O5"));
                super.addAttribute(new Attribute("BOUTMUX", "", "O5"));
                super.addAttribute(new Attribute("COUTMUX", "", "O5"));
                super.addAttribute(new Attribute("DOUTMUX", "", "O5"));
                //Set thermometer enable pin
                thermometerEnablePins.add(new Pin(false, "A3", this));
                break;
            default:
                throw new JGeneriloException(JGeneriloExceptionType.ThermometerTypeError);
        }
    }

    public ArrayList<Net> getThermometerInternalNets() {
        ArrayList<Net> nets = new ArrayList<Net>();

        switch (thermometerType) {
            case RO7u:
                break;
            case RO7l:
                break;
            default:
                break;
        }
        return nets;
    }

    public void connectThermometerWithLogic(Net thermometerEnableNet) {
        switch (thermometerType) {
            case RO7l:
                break;
            default:
                break;
        }
    }

    public ArrayList<Pin> getThermometerEnablePins() {
        return thermometerEnablePins;
    }
}
