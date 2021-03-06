/*
 * Copyright (c) 2010 Brigham Young University
 * 
 * This file is part of the BYU RapidSmith Tools.
 * 
 * BYU RapidSmith Tools is free software: you may redistribute it 
 * and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * BYU RapidSmith Tools is distributed in the hope that it will be 
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is included with the BYU 
 * RapidSmith Tools. It can be found at doc/gpl2.txt. You may also 
 * get a copy of the license at <http://www.gnu.org/licenses/>.
 * 
 */
package edu.byu.ece.rapidSmith.util;

import edu.byu.ece.rapidSmith.design.Design;
import edu.byu.ece.rapidSmith.design.Net;
import edu.byu.ece.rapidSmith.design.NetType;
import edu.byu.ece.rapidSmith.design.Pin;

import java.util.ArrayList;

/**
 * This class will read in an XDL design and unroute and merge all
 * static source (GND/VCC) nets into GLOBAL_LOGIC0 and GLOBAL_LOGIC1
 * and also remove all sources (TIEOFFs/SLICEs). This makes the design
 * easier to be routed by the RapidSmith router.
 *
 * @author Chris Lavin
 *         Created on: Nov 15, 2010
 */
public class MergeStaticNets {

    public static void main(String[] args) {
        if (args.length != 2) {
            MessageGenerator.briefMessageAndExit("USAGE: <input.xdl> <output.xdl>");
        }

        Design design = new Design();

        design.loadXDLFile(args[0]);

        Net gnd = new Net("GLOBAL_LOGIC0", NetType.GND);
        Net vcc = new Net("GLOBAL_LOGIC1", NetType.VCC);

        ArrayList<Net> netsToRemove = new ArrayList<Net>();

        for (Net net : design.getNets()) {
            if (net.isStaticNet()) {
                netsToRemove.add(net);
                if (net.getSource() != null && net.getSource().getInstance() != null) {
                    design.getInstanceMap().remove(net.getSource().getInstance().getName());
                }
                if (net.getType().equals(NetType.GND)) {
                    for (Pin pin : net.getPins()) {
                        if (pin.isOutPin()) continue;
                        gnd.addPin(pin);
                    }
                } else if (net.getType().equals(NetType.VCC)) {
                    for (Pin pin : net.getPins()) {
                        if (pin.isOutPin()) continue;
                        vcc.addPin(pin);
                    }
                }
            }
        }

        for (Net net : netsToRemove) {
            design.removeNet(net.getName());
        }

        design.addNet(gnd);
        design.addNet(vcc);

        design.saveXDLFile(args[1], true);
    }
}
