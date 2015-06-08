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
import edu.byu.ece.rapidSmith.design.Pin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 *          Created on 25.03.14.
 */

public class ThermalDesignTest {

    public ThermalDesign design = new ThermalDesign();
    public ArrayList<HeaterUnit> testHeaterUnits = new ArrayList<HeaterUnit>();
    public ArrayList<ThermometerUnit> testThermometers = new ArrayList<ThermometerUnit>();

    @Before
    public void setUp() throws Exception {
        //testHeaterUnits.add(new HeaterUnit("heater1", "RO1", 1, 44, 15));
        //testHeaterUnits.add(new HeaterUnit("heater1", "RO1", 1, 44, 16));
        //testHeaterUnits.add(new HeaterUnit("heater1", "RO1", 1, 44, 17));
        //testHeaterUnits.add(new HeaterUnit("heater2", "RO1", 2, 44, 18));
        //testHeaterUnits.add(new HeaterUnit("heater2", "RO1", 2, 44, 19));
        //testHeaterUnits.add(new HeaterUnit("heater2", "RO1", 2, 44, 20));
        //testHeaterUnits.add(new HeaterUnit("heater3", "RO1", 3, 44, 21));

        testThermometers.add(new ThermometerUnit("thermometer1", "RO7", 1, 18, 20));
        testThermometers.add(new ThermometerUnit("thermometer2", "RO7", 1, 18, 21));
    }

    @Test
    @Ignore
    public void testLoadXDLFile() throws Exception {

        design.loadXDLFile("src/test/testData/SimCore.xdl");
        Net clockNet = design.getNet("clk_BUFGP");

        for (Pin pin : clockNet.getPins()) {
            if (!pin.isOutPin())
                if (pin.getInstanceName().contains("InstThermometersLogic/termometr<"))
                    System.out.println(pin.getInstanceName());
        }
    }

    @Test
    public void testConnectCountersToThermometers() throws Exception {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        design.loadXDLFile("src/test/testData/SimCore.xdl");
        design.addThermometer(new ThermometerUnit("thermometer2", "RO7", 1, 18, 21));

        System.out.println("Counter connections test");
        String counterInstanceName = "InstThermometersLogic/termometr<%d>";
        for (ThermometerUnit thermometerUnit : design.getThermometerUnits()) {
        }

    }


    @Test
    @Ignore
    public void testLoadXMLFile() throws Exception {
        design.loadXMLFile("src/test/testData/generilo.xml");
        Iterator<HeaterUnit> xmlIt = design.getHeaterUnits().iterator();
        Iterator<HeaterUnit> testIt = testHeaterUnits.iterator();

        while (xmlIt.hasNext() && testIt.hasNext()) {
            HeaterUnit testHeater = testIt.next();
            HeaterUnit xmlHeater = xmlIt.next();
            Assert.assertTrue(testHeater.equals(xmlHeater));
        }
    }

    @Test
    public void testAddHeater() throws Exception {
        design.setPartName("xc5vlx110tff1136-2");
        //HeaterUnit heaterUnit = new HeaterUnit("heater1", "RO1", 1, 52, 50);
        //design.addHeater(heaterUnit);
        HeaterUnit testHeaterUnit = design.getHeaterUnits().get(0);
        //System.out.format("Real: %s, test: %s\n", heaterUnit.toString(), testHeaterUnit.toString());
        //Assert.assertTrue(heaterUnit.equals(testHeaterUnit));
    }

    @Test
    public void testAddHeaters() throws Exception {
    }

    @Test
    public void testAddThermometer() throws Exception {
        design.setPartName("xc5vlx110tff1136-2");
        ThermometerUnit thermometerUnit = new ThermometerUnit(
                "thermometer1", "RO7", 1, 44, 18);
        design.addThermometer(thermometerUnit);
        ThermometerUnit testThermUnit = design.getThermometerUnits().get(0);
        System.out.format("Real: %s, test: %s\n", thermometerUnit.toString(), testThermUnit.toString());
        Assert.assertTrue(thermometerUnit.equals(testThermUnit));
    }

    @Test
    public void testCheckTileSuitability() throws Exception {
        //Test for heater
        design.setPartName("xc5vlx110tff1136-2");
        //HeaterUnit heaterUnit = new HeaterUnit("heater1", "RO1", 1, 52, 50);
        ThermometerUnit thermometerUnit =
                new ThermometerUnit("thermometer1", "RO7", 1, 44, 18);

        Method checkTileSuitability = ThermalDesign.class.getDeclaredMethod("checkTileSuitability", ThermalUnit.class);
        checkTileSuitability.setAccessible(true);

        //Assert.assertTrue((Boolean) checkTileSuitability.invoke(design, heaterUnit));
        //System.out.println(checkTileSuitability.invoke(design, heaterUnit));
        System.out.println(checkTileSuitability.invoke(design, thermometerUnit));
        Assert.assertTrue((Boolean) checkTileSuitability.invoke(design, thermometerUnit));

    }

    @Test
    public void testAddNets() throws Exception {

    }

    @Test
    public void testGetHeaterInstances() throws Exception {

    }

    @Test
    public void testGetThermometer() throws Exception {

    }

    @Test
    public void testConnectEnableNets() throws Exception {
        design.setPartName("xc5vlx110tff1136-2");
        //HeaterUnit heaterUnit = new HeaterUnit("heater1", "RO1", 1, 52, 50);
        //design.addHeater(heaterUnit);

        //design.connectEnableNets();
    }

    @Test
    public void testPlaceAndRouteHeaters() throws Exception {
        design.setPartName("xc5vlx110tff1136-2");
        //HeaterUnit heaterUnit = new HeaterUnit("heater1", "RO1", 1, 52, 50);
        //design.addHeater(heaterUnit);
        //design.placeAndRouteHeaters();
        //Assert.assertNotNull(design.getInstance("heater_SLICE_X32Y114"));
        //Assert.assertNotNull(design.getInstance("heater_SLICE_X33Y114"));
    }

    @Test
    public void testEqual() throws Exception {
        //HeaterUnit heaterUnit = new HeaterUnit("heater1", "RO1", 1, 52, 50);
        //HeaterUnit heaterUnit2 = new HeaterUnit("heater1", "RO1", 1, 52, 50);
        //Assert.assertTrue(heaterUnit.equals(heaterUnit2));
    }
}
