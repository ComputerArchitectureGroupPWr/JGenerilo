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

package pl.edu.pwr.iiar.zak.thermalKit.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 *          Created on 14.05.14.
 */
public class RcFileParser {

    private JsonNode node;

    public RcFileParser() throws IOException, InterruptedException {
        File rcFile = new File(String.format("/home/%s/.jgenerilorc", System.getenv().get("USER")));

        ObjectMapper mapper = new ObjectMapper();
        node = mapper.readValue(rcFile, JsonNode.class);
    }

    public String getDatabaseName() {
        return node.get("database").get("name").asText();
    }

    public String getHeaterCollection(String deviceType) {
        return node.get("database").get("heaters collections").get(deviceType).asText();
    }

    public String getThermometersCollection(String deviceFamily) {
        return node.get("database").get("thermometers collections").get(deviceFamily).asText();
    }
}
