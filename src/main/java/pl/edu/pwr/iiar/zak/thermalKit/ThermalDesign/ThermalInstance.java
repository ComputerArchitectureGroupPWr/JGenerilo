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
import edu.byu.ece.rapidSmith.design.Instance;
import edu.byu.ece.rapidSmith.design.Pin;

import java.util.ArrayList;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 * @see edu.byu.ece.rapidSmith.design.Instance
 * Created on 20.03.14.
 */

public class ThermalInstance extends Instance implements Cloneable {

    /**
     * Enable pins
     */
    protected ArrayList<Pin> enablePins;

    public ThermalInstance() {
        super();
    }

    @Override
    public String toString() {
        String info = "" +
                "Instance name %s in %s PrimitiveType \n" +
                "The list of attributes:\n" +
                "%s\n";

        return String.format(info, this.getName(), this.getType(), this.getAttributes());
    }

    public void setEnablePins(ArrayList<Pin> enablePins) {
        this.enablePins = enablePins;
    }

    public ArrayList<Pin> getEnablePins() {
        return enablePins;
    }

    public Object clone() throws CloneNotSupportedException {
        ThermalInstance clone = (ThermalInstance) super.clone();
        ArrayList<Attribute> clonedAttrs = new ArrayList<Attribute>();
        ArrayList<Pin> clonedEnablePins = new ArrayList<Pin>();

        for (Attribute attr : this.getAttributes())
            clonedAttrs.add(new Attribute(attr.getPhysicalName(), attr.getLogicalName(), attr.getValue()));

        for (Pin pin : this.enablePins)
            clonedEnablePins.add(new Pin(pin.isOutPin(), pin.getName(), clone));

        clone.setAttributes(clonedAttrs);
        clone.setEnablePins(clonedEnablePins);

        return clone;
    }

}
