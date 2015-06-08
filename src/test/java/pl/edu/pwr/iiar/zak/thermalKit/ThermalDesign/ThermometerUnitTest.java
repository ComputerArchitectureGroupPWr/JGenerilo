package pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign;

import edu.byu.ece.rapidSmith.design.Attribute;
import edu.byu.ece.rapidSmith.design.Net;
import edu.byu.ece.rapidSmith.design.NetType;
import edu.byu.ece.rapidSmith.design.Pin;
import org.hamcrest.internal.ArrayIterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by pawel on 22.05.14.
 */
public class ThermometerUnitTest {

    private ThermometerUnit testLoadedUnit;

    private ThermometerUnit testBuildedUnit;

    @Before
    public void setUp() throws Exception {
        ThermalUnitsFactory factory = new ThermalUnitsFactory("virtex5");
        testLoadedUnit = factory.createThermometer("RO7", 22, 24, "term1", 0);
        testBuildedUnit = new ThermometerUnit();
    }

    @Test
    public void testGetType() throws Exception {
        Assert.assertEquals("RO7", testLoadedUnit.getType());
    }

    @Test
    public void testSetType() throws Exception {
        testBuildedUnit.setType("RO7");
        Assert.assertEquals("RO7", testBuildedUnit.getType());
    }

    @Test
    @Ignore
    public void testEquals() throws Exception {

    }

    @Test
    @Ignore
    public void testToString() throws Exception {

    }

    @Test
    @Ignore
    public void testClone() throws Exception {

    }

    @Test
    public void testSetCLBSize() throws Exception {
        testBuildedUnit.setCLBSize(1);
        Assert.assertEquals(1, testBuildedUnit.getCLBSize());
    }

    @Test
    public void testGetCLBSize() throws Exception {
        Assert.assertEquals(1, testLoadedUnit.getCLBSize());
    }

    @Test
    public void testSetInstancesNum() throws Exception {
        testBuildedUnit.setInstancesNum(2);
        Assert.assertEquals(2, testBuildedUnit.getInstancesNum());
    }

    @Test
    public void testGetInstancesNum() throws Exception {
        Assert.assertEquals(2, testLoadedUnit.getInstancesNum());
    }

    @Test
    public void testSetInstances() throws Exception {
        ThermalInstance thermalInstance = new ThermalInstance();
        thermalInstance.setName("lol");
        ArrayList<ThermalInstance> thermalInstances = new ArrayList<ThermalInstance>();
        thermalInstances.add(thermalInstance);
        testBuildedUnit.setInstances(thermalInstances);
        Assert.assertEquals("lol", testBuildedUnit.getInstances().get(0).getName());
    }

    @Test
    public void testGetInstances() throws Exception {
        Assert.assertNotSame(null, testLoadedUnit.getInstances());
    }

    @Test
    public void testLoadedUnitAttributes() throws Exception {
        ThermalInstance upper = testLoadedUnit.getInstances().get(0);
        ThermalInstance lower = testLoadedUnit.getInstances().get(1);

        ArrayIterator namesIt = new ArrayIterator(new String[]{"A6LUT", "B6LUT", "C6LUT", "D6LUT", "AUSED", "BUSED",
                "CUSED", "DUSED"});
        ArrayIterator valuesIt = new ArrayIterator(new String[]{"#LUT:O6=~A4", "#LUT:O6=~A4", "#LUT:O6=~A4",
                "#LUT:O6=~A4", "0", "0", "0", "0"});

        Iterator<Attribute> attrIt = upper.getAttributes().iterator();

        while (attrIt.hasNext() && namesIt.hasNext() && valuesIt.hasNext()) {
            Assert.assertEquals(attrIt.next(), new Attribute((String) namesIt.next(), "", (String) valuesIt.next()));
        }

        namesIt = new ArrayIterator(new String[]{"A6LUT", "B6LUT", "C6LUT", "D6LUT", "AUSED", "BUSED",
                "CUSED", "DUSED"});
        valuesIt = new ArrayIterator(new String[]{"#LUT:O6=~A4", "#LUT:O6=~A4", "#LUT:O6=~A4",
                "#LUT:O6=A4*A1", "0", "0", "0", "0"});

        attrIt = lower.getAttributes().iterator();

        while (attrIt.hasNext() && namesIt.hasNext() && valuesIt.hasNext()) {
            Assert.assertEquals(attrIt.next(), new Attribute((String) namesIt.next(), "", (String) valuesIt.next()));
        }
    }

    @Test
    public void testEnablePins() throws Exception {
        ThermalInstance upper = testLoadedUnit.getInstances().get(0);
        ThermalInstance lower = testLoadedUnit.getInstances().get(1);

        Assert.assertEquals(new ArrayList<Pin>(), upper.getEnablePins());
        Assert.assertNotSame(new ArrayList<Pin>(), lower.getEnablePins());

        Assert.assertEquals(new Pin(false, "D1", lower.getEnablePins().get(0).getInstance()), lower.getEnablePins().get(0));
    }

    @Test
    public void testInternalNets() throws Exception {
        ThermalInstance upper = testLoadedUnit.getInstances().get(0);
        ThermalInstance lower = testLoadedUnit.getInstances().get(1);

        ArrayIterator namesIt = new ArrayIterator(new String[]{"net1", "net2", "net3", "net4", "net5", "net6", "net7",
                "net8"});

        ArrayIterator instancesIt = new ArrayIterator(new ThermalInstance[][]{{upper, upper}, {upper, upper},
                {upper, upper}, {upper, lower}, {lower, lower}, {lower, lower}, {lower, lower}, {lower, upper}});

        ArrayIterator pinNamesIt = new ArrayIterator(new String[][]{{"D", "C4"}, {"C", "B4"}, {"B", "A4"}, {"A", "D4"},
                {"D", "C4"}, {"C", "B4"}, {"B", "A4"}, {"A", "D4"}});

        Iterator<Net> netsIt = testLoadedUnit.getInternalNets().iterator();

        while (namesIt.hasNext() && instancesIt.hasNext() && netsIt.hasNext() && pinNamesIt.hasNext()) {
            String name = (String) namesIt.next();
            Net net = (Net) netsIt.next();

            ArrayIterator instIt = new ArrayIterator((ThermalInstance[]) instancesIt.next());
            ArrayIterator pinOutput = new ArrayIterator(new Boolean[]{true, false});
            ArrayIterator pinNameIt = new ArrayIterator((String[]) pinNamesIt.next());

            Net newNet = new Net(name, NetType.WIRE);

            while (pinNameIt.hasNext() && pinOutput.hasNext() && instIt.hasNext()) {
                newNet.addPin(new Pin((Boolean) pinOutput.next(), (String) pinNameIt.next(), (ThermalInstance) instIt.next()));
            }

            Assert.assertEquals(net, newNet);
        }
    }

    @Test
    public void testInstanceNamePropagation() throws Exception {
        testLoadedUnit.getInstances().get(1).setName("dupa");
        Assert.assertEquals("dupa", testLoadedUnit.getInstances().get(1).getName());

        Assert.assertEquals("dupa", testLoadedUnit.getInternalNets().get(5).getPins().get(0).getInstanceName());
        Assert.assertEquals("dupa", testLoadedUnit.getInstances().get(1).getEnablePins().get(0).getInstanceName());
    }

    @Test
    public void testConnectWithCounter() throws Exception {

    }
}
