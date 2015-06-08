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

package edu.byu.ece.rapidSmith.design;

import edu.byu.ece.rapidSmith.device.PrimitiveType;
import junit.framework.Assert;
import org.junit.Test;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalInstance;

import java.util.HashMap;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 * @see edu.byu.ece.rapidSmith.design.Net
 * Created on 16.05.14.
 */
public class NetTest {
    @Test
    public void testClone() throws Exception {
        ThermalInstance instance = new ThermalInstance();
        instance.setName("inst1");
        instance.setType(PrimitiveType.SLICEL);

        ThermalInstance instance2 = new ThermalInstance();
        instance2.setName("inst1");
        instance2.setType(PrimitiveType.SLICE);

        Net net = new Net("net1", NetType.WIRE);
        net.addPin(new Pin(false, "D", instance));
        net.addPin(new Pin(false, "C", instance));

        HashMap<String, ThermalInstance> hashMap = new HashMap<String, ThermalInstance>();
        hashMap.put("inst1", instance2);

        net.setHeaterInstances(hashMap);
        Net clonedNet = (Net) net.clone();

        instance2.setName("inst2");

        Assert.assertEquals(clonedNet.getPins().get(0).getInstanceName(), "inst2");
        Assert.assertEquals(net.getPins().get(0).getInstanceName(), "inst1");
    }
}
