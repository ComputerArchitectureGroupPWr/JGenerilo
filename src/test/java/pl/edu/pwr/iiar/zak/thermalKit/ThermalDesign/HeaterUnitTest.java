package pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import edu.byu.ece.rapidSmith.device.PrimitiveType;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.pwr.iiar.zak.thermalKit.deserializers.HeaterUnitDeserializer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by pawel on 10.05.14.
 */
public class HeaterUnitTest {

    private HeaterUnit testUnit;

    @Before
    public void setUp() throws Exception {
        DB db = new MongoClient().getDB("JGeneriloDB");

        SimpleModule module = new SimpleModule();
        module.addDeserializer(HeaterUnit.class, new HeaterUnitDeserializer());
        Jongo jongo = new Jongo(db, new JacksonMapper.Builder()
                .registerModule(module).build());
        MongoCollection heaters = jongo.getCollection("virtex5heaters");

        testUnit = heaters.findOne().as(HeaterUnit.class);
    }

    @Test
    @Ignore
    public void testGetType() throws Exception {

    }

    @Test
    @Ignore
    public void testSetType() throws Exception {

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
    public void testGetHeaterInstancesHashMap() throws Exception {
        Method method = HeaterUnit.class.getDeclaredMethod("getHeaterInstancesHashMap", ArrayList.class);
        method.setAccessible(true);
        ArrayList<ThermalInstance> clonedInstances = new ArrayList<ThermalInstance>();

        for (ThermalInstance inst : testUnit.getInstances())
            clonedInstances.add((ThermalInstance) inst.clone());

        HashMap<String, ThermalInstance> instances =
                (HashMap<String, ThermalInstance>) method.invoke(testUnit, clonedInstances);
        HashMap<String, ThermalInstance> instances2 =
                (HashMap<String, ThermalInstance>) method.invoke(testUnit, testUnit.getInstances());

        instances.get("upper").setName("upperOtherValue");
        instances.get("lower").setName("lowerOtherValue");

        Assert.assertNotSame(instances.get("upper").getName(), instances2.get("upper").getName());
        Assert.assertNotSame(instances.get("lower").getName(), instances2.get("lower").getName());
    }

    @Test
    public void testCloneHeaterParameters() throws Exception {
        HeaterUnit cloned = (HeaterUnit) testUnit.clone();
        cloned.setType("RO3");
        cloned.setCLBSize(10);
        cloned.setInstancesNum(11);

        //Test heater basic fields
        Assert.assertNotSame(testUnit.getType(), cloned.getType());
        Assert.assertNotSame(testUnit.getCLBSize(), cloned.getCLBSize());
        Assert.assertNotSame(testUnit.getInstancesNum(), cloned.getInstancesNum());

        //Test heater collection fields
        //Test Instances
        cloned.getInstances().get(0).setType(PrimitiveType.SLICEM);
        cloned.getInstances().get(0).setName("dupper");

        Assert.assertNotSame(cloned.getInstances().get(0).getType(),
                testUnit.getInstances().get(0).getType());

        Assert.assertNotSame(cloned.getInstances().get(0).getName(),
                testUnit.getInstances().get(0).getName());

        //Test Attributes
        cloned.getInstances().get(0).getAttributes().get(0).setValue("Testvalue");

        Assert.assertNotSame(cloned.getInstances().get(0).getAttributes().get(0).getValue(),
                testUnit.getInstances().get(0).getAttributes().get(0).getValue());

        //Test EnablePins
        cloned.getInstances().get(0).getEnablePins().get(0).setPinName("F");

        Assert.assertNotSame(cloned.getInstances().get(0).getEnablePins().get(0).getName(),
                testUnit.getInstances().get(0).getEnablePins().get(0).getName());

        //Test internalNets
        cloned.getInstances().get(0).setName("dupper");
        Assert.assertEquals(cloned.getInternalNets().get(0).getPins().get(0).getInstanceName(), "dupper");
    }

    @Test
    public void testInstanceNamePropagation() throws Exception {
        ThermalUnitsFactory factory = new ThermalUnitsFactory("virtex5");
        HeaterUnit testLoadedUnit = factory.createHeater("RO1", 1, 1, "heater1", 1);
        testLoadedUnit.getInstances().get(0).setName("dupa");
        Assert.assertEquals("dupa", testLoadedUnit.getInstances().get(0).getName());

        Assert.assertEquals("dupa", testLoadedUnit.getInternalNets().get(0).getPins().get(0).getInstanceName());
        Assert.assertEquals("dupa", testLoadedUnit.getInstances().get(0).getEnablePins().get(0).getInstanceName());
    }
}
