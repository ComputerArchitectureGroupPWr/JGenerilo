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

package pl.edu.pwr.iiar.zak.thermalKit.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 *          Created on 14.05.14.
 */
public class DBConnector {

    private DB db;

    private String databaseName;

    public DBConnector() throws IOException {
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser(new File("~/.jgenerilorc"));
        JsonNode node = jp.getCodec().readTree(jp);

        databaseName = node.get("database").asText();
    }

    public DBConnector(String databaseName) {
        this.databaseName = databaseName;
    }

    public void connectDB() throws UnknownHostException {
        db = new MongoClient().getDB(databaseName);
    }

    public DB getDB() {
        return db;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


}
