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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import edu.byu.ece.rapidSmith.design.Attribute;
import edu.byu.ece.rapidSmith.design.Net;
import edu.byu.ece.rapidSmith.design.NetType;
import edu.byu.ece.rapidSmith.design.Pin;
import edu.byu.ece.rapidSmith.device.PrimitiveType;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.HeaterUnit;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalInstance;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * HeaterUnitDeserializer is the recipe how to deserialize the HeaterDescription from
 * the JSON file or MongoDB.
 *
 * @author pawel
 * @version ${VERSION}
 *          Created on 13.05.14.
 */
public class HeaterUnitDeserializer extends JsonDeserializer<HeaterUnit> {

    /**
     * Override main method which deserializes the JSON description of the heater to Java
     * object.
     *
     * @param jp                     JSON parser
     * @param deserializationContext Deserialization context
     * @return HeaterUnit obejct
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public HeaterUnit deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        HeaterUnit heaterUnit = new HeaterUnit();

        try {
            heaterUnit.setType(node.get("type").asText());
        } catch (JGeneriloException e) {
            e.printStackTrace();
        }
        heaterUnit.setCLBSize((Integer) (node.get("CLBsize")).numberValue());
        heaterUnit.setInstancesNum((Integer) (node.get("instancesNum")).numberValue());
        heaterUnit.setInstances(deserializeThermalInstances((ArrayNode) node.get("instances")));
        heaterUnit.setInternalNets(deserializeNets((ArrayNode) node.get("nets"),
                createInstancesHashMap(heaterUnit.getInstances())));

        return heaterUnit;
    }

    /**
     * Method supports main deserialization method. It deserializes
     * the instances objects which are members of HeaterUnit object
     *
     * @param instancesArrayNode JSON ArrayNode Object
     * @return ArrayList of thermalInstances
     */
    private ArrayList<ThermalInstance> deserializeThermalInstances(ArrayNode instancesArrayNode) {
        ArrayList<ThermalInstance> heaterInstances = new ArrayList<ThermalInstance>();

        for (JsonNode instanceNode : instancesArrayNode) {
            ThermalInstance heaterInstance = new ThermalInstance();

            heaterInstance.setName(instanceNode.get("name").asText());
            heaterInstance.setType(PrimitiveType.valueOf(instanceNode.get("primitiveType").asText()));
            heaterInstance.setAttributes(deserializeAttributes((ArrayNode) instanceNode.get("attributes")));
            heaterInstance.setEnablePins(deserializeEnablePins((ArrayNode) instanceNode.get("enablePins"), heaterInstance));

            heaterInstances.add(heaterInstance);
        }

        return heaterInstances;
    }

    private HashMap<String, ThermalInstance> createInstancesHashMap(ArrayList<ThermalInstance> instances) {
        HashMap<String, ThermalInstance> hashMap = new HashMap<String, ThermalInstance>();

        for (ThermalInstance inst : instances)
            hashMap.put(inst.getName(), inst);

        return hashMap;
    }

    /**
     * Method supports main deserialization method. It deserializes
     * the attributes which are members of ThermalInstance object
     *
     * @param attributesNode JSON ArrayNode Object
     * @return ArrayList of attributes
     */
    private ArrayList<Attribute> deserializeAttributes(ArrayNode attributesNode) {
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();

        for (JsonNode attrNode : attributesNode) {
            Attribute attr = new Attribute();

            attr.setPhysicalName(attrNode.get("physicalName").asText());
            attr.setLogicalName(attrNode.get("logicalName").asText());
            attr.setValue(attrNode.get("value").asText());

            attributes.add(attr);
        }

        return attributes;
    }

    /**
     * Method supports main deserialization method. It desarializes
     * the pins which are members of Net object.
     *
     * @param pinsNode       JSON ArrayNode object
     * @param heaterInstance Reference to heaterInstance
     * @return ArrayList of Pins objects
     */
    private ArrayList<Pin> deserializeEnablePins(ArrayNode pinsNode, ThermalInstance heaterInstance) {
        ArrayList<Pin> enablePins = new ArrayList<Pin>();

        for (JsonNode pin : pinsNode) {
            enablePins.add(new Pin(pin.get("isOutputPin").asBoolean(), pin.get("name").asText(), heaterInstance));
        }

        return enablePins;
    }

    /**
     * Method supports main deserialization method. It deserializes
     * the pins which are members of Net object.
     *
     * @param pinsNode        JSON ArrayNode object
     * @param heaterInstances Reference to heaterInstance
     * @return ArrayList of Pins objects
     */
    private ArrayList<Pin> deserializePins(ArrayNode pinsNode, HashMap<String, ThermalInstance> heaterInstances) {
        ArrayList<Pin> pins = new ArrayList<Pin>();

        for (JsonNode pin : pinsNode) {
            ThermalInstance instance = heaterInstances.get(pin.get("instance").asText());

            pins.add(new Pin(pin.get("isOutputPin").asBoolean(), pin.get("name").asText(), instance));
        }

        return pins;
    }

    /**
     * Methods supports main deserialization method. It deserializes
     * the internal nets which are members of HeaterUnit objects.
     *
     * @param netsNode       JSON ArrayNode object
     * @param heaterInstances Reference to heaterInstance
     * @return ArrayList of Net objects
     */
    private ArrayList<Net> deserializeNets(ArrayNode netsNode, HashMap<String, ThermalInstance> heaterInstances) {
        ArrayList<Net> nets = new ArrayList<Net>();

        try{
            for (JsonNode netNode : netsNode) {
                Net net = new Net();
                net.setName(netNode.get("name").asText());
                net.setType(NetType.valueOf(netNode.get("netType").asText()));
                net.addPins(deserializePins((ArrayNode) netNode.get("pins"), heaterInstances));
                nets.add(net);
            }
        } catch (Exception e) {

        }

        return nets;
    }

}