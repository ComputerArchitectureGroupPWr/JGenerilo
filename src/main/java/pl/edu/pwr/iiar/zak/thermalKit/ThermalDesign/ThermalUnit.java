/*
 * Copyright (c) 2014 Wroclaw University of Technology
 *
 * This file is part of the pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo Thermal Emulation Tools.
 *
 * pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo Thermal Emulation Tools are free software: you may redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * pl.edu.pwr.iiar.tywin.tywin.jGenerilo.JGenerilo is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License is available
 * at <http://www.gnu.org/licenses/>.
 *
 */

package pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign;

import edu.byu.ece.rapidSmith.design.Instance;
import edu.byu.ece.rapidSmith.design.Net;
import edu.byu.ece.rapidSmith.design.NetType;
import edu.byu.ece.rapidSmith.design.Pin;
import edu.byu.ece.rapidSmith.device.PrimitiveType;
import edu.byu.ece.rapidSmith.device.Tile;

import java.util.ArrayList;

/**
 * This is the main abstract class which gives interface of implementation
 * for thermal units like heaters and thermometers.
 *
 * @author Pawel Weber
 * @version 0.1.0
 *          Created on 20.03.14.
 */
public abstract class ThermalUnit implements Cloneable {

    //========================================================================//
    // Class Members
    //========================================================================//
    /**
     * Name of thermal unit
     */
    protected String name;
    /**
     * Id of thermal unit
     */
    protected int id;
    /**
     * Column coordinate of thermal unit
     */
    protected int col;
    /**
     * Row coordinate of thermal unit
     */
    protected int row;
    /**
     * Thermal unit enable pins
     */
    protected ArrayList<Pin> enablePins;
    /**
     * ArrayList of internal nets
     */
    protected ArrayList<Net> internalNets;
    /**
     * Tile where unit is settled
     */
    protected Tile unitTile;
    /**
     * ArrayList of primitive sites required by ThermalUnit to place
     */
    protected ArrayList<PrimitiveType> requiredPrimitiveSites;
    /**
     * ArrayList of thermal instances
     */
    protected ArrayList<ThermalInstance> instances;

    /**
     * Constructor, initializes all objects to new
     */
    public ThermalUnit() {
        internalNets = new ArrayList<Net>();
        enablePins = new ArrayList<Pin>();
        requiredPrimitiveSites = new ArrayList<PrimitiveType>();
        instances = new ArrayList<ThermalInstance>();
        unitTile = null;
        name = null;
    }

    public ThermalUnit(String name, int id, int col, int row) {
        this.name = name;
        this.id = id;
        this.col = col;
        this.row = row;
        internalNets = new ArrayList<Net>();
        enablePins = new ArrayList<Pin>();
        requiredPrimitiveSites = new ArrayList<PrimitiveType>();
        instances = new ArrayList<ThermalInstance>();
        unitTile = null;
    }

    //========================================================================//
    // Getter and Setter Methods
    //========================================================================//

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public ArrayList<Pin> getEnablePins() {
        return enablePins;
    }

    public ArrayList<Net> getInternalNets() {
        return internalNets;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tile getUnitTile() {
        return unitTile;
    }

    public void setUnitTile(Tile unitTile) {
        this.unitTile = unitTile;
    }

    public void addInstances(ThermalInstance instances) {
        this.instances.add(instances);
    }

    public ArrayList<ThermalInstance> getInstances() {
        return instances;
    }

    public void setRequiredPrimitiveSite(PrimitiveType type) {
        requiredPrimitiveSites.add(type);
    }

    public ArrayList<PrimitiveType> getRequiredPrimitiveSites() {
        return requiredPrimitiveSites;
    }

    protected Net createNet(String name, NetType type, String pin_in, String pin_out, Instance instanceIn,
                            Instance instanceOut) {
        Net net = new Net();
        net.setName(name);
        net.setType(type);
        net.addPin(new Pin(true, pin_in, instanceIn));
        net.addPin(new Pin(false, pin_out, instanceOut));

        return net;
    }

    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        ThermalUnit cloned = (ThermalUnit) super.clone();
        cloned.setInstances((ArrayList<ThermalInstance>) this.getInstances().clone());
        return cloned;
    }

    public void setInternalNets(ArrayList<Net> internalNets) {
        this.internalNets = internalNets;
    }

    public void setInstances(ArrayList<ThermalInstance> instances) {
        this.instances = instances;
    }

}
