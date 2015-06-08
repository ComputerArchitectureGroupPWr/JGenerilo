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
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import pl.edu.pwr.iiar.zak.thermalKit.deserializers.HeaterUnitDeserializer;
import pl.edu.pwr.iiar.zak.thermalKit.deserializers.ThermometerUnitDeserializer;
import pl.edu.pwr.iiar.zak.thermalKit.parser.RcFileParser;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 *          Created on 14.05.14.
 */
public class ThermalUnitsFactory {

    /**
     * TODO: description
     */
    public HashMap<String, HeaterUnit> heaters;
    /**
     * TODO: description
     */
    public HashMap<String, ThermometerUnit> thermometers;
    /**
     * TODO: description
     */
    private RcFileParser parser;
    /**
     * TODO: description
     */
    private DB db;
    /**
     * TODO: description
     */
    private MongoCollection heatersDBCollection;
    /**
     * TODO: description
     */
    private MongoCollection thermometersDBCollection;
    /**
     * TODO: description
     */
    private String deviceFamily;
    /**
     * TODO: description
     */
    private ArrayList<String> availableHeaters;

    /**
     * TODO: description
     */
    public ThermalUnitsFactory() {
        heaters = new HashMap<String, HeaterUnit>();
        thermometers = new HashMap<String, ThermometerUnit>();
        availableHeaters = new ArrayList<String>();
        deviceFamily = null;

        try {
            parser = new RcFileParser();
        } catch (IOException e) {
            System.out.print("Your .jgenerilorc file doesn't exist or is corrupted!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.print("Your .jgenerilorc file doesn't exist or is corrupted!");
            e.printStackTrace();
        }
        try {
            db = new MongoClient().getDB(parser.getDatabaseName());
        } catch (UnknownHostException e) {
            System.out.format("Something is wrong with your connection to MongoDB database" +
                    "check the error: %s", e.toString());
        }
    }

    /**
     * TODO: description
     *
     * @param deviceFamily
     */
    public ThermalUnitsFactory(String deviceFamily) {
        this();
        this.deviceFamily = deviceFamily;
    }

    /**
     * TODO: description
     */
    private void loadHeaterCollection() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(HeaterUnit.class, new HeaterUnitDeserializer());
        Jongo jongo = new Jongo(db, new JacksonMapper.Builder().registerModule(module).build());
        heatersDBCollection = jongo.getCollection(parser.getHeaterCollection(deviceFamily));
    }

    /**
     * TODO: description
     */
    private void loadThermometerCollection() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ThermometerUnit.class, new ThermometerUnitDeserializer());
        Jongo jongo = new Jongo(db, new JacksonMapper.Builder().registerModule(module).build());

        thermometersDBCollection = jongo.getCollection(parser.getThermometersCollection(deviceFamily));
    }

    /**
     * TODO: description
     *
     * @param heaterType
     * @return
     * @throws CloneNotSupportedException
     */
    public HeaterUnit createHeater(String heaterType, int col, int row, String name, int id)
            throws CloneNotSupportedException {

        if (!heaters.containsKey(heaterType)) {
            if (heatersDBCollection == null)
                loadHeaterCollection();

            this.heaters.put(heaterType,
                    heatersDBCollection.findOne(String.format("{ 'type': '%s'}", heaterType)).as(HeaterUnit.class));
        }

        HeaterUnit heaterUnit = (HeaterUnit) heaters.get(heaterType).clone();
        heaterUnit.setCol(col);
        heaterUnit.setRow(row);
        heaterUnit.setName(name);
        heaterUnit.setId(id);

        return heaterUnit;
    }

    public ThermometerUnit createThermometer(String thermometerType, int col, int row, String name, int id)
            throws CloneNotSupportedException {

        if (!thermometers.containsKey(thermometerType)) {
            if (thermometersDBCollection == null)
                loadThermometerCollection();

            this.thermometers.put(thermometerType,
                    thermometersDBCollection.findOne(String.format("{ 'type': '%s'}", thermometerType)).
                            as(ThermometerUnit.class));
        }

        ThermometerUnit thermometerUnit = (ThermometerUnit) thermometers.get(thermometerType).clone();
        thermometerUnit.setCol(col);
        thermometerUnit.setRow(row);
        thermometerUnit.setName(name);
        thermometerUnit.setId(id);

        return thermometerUnit;
    }

    /**
     * TODO: description
     *
     * @return
     */
    public ArrayList getAvailableHeatersTypes() {
        if (availableHeaters.isEmpty()) {
            if (heatersDBCollection == null)
                loadHeaterCollection();

            Iterator<HeaterUnit> it = heatersDBCollection.
                    find("{}, {'type': 1, _id : 0}").as(HeaterUnit.class).iterator();

            while (it.hasNext()) availableHeaters.add(it.next().getType().toString());
        }

        return availableHeaters;
    }


}
