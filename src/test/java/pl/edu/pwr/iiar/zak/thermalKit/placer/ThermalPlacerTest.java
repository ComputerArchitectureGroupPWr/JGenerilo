package pl.edu.pwr.iiar.zak.thermalKit.placer;

import edu.byu.ece.rapidSmith.design.Net;
import edu.byu.ece.rapidSmith.design.Pin;
import org.junit.Test;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalDesign;

/**
 * Created by pawel on 22.05.14.
 */
public class ThermalPlacerTest {

    public String termometerEnableNet = "InstThermometersLogic/termometrEnable<1>";

    @Test
    public void testPlaceHeaters() throws Exception {

    }

    @Test
    public void testPlaceThermometers() throws Exception {

    }

    @Test
    public void testAddInternalNets() throws Exception {

    }

    @Test
    public void testConnectHeaterEnableNets() throws Exception {

    }

    @Test
    public void testConnectThermometerEnableNets() throws Exception {
        ThermalDesign thermalDesign = new ThermalDesign();
        thermalDesign.loadXMLFile("src/test/testData/generilo.xml");

        System.out.format("Number of pins after xml parse from factory %d\n", thermalDesign.getThermometerUnits().get(0).getInternalNets().get(0).getPins().size());
        ThermalPlacer placer = new ThermalPlacer(thermalDesign);
        placer.placeThermometers();
        placer.placeHeaters();

        for (Net net : thermalDesign.getHeaterUnits().get(0).getInternalNets()) {
            System.out.println(net.getName());
            System.out.println(net.getPins().size());
            for (Pin pin : net.getPins()) {
                System.out.print("1");
                System.out.println(pin);
            }
        }

        placer.connectThermometerEnableNets();
        placer.connectHeaterEnableNets();

        Net thermEnable = thermalDesign.getNet(termometerEnableNet);

        System.out.println("ENABLE PINS");
        for (Pin enablePins : thermEnable.getPins())
            System.out.println(enablePins);

        System.out.println("Wire pins");

        System.out.println(thermalDesign.getThermometerUnits().get(0).getInstances().get(0).getName());

        for (Net net : thermalDesign.getThermometerUnits().get(0).getInternalNets()) {
            System.out.println(net.getName());
            System.out.println(net.getPins().size());
            int i = 0;
            for (Pin pin : net.getPins()) {
                i++;
                System.out.print(i);
                System.out.println(pin);
            }
        }

    }
}
