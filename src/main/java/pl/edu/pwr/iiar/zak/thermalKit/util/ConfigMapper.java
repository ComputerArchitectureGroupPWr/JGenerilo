/*
 * Copyright (c) 2014 Wroclaw University of Technology
 *
 * This file is part of the JGenerilo Thermal Emulation Tools.
 *
 * JGenerilo Thermal Emulation Tools are free software: you may redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * JGenerilo is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License is available
 * at <http://www.gnu.org/licenses/>.
 *
 */

package pl.edu.pwr.iiar.zak.thermalKit.util;

import com.fasterxml.jackson.databind.util.JSONPObject;
import edu.byu.ece.rapidSmith.design.*;
import edu.byu.ece.rapidSmith.device.Device;
import edu.byu.ece.rapidSmith.device.PrimitiveSite;
import edu.byu.ece.rapidSmith.device.PrimitiveType;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloException;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 * Created on 23.07.14.
 */
public class ConfigMapper {

    public static void main (String[] args) {
        Design design = new Design("/home/pawel/Development/CLKIPCore/clk/top.xdl");
        Device device = design.getDevice();

        Instance dcm = design.getInstance("Inst_ClockDCMGenerator/DCM_BASE_inst");

        for (Attribute x: dcm.getAttributes()) {
            System.out.println(x.toString());
        }

        System.out.println("Instance details: ");
        System.out.println("Name: " + dcm.getName());
        System.out.println("Site primitive name: " + dcm.getPrimitiveSiteName());
        System.out.println("Site primitive type: " + dcm.getPrimitiveSite().getType().toString());

        int x = dcm.getPrimitiveSite().getTile().getTileXCoordinate();
        int y = dcm.getPrimitiveSite().getTile().getTileYCoordinate();

        for (PrimitiveSite prim: device.getTile(x,y-1).getPrimitiveSites()) {
            System.out.println(prim.getName());
        }

        try {
            for (Pin pin: design.getNet("GLOBAL_LOGIC0_0").getPins())
                System.out.println(pin.toString());
        } catch (JGeneriloException e) {
            e.printStackTrace();
        }

        System.out.println(design.getInstance("XDL_DUMMY_INT_X28Y22_TIEOFF_X28Y22").getAttributes().toString());

        System.out.println(design.getInstance("ledos_BUFG").getAttributes().toString());
        System.out.println(design.getInstance("ledos_BUFG").getPrimitiveSite().getType());
        System.out.println(design.getInstance("ledos_BUFG").getType());
        System.out.println(design.getInstance("ledos_BUFG").getPinMap());
        System.out.println(design.getInstance("ledos_BUFG"));

        for (Attribute attr: design.getInstance("Inst_ClockDCMGenerator/DCM_BASE_inst_ML_NEW_OUT").getAttributes())
            System.out.println(attr.getPhysicalName()+" "+attr.getLogicalName()+" " +attr.getValue());

        System.out.println(design.getInstance("Inst_ClockDCMGenerator/DCM_BASE_inst_ML_NEW_OUT"));

        for (PrimitiveSite p: device.getAllPrimitiveSitesOfType(PrimitiveType.DCM_ADV)) {
            System.out.println(p.getName());
        }
    }

}


