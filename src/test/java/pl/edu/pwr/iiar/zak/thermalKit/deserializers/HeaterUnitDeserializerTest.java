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
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.HeaterUnit;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalInstance;
import pl.edu.pwr.iiar.zak.thermalKit.parser.RcFileParser;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 * @see pl.edu.pwr.iiar.zak.thermalKit.deserializers.HeaterUnitDeserializerTest
 * Created on 16.05.14.
 */
public class HeaterUnitDeserializerTest {
    @Test
    public void testDeserialize() throws Exception {
        RcFileParser parser = new RcFileParser();
        DB db = new MongoClient().getDB(parser.getDatabaseName());

        SimpleModule module = new SimpleModule();
        module.addDeserializer(HeaterUnit.class, new HeaterUnitDeserializer());
        Jongo jongo = new Jongo(db, new JacksonMapper.Builder().registerModule(module).build());

        MongoCollection heatersDBCollection = jongo.getCollection(parser.getHeaterCollection("virtex5"));
        HeaterUnit heaterUnit = heatersDBCollection.findOne(String.format("{ 'type': '%s'}", "RO1")).as(HeaterUnit.class);
    }

    @Test
    public void testDeserializeThermalInstances() throws Exception {
        Method method = HeaterUnitDeserializer.class.getDeclaredMethod("deserializeThermalInstances", ArrayNode.class);
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

        ArrayList<ThermalInstance> heaterInstances = (ArrayList<ThermalInstance>) method.invoke(new HeaterUnitDeserializer(), instanceArrayNode);

        ThermalInstance heaterInstance = heaterInstances.get(0);
        Assert.assertEquals(heaterInstance.getName(), "upper");
        Assert.assertEquals(heaterInstance.getType().toString(), "SLICEL");
    }
}
