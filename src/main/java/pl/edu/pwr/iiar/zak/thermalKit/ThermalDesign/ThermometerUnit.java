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

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.byu.ece.rapidSmith.design.Instance;
import edu.byu.ece.rapidSmith.design.Net;
import edu.byu.ece.rapidSmith.design.Pin;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * TODO Write module description
 *
 * @author Pawel Weber
 * @version 0.1
 * @see pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalUnit
 * Created on 21.03.14.
 */

public class ThermometerUnit extends ThermalUnit implements Cloneable {

    //========================================================================//
    // Class Members
    //========================================================================//
    /**
     * Type of heater unit *
     */
    @JsonProperty("type")
    private String type;
    /**
     * ArrayList if heater instances *
     */
    @JsonProperty("instances")
    private ArrayList<ThermalInstance> instances;
    /**
     * Size of heater unit in CLBs
     */
    @JsonProperty("CLBSize")
    private int CLBSize;
    /**
     * Number of required instances
     */
    @JsonProperty("instancesNum")
    private int instancesNum;

    /**
     * Constructor, initializes all objects to new
     */
    public ThermometerUnit() {
        super();
        type = null;
        CLBSize = 0;
    }

    public ThermometerUnit(String type, int CLBsize, int instanceNum) {
        super();
        this.type = type;
        this.CLBSize = CLBsize;
        this.instancesNum = instanceNum;
    }


    /**
     * Initializes
     *
     * @param name name of the heater
     * @param type type of the heater
     * @param id   id of heater for control purposes
     * @param col  y position on fpga die
     * @param row  x position on fpga die
     * @throws JGeneriloException
     */
    public ThermometerUnit(String name, String type, int id, int col, int row) throws JGeneriloException {
        //instances = new ArrayList<HeaterInstance>();
        this.type = type;
    }

    //========================================================================//
    // Getter and Setter Methods
    //========================================================================//

    /**
     * Gets the type of heater unit
     *
     * @return heater unit type
     */
    public String getType() {
        return type;
    }

    /**
     * Set type of heater unit e.g. one stage ring oscillator and sets equivalent
     * heater instances.
     *
     * @param type
     */
    public void setType(String type) throws JGeneriloException {
        this.type = type;
    }

    /**
     * @param obj
     * @return the equality or inquality of objects
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ThermometerUnit)) return false;
        ThermometerUnit thermometerUnit = (ThermometerUnit) obj;

        boolean isEqual = true;

        if (this.type != thermometerUnit.type)
            isEqual = false;

        Iterator<ThermalInstance> thisIt = this.instances.iterator();
        Iterator<ThermalInstance> eqIt = thermometerUnit.instances.iterator();

        while (thisIt.hasNext() && eqIt.hasNext()) {
            if (!thisIt.next().equals(eqIt.next()))
                isEqual = false;
        }

        return isEqual;
    }

    @Override
    public String toString() {
        String info = "ThermometerUnit type %s \n" +
                "Occupies %d CLBs and %d PrimitiveTypes \n" +
                "Insctances: \n\n";

        for (ThermalInstance instance : instances)
            info += String.format("%s\n", instance);

        return String.format(info, type, CLBSize, instancesNum);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        ThermometerUnit clone = (ThermometerUnit) super.clone();
        ArrayList<ThermalInstance> clonedInstances = new ArrayList<ThermalInstance>();
        ArrayList<Net> clonedNets = new ArrayList<Net>();

        for (ThermalInstance inst : this.getInstances())
            clonedInstances.add((ThermalInstance) inst.clone());

        for (Net net : this.internalNets) {
            net.setHeaterInstances(getHeaterInstancesHashMap(clonedInstances));
            clonedNets.add((Net) net.clone());
        }

        clone.setInstances(clonedInstances);
        clone.setInternalNets(clonedNets);

        return clone;
    }

    private HashMap<String, ThermalInstance> getHeaterInstancesHashMap(ArrayList<ThermalInstance> clonedInstances) {
        HashMap<String, ThermalInstance> hashMap = new HashMap<String, ThermalInstance>();

        for (ThermalInstance inst : clonedInstances)
            hashMap.put(inst.getName(), inst);

        return hashMap;
    }

    public void setCLBSize(Integer CLBSize) {
        this.CLBSize = CLBSize;
    }

    public int getCLBSize() {
        return CLBSize;
    }

    public void setInstancesNum(Integer instancesNum) {
        this.instancesNum = instancesNum;
    }

    public int getInstancesNum() {
        return instancesNum;
    }

    public void setInstances(ArrayList<ThermalInstance> instances) {
        this.instances = instances;
    }

    public ArrayList<ThermalInstance> getInstances() {
        return instances;
    }

    public void connectWithCounter(ThermalDesign thermalDesign) {
        Instance counterInstance;
        try {
            counterInstance = thermalDesign.getInstance(String.format("InstThermometersLogic/termometr<%d>", id));
        } catch (Exception e) {
            System.out.println("You don't set the id of thermometer before or the counter instance doesn't exists.");
            return;
        }

        Net counter_net = internalNets.get(0);
        counter_net.addPin(new Pin(false, "CLK", counterInstance));
    }
}
