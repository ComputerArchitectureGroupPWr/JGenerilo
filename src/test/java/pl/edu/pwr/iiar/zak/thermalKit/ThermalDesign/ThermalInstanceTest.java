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

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import edu.byu.ece.rapidSmith.design.Attribute;
import edu.byu.ece.rapidSmith.design.Pin;
import edu.byu.ece.rapidSmith.device.PrimitiveType;
import junit.framework.Assert;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.iiar.zak.thermalKit.deserializers.HeaterUnitDeserializer;

import java.util.ArrayList;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 * @see pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalInstance
 * Created on 14.05.14.
 */
public class ThermalInstanceTest {

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
    public void testToString() throws Exception {

    }

    @Test
    public void testSetEnablePins() throws Exception {

    }

    @Test
    public void testClone() throws Exception {
        ThermalInstance instance = new ThermalInstance();
        instance.setName("Instance1");
        instance.setType(PrimitiveType.SLICEL);

        ArrayList<Pin> pins = new ArrayList<Pin>();
        pins.add(new Pin(false, "D", instance));
        pins.add(new Pin(false, "C", instance));

        instance.setEnablePins(pins);

        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("LUT6", "", "value1"));
        attributes.add(new Attribute("LUT8", "", "value2"));

        instance.setAttributes(attributes);

        ThermalInstance newInstance = (ThermalInstance) instance.clone();

        newInstance.setName("Instance2");
        newInstance.setType(PrimitiveType.SLICEM);

        newInstance.getEnablePins().get(0).setInstance(newInstance);
        newInstance.getEnablePins().get(1).setInstance(newInstance);

        newInstance.getAttributes().get(0).setPhysicalName("LUT1");
        newInstance.getAttributes().get(1).setPhysicalName("LUT2");

        Assert.assertNotSame(instance.getName(), newInstance.getName());
        Assert.assertNotSame(instance.getType(), newInstance.getType());

        Assert.assertEquals(instance.getEnablePins().get(0), new Pin(false, "D", instance));
        Assert.assertEquals(instance.getEnablePins().get(1), new Pin(false, "C", instance));
        Assert.assertEquals(newInstance.getEnablePins().get(0), new Pin(false, "D", newInstance));
        Assert.assertEquals(newInstance.getEnablePins().get(1), new Pin(false, "C", newInstance));

        Assert.assertEquals(instance.getAttributes().get(0), new Attribute("LUT6", "", "value1"));
        Assert.assertEquals(instance.getAttributes().get(1), new Attribute("LUT8", "", "value2"));
        Assert.assertEquals(newInstance.getAttributes().get(0), new Attribute("LUT1", "", "value1"));
        Assert.assertEquals(newInstance.getAttributes().get(1), new Attribute("LUT2", "", "value2"));

    }
}
