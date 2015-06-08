package pl.edu.pwr.iiar.zak.thermalKit.deserializers;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.junit.Assert;
import org.junit.Test;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalInstance;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalUnit;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermometerUnit;
import pl.edu.pwr.iiar.zak.thermalKit.parser.RcFileParser;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by pawel on 22.05.14.
 */
public class ThermometerUnitDeserializerTest {
    @Test
    public void testDeserialize() throws Exception {
        RcFileParser parser = new RcFileParser();
        DB db = new MongoClient().getDB(parser.getDatabaseName());

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ThermalUnit.class, new ThermometerUnitDeserializer());
        Jongo jongo = new Jongo(db, new JacksonMapper.Builder().registerModule(module).build());

        MongoCollection thermometersDBCollection = jongo.getCollection(parser.getThermometersCollection("virtex5"));
        ThermometerUnit thermometerUnit = thermometersDBCollection.findOne(String.format("{ 'type': '%s'}", "RO1")).as(ThermometerUnit.class);
    }

    @Test
    public void testDeserializeThermalInstances() throws Exception {
        Method method = ThermometerUnitDeserializer.class.getDeclaredMethod("deserializeThermalInstances", ArrayNode.class);
        method.setAccessible(true);

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode instanceArrayNode = new ArrayNode(factory);
        ObjectNode instanceNode = new ObjectNode(factory);
        instanceArrayNode.add(instanceNode);

        instanceNode.put("name", "upper");
        instanceNode.put("primitiveType", "SLICEL");

        ArrayNode attributeArrayNode = new ArrayNode(factory);
        instanceNode.put("attributes", attributeArrayNode);
        ObjectNode attributeNode = new ObjectNode(factory);
        attributeArrayNode.add(attributeNode);
        attributeNode.put("physicalName", "phyName1");
        attributeNode.put("logicalName", "logName1");
        attributeNode.put("value", "value1");

        ArrayNode enablePinsNode = new ArrayNode(factory);
        instanceNode.put("enablePins", enablePinsNode);
        ObjectNode enablePinNode = new ObjectNode(factory);
        enablePinsNode.add(enablePinNode);
        enablePinNode.put("isOutputPin", false);
        enablePinNode.put("name", "D3");

        System.out.print(instanceArrayNode.toString());

        ArrayList<ThermalInstance> termometerInstances = (ArrayList<ThermalInstance>) method.invoke(new ThermometerUnitDeserializer(), instanceArrayNode);

        ThermalInstance heaterInstance = termometerInstances.get(0);
        Assert.assertEquals(heaterInstance.getName(), "upper");
        Assert.assertEquals(heaterInstance.getType().toString(), "SLICEL");
    }
}
