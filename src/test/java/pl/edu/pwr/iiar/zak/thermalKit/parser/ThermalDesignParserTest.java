package pl.edu.pwr.iiar.zak.thermalKit.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.HeaterUnit;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalDesign;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermometerUnit;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by pawel on 12.03.14.
 */

public class ThermalDesignParserTest {

    public static String xmlsource = "src/test/testData/generilo.xml";
    public static ThermalDesignParser xml;
    public static ThermalDesign testDesign;
    public static ArrayList<HeaterUnit> testHeaters;
    public static ArrayList<ThermometerUnit> testThermometers;


    @Before
    public void setUp() throws Exception {
        xml = new ThermalDesignParser(xmlsource);
        testDesign = new ThermalDesign();
    }

    @Test
    public void testParseXML() throws Exception {
        ThermalDesignParser parser = new ThermalDesignParser(xmlsource);
        parser.setThermalDesign(testDesign);
        parser.parseXML();

        Iterator<HeaterUnit> hrealIt = testDesign.getHeaterUnits().iterator();

        System.out.println("Heater units list:");
        while (hrealIt.hasNext()) {
            HeaterUnit real = hrealIt.next();
            Assert.assertEquals("RO1", real.getType());
            System.out.format("Real: %s \nTest: %s\n", real.getName(), "O");
        }

        for (ThermometerUnit thermometerUnit : testDesign.getThermometerUnits()) {
            System.out.println(thermometerUnit.toString());
        }

        Iterator<ThermometerUnit> trealIt = testDesign.getThermometerUnits().iterator();

        System.out.println("Thermometer units list:");
        while (trealIt.hasNext()) {
            ThermometerUnit real = trealIt.next();
            System.out.format("Real: %s \nTest: %s\n", real.getName(), "0");
        }
    }

    @Test
    public void testParseThermometer() throws Exception {
    }

    @Test
    public void testGetInput_file() throws Exception {
        //Assert.assertEquals("system.ncd",xml.getInput_file());
    }

    @Test
    public void testGetOutput_file() throws Exception {
        //Assert.assertEquals("system_new.ncd",xml.getOutput_file());
    }
}
