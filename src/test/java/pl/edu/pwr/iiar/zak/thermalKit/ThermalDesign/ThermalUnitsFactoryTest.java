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

package pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign;

import edu.byu.ece.rapidSmith.design.Net;
import edu.byu.ece.rapidSmith.design.NetType;
import edu.byu.ece.rapidSmith.design.Pin;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 * @see pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalUnitsFactory
 * Created on 15.05.14.
 */
public class ThermalUnitsFactoryTest {

    @Test
    public void testDummyHeaterConfiguration() throws Exception {
        ThermalUnitsFactory factory = new ThermalUnitsFactory("virtex5");
        HeaterUnit heaterUseLessUnit = factory.createHeater("RO1", 10, 10, "heater1", 1);

        HeaterUnit heaterUnit = factory.heaters.get("RO1");

        //Basic parameters test
        Assert.assertEquals(heaterUnit.getType(), "RO1");

        //Instances Test
        //Instance upper
        Assert.assertEquals(heaterUnit.getInstances().get(0).getName(), "upper");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getType().toString(), "SLICEL");
        //Attributes
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(0).getPhysicalName(), "A6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(0).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(0).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(1).getPhysicalName(), "B6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(1).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(1).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(2).getPhysicalName(), "C6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(2).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(2).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(3).getPhysicalName(), "D6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(3).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(3).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(4).getPhysicalName(), "AUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(4).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(4).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(5).getPhysicalName(), "BUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(5).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(5).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(6).getPhysicalName(), "CUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(6).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(6).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(7).getPhysicalName(), "DUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(7).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(7).getValue(), "0");
        //EnablePins
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(0).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(0).getName(), "A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(1).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(1).getName(), "B1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(2).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(2).getName(), "C1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(3).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(3).getName(), "D1");
        //Instance lower
        Assert.assertEquals(heaterUnit.getInstances().get(1).getName(), "lower");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getType().toString(), "SLICEL");
        //Attributes
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(0).getPhysicalName(), "A6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(0).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(0).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(1).getPhysicalName(), "B6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(1).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(1).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(2).getPhysicalName(), "C6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(2).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(2).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(3).getPhysicalName(), "D6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(3).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(3).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(4).getPhysicalName(), "AUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(4).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(4).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(5).getPhysicalName(), "BUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(5).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(5).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(6).getPhysicalName(), "CUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(6).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(6).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(7).getPhysicalName(), "DUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(7).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(7).getValue(), "0");
        //EnablePins
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(0).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(0).getName(), "A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(1).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(1).getName(), "B1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(2).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(2).getName(), "C1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(3).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(3).getName(), "D1");
    }

    @Test
    public void testDummyThermometerConfiguration() throws Exception {
        ThermalUnitsFactory factory = new ThermalUnitsFactory("virtex5");
        ThermometerUnit thermometerUselessUnit = factory.createThermometer("RO7", 10, 10, "heater1", 1);

        ThermometerUnit thermometerUnit = factory.thermometers.get("RO7");

        //Basic parameters test
        Assert.assertEquals(thermometerUnit.getType(), "RO7");

        //Instances Test
        //Instance upper
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getName(), "upper");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getType().toString(), "SLICEL");
        //Attributes
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(0).getPhysicalName(), "A6LUT");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(0).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(0).getValue(), "#LUT:O6=~A4");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(1).getPhysicalName(), "B6LUT");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(1).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(1).getValue(), "#LUT:O6=~A4");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(2).getPhysicalName(), "C6LUT");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(2).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(2).getValue(), "#LUT:O6=~A4");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(3).getPhysicalName(), "D6LUT");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(3).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(3).getValue(), "#LUT:O6=~A4");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(4).getPhysicalName(), "AUSED");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(4).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(4).getValue(), "0");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(5).getPhysicalName(), "BUSED");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(5).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(5).getValue(), "0");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(6).getPhysicalName(), "CUSED");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(6).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(6).getValue(), "0");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(7).getPhysicalName(), "DUSED");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(7).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(0).getAttributes().get(7).getValue(), "0");
        //Instance lower
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getName(), "lower");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getType().toString(), "SLICEL");
        //Attributes
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(0).getPhysicalName(), "A6LUT");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(0).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(0).getValue(), "#LUT:O6=~A4");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(1).getPhysicalName(), "B6LUT");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(1).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(1).getValue(), "#LUT:O6=~A4");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(2).getPhysicalName(), "C6LUT");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(2).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(2).getValue(), "#LUT:O6=~A4");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(3).getPhysicalName(), "D6LUT");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(3).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(3).getValue(), "#LUT:O6=A4*A1");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(4).getPhysicalName(), "AUSED");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(4).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(4).getValue(), "0");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(5).getPhysicalName(), "BUSED");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(5).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(5).getValue(), "0");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(6).getPhysicalName(), "CUSED");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(6).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(6).getValue(), "0");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(7).getPhysicalName(), "DUSED");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(7).getLogicalName(), "");
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getAttributes().get(7).getValue(), "0");
        //EnablePins
        Assert.assertEquals(thermometerUnit.getInstances().get(1).getEnablePins().get(0).getName(), "D1");
    }

    @Test
    public void testCreateHeater() throws Exception {
        ThermalUnitsFactory factory = new ThermalUnitsFactory("virtex5");
        HeaterUnit heaterUnit = factory.createHeater("RO1", 10, 10, "heater1", 1);

        //Basic parameters test
        Assert.assertEquals(heaterUnit.getType(), "RO1");
        Assert.assertEquals(heaterUnit.getCol(), 10);
        Assert.assertEquals(heaterUnit.getRow(), 10);
        Assert.assertEquals(heaterUnit.getName(), "heater1");
        Assert.assertEquals(heaterUnit.getId(), 1);

        //Instances Test
        //Instance upper
        Assert.assertEquals(heaterUnit.getInstances().get(0).getName(), "upper");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getType().toString(), "SLICEL");
        //Attributes
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(0).getPhysicalName(), "A6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(0).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(0).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(1).getPhysicalName(), "B6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(1).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(1).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(2).getPhysicalName(), "C6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(2).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(2).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(3).getPhysicalName(), "D6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(3).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(3).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(4).getPhysicalName(), "AUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(4).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(4).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(5).getPhysicalName(), "BUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(5).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(5).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(6).getPhysicalName(), "CUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(6).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(6).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(7).getPhysicalName(), "DUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(7).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getAttributes().get(7).getValue(), "0");
        //EnablePins
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(0).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(0).getName(), "A1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(1).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(1).getName(), "B1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(2).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(2).getName(), "C1");
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(3).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(0).getEnablePins().get(3).getName(), "D1");
        //Instance lower
        Assert.assertEquals(heaterUnit.getInstances().get(1).getName(), "lower");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getType().toString(), "SLICEL");
        //Attributes
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(0).getPhysicalName(), "A6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(0).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(0).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(1).getPhysicalName(), "B6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(1).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(1).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(2).getPhysicalName(), "C6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(2).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(2).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(3).getPhysicalName(), "D6LUT");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(3).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(3).getValue(), "#LUT:O6=~A4*A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(4).getPhysicalName(), "AUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(4).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(4).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(5).getPhysicalName(), "BUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(5).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(5).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(6).getPhysicalName(), "CUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(6).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(6).getValue(), "0");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(7).getPhysicalName(), "DUSED");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(7).getLogicalName(), "");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getAttributes().get(7).getValue(), "0");
        //EnablePins
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(0).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(0).getName(), "A1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(1).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(1).getName(), "B1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(2).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(2).getName(), "C1");
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(3).isOutPin(), false);
        Assert.assertEquals(heaterUnit.getInstances().get(1).getEnablePins().get(3).getName(), "D1");
    }

    @Test
    public void testCreateThermometer() throws Exception {
        ThermalUnitsFactory factory = new ThermalUnitsFactory("virtex5");
        ThermometerUnit thermometerUnit1 = factory.createThermometer("RO7", 10, 10, "therm1", 1);

        Assert.assertEquals(thermometerUnit1.getName(), "therm1");
    }

    @Test
    public void testGetAvailableHeatersTypes() throws Exception {
        ThermalUnitsFactory factory = new ThermalUnitsFactory("virtex5");
        ArrayList<String> heaters = new ArrayList<String>();
        heaters.add("RO1");
        heaters.add("RO3");
        Assert.assertEquals(factory.getAvailableHeatersTypes(), heaters);
    }

    @Test
    public void testCloningHeaters() throws Exception {
        ThermalUnitsFactory factory = new ThermalUnitsFactory("virtex5");
        HeaterUnit heaterUnit1 = factory.createHeater("RO1", 10, 10, "heater1", 1);
        HeaterUnit heaterUnit2 = factory.createHeater("RO3", 10, 10, "heater2", 1);

        for (Net net : heaterUnit1.getInternalNets())
            System.out.println(net.getName());

        for (Net net : heaterUnit2.getInternalNets())
            System.out.println(net.getName());
    }

    @Test
    public void instanceNetTest() throws Exception {
        ThermalInstance instance = new ThermalInstance();
        instance.setName("lol");
        Net net = new Net("wire1", NetType.WIRE);
        Pin pin = new Pin(false, "E", instance);
        net.addPin(pin);

        instance.setName("buuu!");

        System.out.println(net.getPins().get(0).getInstanceName());

    }

    @Test
    public void testInternalThermometersNets() throws Exception {

    }
}
