/*
 * Copyright (c) 2010 Brigham Young University
 * 
 * This file is part of the BYU RapidSmith Tools.
 * 
 * BYU RapidSmith Tools is free software: you may redistribute it 
 * and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * BYU RapidSmith Tools is distributed in the hope that it will be 
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is included with the BYU 
 * RapidSmith Tools. It can be found at doc/gpl2.txt. You may also 
 * get a copy of the license at <http://www.gnu.org/licenses/>.
 * 
 */
package edu.byu.ece.rapidSmith.design;

import com.caucho.hessian.io.Deflation;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import edu.byu.ece.rapidSmith.device.*;
import edu.byu.ece.rapidSmith.device.helper.HashPool;
import edu.byu.ece.rapidSmith.util.FileTools;
import edu.byu.ece.rapidSmith.util.MessageGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents the modules as found in XDL.  They are used to describe
 * hard macros and RPMs and instances of each.
 *
 * @author Chris Lavin
 *         Created on: Jun 22, 2010
 */
public class Module implements Serializable {

    private static final long serialVersionUID = 7127893920489370872L;
    /**
     * This is the current module file version (saved in file to ensure proper compatibility)
     */
    public static final String moduleFileVersion = "0.5";
    /**
     * Unique name of this module
     */
    private String name;
    /**
     * All of the attributes in this module
     */
    private ArrayList<Attribute> attributes;
    /**
     * This is the anchor of the module
     */
    private Instance anchor;
    /**
     * Ports on the module
     */
    private ArrayList<Port> portList;
    /**
     * Instances which are part of the module
     */
    private HashMap<String, Instance> instanceMap;
    /**
     * Nets of the module
     */
    private HashMap<String, Net> netMap;
    /**
     * This provides meta data for scenarios where inputs map directly to outputs in hard macros
     */
    private HashMap<String, ArrayList<String>> externalPortMap = null;

    /**
     * Empty constructor, strings are null, everything else is initialized
     */
    public Module() {
        name = null;
        anchor = null;
        attributes = new ArrayList<Attribute>();
        portList = new ArrayList<Port>();
        instanceMap = new HashMap<String, Instance>();
        netMap = new HashMap<String, Net>();
    }

    /**
     * Creates and returns a new hard macro design with the appropriate
     * settings and adds this module to the module list.
     *
     * @return A complete hard macro design with this module as the hard macro.
     */
    public Design createDesignFromModule(String partName) {
        Design design = new Design();
        design.setPartName(partName);
        design.setName(Design.hardMacroDesignName);
        design.setIsHardMacro(true);
        design.addModule(this);
        return design;
    }


    /**
     * Sets the name of this module
     *
     * @param name New name for this module
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets and returns the current name of this module
     *
     * @return The current name of this module
     */
    public String getName() {
        return name;
    }

    /**
     * Gets and returns the current attributes of this module
     *
     * @return The current attributes of this module
     */
    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Adds the attribute with value to this module.
     *
     * @param physicalName Physical name of the attribute.
     * @param value        Value to set the new attribute to.
     */
    public void addAttribute(String physicalName, String logicalName, String value) {
        attributes.add(new Attribute(physicalName, logicalName, value));
    }

    /**
     * Add the attribute to this module.
     *
     * @param attribute The attribute to add.
     */
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    /**
     * Checks if the design attribute has an attribute with a physical
     * name called phyisicalName.
     *
     * @param physicalName The physical name of the attribute to check for.
     * @return True if this module contains an attribute with the
     * physical name physicalName, false otherwise.
     */
    public boolean hasAttribute(String physicalName) {
        for (Attribute attr : attributes) {
            if (attr.getPhysicalName().equals(physicalName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the list of attributes for this module.
     *
     * @param attributes The new list of attributes to associate with this
     *                   module.
     */
    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }


    /**
     * This gets and returns the instance anchor of the module.
     *
     * @return Instance which is the anchor for this module.
     */
    public Instance getAnchor() {
        return anchor;
    }

    /**
     * Gets and returns the instance in the module called name.
     *
     * @param name Name of the instance in the module to get.
     * @return The instance name or null if it does not exist.
     */
    public Instance getInstance(String name) {
        return instanceMap.get(name);
    }

    /**
     * Gets and returns all of the instances part of this module.
     *
     * @return The instances that are part of this module.
     */
    public Collection<Instance> getInstances() {
        return instanceMap.values();
    }

    /**
     * Gets and returns the net in the module called name.
     *
     * @param name Name of the net in the module to get.
     * @return The net name or null if it does not exist.
     */
    public Net getNet(String name) {
        return netMap.get(name);
    }

    /**
     * Gets and returns all the nets that are part of the module.
     *
     * @return The nets that are part of this module.
     */
    public Collection<Net> getNets() {
        return netMap.values();
    }

    /**
     * Sets the anchor instance for this module.
     *
     * @param anchor New anchor instance for this module.
     */
    public void setAnchor(Instance anchor) {
        this.anchor = anchor;
    }

    /**
     * Gets and returns the port list for this module.
     *
     * @return The port list for this module.
     */
    public ArrayList<Port> getPortList() {
        return portList;
    }

    /**
     * Gets and returns the port list for this module.
     *
     * @return The port list for this module.
     */
    public ArrayList<Port> getPorts() {
        return portList;
    }

    /**
     * Sets the port list for this module.
     *
     * @param portList The new port list to be set for this module.
     */
    public void setPortList(ArrayList<Port> portList) {
        this.portList = portList;
    }

    /**
     * Sets the port list for this module.
     *
     * @param portList The new port list to be set for this module.
     */
    public void setPorts(ArrayList<Port> portList) {
        this.portList = portList;
    }

    /**
     * Adds a port to this module.
     *
     * @param port The new port to add.
     */
    public void addPort(Port port) {
        this.portList.add(port);
    }

    /**
     * Adds a net to this module.
     *
     * @param net The net to add to the module.
     */
    public void addNet(Net net) {
        this.netMap.put(net.getName(), net);
    }

    /**
     * Adds an instance to this module.
     *
     * @param inst The instance to add to the module.
     */
    public void addInstance(Instance inst) {
        this.instanceMap.put(inst.getName(), inst);
    }

    /**
     * @param externalPortMap the externalPortMap to set
     */
    public void setExternalPortMap(HashMap<String, ArrayList<String>> externalPortMap) {
        this.externalPortMap = externalPortMap;
    }

    /**
     * @return the externalPortMap
     */
    public HashMap<String, ArrayList<String>> getExternalPortMap() {
        return externalPortMap;
    }

    /**
     * Sets the design in all the module's instances to null
     */
    public void disconnectDesign() {
        for (Instance i : this.getInstances()) {
            i.setDesign(null);
        }
    }

    /**
     * Saves a compact version of the hard macro so it can be
     * used later. This method is a replacement for standard
     * Java serialization and should be much more compact.
     *
     * @param fileName the name of the file you want to save
     * @param dev      the device the hard macro was created for
     */
    public void saveToCompactFile(String fileName, Device dev) {

        HashPool<String> stringPool = new HashPool<String>();
        HashPool<Instance> instancePool = new HashPool<Instance>();
        HashPool<Pin> pinPool = new HashPool<Pin>();

        ///////// Building hash pools for different objects /////////
        // Populate String pool, all unique strings in the module
        // Module
        stringPool.add(name);
        for (Attribute attr : getAttributes()) {
            stringPool.add(attr.getPhysicalName());
            stringPool.add(attr.getLogicalName());
            stringPool.add(attr.getValue());
        }
        for (Port p : portList) {
            // Populating Pin pool
            pinPool.add(p.getPin());

            stringPool.add(p.getName());
        }

        // Instances
        for (Instance inst : instanceMap.values()) {
            // Populating Instance pool
            instancePool.add(inst);

            stringPool.add(inst.getName());
            if (inst.getPrimitiveSiteName() != null) {
                stringPool.add(inst.getPrimitiveSiteName());
            }

            for (Attribute attr : inst.getAttributes()) {
                stringPool.add(attr.getPhysicalName());
                stringPool.add(attr.getLogicalName());
                stringPool.add(attr.getValue());
            }
        }

        instancePool.add(anchor);

        // Nets
        for (Net net : netMap.values()) {
            stringPool.add(net.getName());

            if (net.getAttributes() != null) {
                for (Attribute attr : net.getAttributes()) {
                    stringPool.add(attr.getPhysicalName());
                    stringPool.add(attr.getLogicalName());
                    stringPool.add(attr.getValue());
                }
            }

            for (Pin p : net.getPins()) {
                // Populating Pin pool
                pinPool.add(p);

                stringPool.add(p.getName());
            }

            if (net.getSource() != null) {
                pinPool.add(net.getSource());
            }
        }

        ///////// Saving Data to File /////////
        try {

            FileOutputStream fos = new FileOutputStream(fileName);
            Hessian2Output h2os = new Hessian2Output(fos);
            Deflation deflate = new Deflation();
            Hessian2Output hos = deflate.wrap(h2os);

            // Save version
            hos.writeString(moduleFileVersion);

            // Save which part they used so we can load it when they load the file
            hos.writeString(dev.getPartName());

            ////// Save String[] - from StringPool //////
            hos.writeInt(stringPool.getEnumerations().size());
            for (String string : stringPool.getEnumerations()) {
                hos.writeString(string);
            }

            ////// Save Instance[] - from InstancePool //////
            hos.writeInt(instancePool.getEnumerations().size());
            for (Instance instance : instancePool.getEnumerations()) {
                // String name;
                hos.writeInt(stringPool.getEnumerationValue(instance.getName()));

                // ArrayList<Attribute> attributes;
                if (instance.getAttributes() != null) {
                    hos.writeInt(instance.getAttributes().size());
                    for (Attribute attr : instance.getAttributes()) {
                        hos.writeInt(stringPool.getEnumerationValue(attr.getPhysicalName()));
                        hos.writeInt(stringPool.getEnumerationValue(attr.getLogicalName()));
                        hos.writeInt(stringPool.getEnumerationValue(attr.getValue()));
                    }
                } else {
                    hos.writeInt(0);
                }

                // Boolean bonded;
                // InstanceType type;
                int mask;
                if (instance.getBonded() == null) {
                    mask = 0xC0000000;
                } else {
                    mask = instance.getBonded() ? 0x80000000 : 0x0;
                }

                hos.writeInt(mask | instance.getType().ordinal());

                //PrimitiveSite site;
                if (instance.getPrimitiveSiteName() == null) {
                    hos.writeInt(-1);
                } else {
                    hos.writeInt(stringPool.getEnumerationValue(instance.getPrimitiveSiteName()));
                }


                // Module moduleTemplate; -> reference back to this
            }

            ////// Save Pin[] - from PinPool //////
            hos.writeInt(pinPool.getEnumerations().size());
            for (Pin pin : pinPool.getEnumerations()) {
                // String name;
                hos.writeInt(stringPool.getEnumerationValue(pin.getName()));

                // boolean isOutputPin;
                // Instance instance;
                int mask = pin.isOutPin() ? 0x80000000 : 0x0;
                hos.writeInt(mask | instancePool.getEnumerationValue(pin.getInstance()));
            }

            ////// Save Module object //////
            // String name;
            hos.writeInt(stringPool.getEnumerationValue(name));

            // ArrayList<Attribute> attributes;
            hos.writeInt(attributes.size());
            for (Attribute attr : attributes) {
                hos.writeInt(stringPool.getEnumerationValue(attr.getPhysicalName()));
                hos.writeInt(stringPool.getEnumerationValue(attr.getLogicalName()));
                hos.writeInt(stringPool.getEnumerationValue(attr.getValue()));
            }


            // Instance anchor;
            int blah = instancePool.getEnumerationValue(anchor);
            hos.writeInt(blah);

            // ArrayList<Port> portList;
            hos.writeInt(portList.size());
            for (Port port : portList) {
                // String name;
                hos.writeInt(stringPool.getEnumerationValue(port.getName()));

                //String instanceName
                // Instance instance;
                hos.writeInt(instancePool.getEnumerationValue(port.getInstance()));

                // String pinName;
                // Pin pin;
                hos.writeInt(pinPool.getEnumerationValue(port.getPin()));
            }

            // HashMap<String,Instance> instanceMap;
            // create from instancePool

            // HashMap<String,Net> netMap;
            Net[] netArray = new Net[netMap.size()];
            netMap.values().toArray(netArray);

            hos.writeInt(netArray.length);
            for (Net net : netArray) {
                // String name;
                hos.writeInt(stringPool.getEnumerationValue(net.getName()));
                // NetType type;
                hos.writeInt(net.getType().ordinal());

                // ArrayList<Attribute> attributes;
                if (net.getAttributes() != null) {
                    hos.writeInt(net.getAttributes().size());
                    for (Attribute attr : net.getAttributes()) {
                        hos.writeInt(stringPool.getEnumerationValue(attr.getPhysicalName()));
                        hos.writeInt(stringPool.getEnumerationValue(attr.getLogicalName()));
                        hos.writeInt(stringPool.getEnumerationValue(attr.getValue()));
                    }
                } else {
                    hos.writeInt(0);
                }

                // ArrayList<Pin> pins;
                hos.writeInt(net.getPins().size());
                for (Pin p : net.getPins()) {
                    hos.writeInt(pinPool.getEnumerationValue(p));
                }

                // ArrayList<PIP> pips;
                hos.writeInt(net.getPIPs().size());
                for (PIP p : net.getPIPs()) {
                    // Tile tile;
                    hos.writeInt(p.getTile().getUniqueAddress());
                    // int startWire;
                    hos.writeInt(p.getStartWire());
                    // int endWire;
                    hos.writeInt(p.getEndWire());
                }

                // Pin source;
                if (net.getSource() == null) {
                    hos.writeInt(-1);
                } else {
                    hos.writeInt(pinPool.getEnumerationValue(net.getSource()));
                }

                //Module moduleTemplate;
                //pointer back to this
            }

            if (externalPortMap != null) {
                FileTools.writeStringMultiMap(hos, externalPortMap);
            }

            hos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkVersion(String fileName) throws IOException {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        Hessian2Input his = null;
        try {
            fis = new FileInputStream(fileName);

            bis = new BufferedInputStream(fis);
            his = new Hessian2Input(bis);
            Deflation dis = new Deflation();
            his = dis.unwrap(his);

            String version = his.readString();

            if (version.equals(moduleFileVersion)) {
                return true;
            }

            return false;


        } catch (FileNotFoundException e) {
            return false;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }


    /**
     * Loads the module from memory and is stored in this object.
     *
     * @param fileName The name of the file that is to be loaded.
     *                 Should coordinate with the file name used with the saveToCompactFile() method.
     */
    public void readFromCompactFile(String fileName) {
        Hessian2Input his = FileTools.getInputStream(fileName);

        String check;
        try {
            // Check to make sure the file was compressed the same way as we are doing now
            // That way there will be no compatibility problems
            check = his.readString();
            if (!check.equals(moduleFileVersion)) {
                MessageGenerator.briefErrorAndExit("ERROR: Current RAPIDSMITH module file version: " +
                        moduleFileVersion + ", existing module file version: " + check + ".  Please" +
                        "clean and regenerate the module cache.");
            }

            // Read in the device name so we can load it
            Device dev = FileTools.loadDevice(his.readString());

            // reading stringPool
            String[] strings = new String[his.readInt()];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = his.readString();
            }

            // reading instancePool
            Instance[] instances = new Instance[his.readInt()];
            for (int i = 0; i < instances.length; i++) {
                Instance instance = new Instance();
                instance.setName(strings[his.readInt()]);

                int attributeSize = his.readInt();
                if (attributeSize != 0) {
                    ArrayList<Attribute> attributes = new ArrayList<Attribute>(attributeSize);
                    for (int j = 0; j < attributeSize; j++) {
                        String physicalName = strings[his.readInt()];
                        String logicalName = strings[his.readInt()];
                        String value = strings[his.readInt()];

                        attributes.add(new Attribute(physicalName, logicalName, value));

                    }
                    instance.setAttributes(attributes);
                }

                int type = his.readInt();
                if ((type & 0xC0000000) != 0xC0000000) {
                    instance.setBonded((type & 0x80000000) == 0x80000000);
                }

                PrimitiveType[] instanceType = PrimitiveType.values();
                instance.setType(instanceType[type & 0x3FFFFFFF]);

                int primSite = his.readInt();
                if (primSite != -1) {
                    instance.place(dev.getPrimitiveSite(strings[primSite]));
                }

                instance.setModuleTemplate(this);

                instances[i] = instance;

                // constructing instanceMap
                instanceMap.put(instance.getName(), instance);
            }

            // reading pinPool
            Pin[] pins = new Pin[his.readInt()];
            for (int i = 0; i < pins.length; i++) {
                String name = strings[his.readInt()];
                int temp = his.readInt();
                boolean isOutPin = (temp & 0x80000000) == 0x80000000;
                Instance instance = instances[temp & 0x7FFFFFFF];

                pins[i] = new Pin(isOutPin, name, instance);
            }

            ////// Loading Module //////
            this.setName(strings[his.readInt()]);

            int attributeSize = his.readInt();
            ArrayList<Attribute> attributes = new ArrayList<Attribute>(attributeSize);
            for (int j = 0; j < attributeSize; j++) {
                String physicalName = strings[his.readInt()];
                String logicalName = strings[his.readInt()];
                String value = strings[his.readInt()];

                attributes.add(new Attribute(physicalName, logicalName, value));

            }
            this.setAttributes(attributes);

            this.setAnchor(instances[his.readInt()]);

            int portSize = his.readInt();
            ArrayList<Port> portList = new ArrayList<Port>(portSize);
            for (int i = 0; i < portSize; i++) {
                Port port = new Port();

                port.setName(strings[his.readInt()]);

                port.setInstance(instances[his.readInt()]);
                port.setInstanceName(port.getInstance().getName());

                port.setPin(pins[his.readInt()]);
                port.setPinName(port.getPin().getName());

                portList.add(port);
            }
            this.setPortList(portList);

            NetType[] type = NetType.values();
            int netSize = his.readInt();
            for (int i = 0; i < netSize; i++) {
                Net net = new Net();
                net.setName(strings[his.readInt()]);

                net.setType(type[his.readInt()]);

                attributeSize = his.readInt();
                if (attributeSize != 0) {
                    attributes = new ArrayList<Attribute>(attributeSize);
                    for (int j = 0; j < attributeSize; j++) {
                        String physicalName = strings[his.readInt()];
                        String logicalName = strings[his.readInt()];
                        String value = strings[his.readInt()];

                        attributes.add(new Attribute(physicalName, logicalName, value));
                    }
                    net.setAttributes(attributes);
                }

                int pinSize = his.readInt();
                ArrayList<Pin> pinList = new ArrayList<Pin>(pinSize);
                for (int j = 0; j < pinSize; j++) {
                    pinList.add(pins[his.readInt()]);
                }
                net.setPins(pinList);

                int pipSize = his.readInt();
                ArrayList<PIP> pipList = new ArrayList<PIP>(pipSize);
                for (int j = 0; j < pipSize; j++) {
                    Tile tile = dev.getTile(his.readInt());
                    int startWire = his.readInt();
                    int endWire = his.readInt();

                    pipList.add(new PIP(tile, startWire, endWire));
                }
                net.setPIPs(pipList);

                int temp = his.readInt();
                if (temp != -1) {
                    net.replaceSource(pins[temp]);
                }

                net.setModuleTemplate(this);

                // constructing HashMap
                netMap.put(net.getName(), net);
            }

            if (!his.isEnd()) {
                setExternalPortMap(FileTools.readStringMultiMap(his));
            }

            his.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Does a brute force search to find all valid locations of where this module
     * can be placed.
     *
     * @return A list of valid anchor sites for the module to be placed.
     */
    public ArrayList<PrimitiveSite> getAllValidPlacements(Device dev) {
        ArrayList<PrimitiveSite> validSites = new ArrayList<PrimitiveSite>();

        PrimitiveSite[] sites = dev.getAllCompatibleSites(getAnchor().getType());
        for (PrimitiveSite newAnchorSite : sites) {
            if (isValidPlacement(newAnchorSite, dev)) {
                validSites.add(newAnchorSite);
            }
        }

        return validSites;
    }

    public boolean isValidPlacement(PrimitiveSite proposedAnchorSite, Device dev) {
        // Check if parameters are null
        if (proposedAnchorSite == null || dev == null) {
            return false;
        }

        // Do some error checking on the newAnchorSite
        PrimitiveSite p = anchor.getPrimitiveSite();
        Tile t = proposedAnchorSite.getTile();
        PrimitiveSite newValidSite = Device.getCorrespondingPrimitiveSite(p, anchor.getType(), t);
        if (!proposedAnchorSite.equals(newValidSite)) {
            return false;
        }

        //=======================================================//
        /* Check instances at proposed location                  */
        //=======================================================//
        for (Instance inst : getInstances()) {
            PrimitiveSite templateSite = inst.getPrimitiveSite();
            Tile newTile = getCorrespondingTile(templateSite.getTile(), proposedAnchorSite.getTile(), dev);
            if (newTile == null) {
                return false;
            }
            if (Device.getCorrespondingPrimitiveSite(templateSite, inst.getType(), newTile) == null) {
                return false;
            }
        }

        //=======================================================//
		/* Check nets at proposed location                       */
        //=======================================================//
        for (Net net : getNets()) {
            for (PIP pip : net.getPIPs()) {
                if (getCorrespondingTile(pip.getTile(), proposedAnchorSite.getTile(), dev) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method will calculate and return the corresponding tile of a module
     * for a new anchor location.
     *
     * @param templateTile  The tile in the module which acts as a template.
     * @param newAnchorTile This is the tile of the new anchor instance of the module.
     * @param dev           The device which corresponds to this module.
     * @return The new tile of the module instance which corresponds to the templateTile, or null
     * if none exists.
     */
    public Tile getCorrespondingTile(Tile templateTile, Tile newAnchorTile, Device dev) {
        int tileXOffset = templateTile.getTileXCoordinate() - anchor.getTile().getTileXCoordinate();
        int tileYOffset = templateTile.getTileYCoordinate() - anchor.getTile().getTileYCoordinate();
        int newTileX = newAnchorTile.getTileXCoordinate() + tileXOffset;
        int newTileY = newAnchorTile.getTileYCoordinate() + tileYOffset;
        String oldName = templateTile.getName();
        String newName = oldName.substring(0, oldName.lastIndexOf('X') + 1) + newTileX + "Y" + newTileY;
        Tile correspondingTile = dev.getTile(newName);
        if (correspondingTile == null) {
            if (templateTile.getType().equals(TileType.CLBLL)) {
                correspondingTile = dev.getTile("CLBLM_X" + newTileX + "Y" + newTileY);
            } else if (templateTile.getType().equals(TileType.CLBLM)) {
                correspondingTile = dev.getTile("CLBLL_X" + newTileX + "Y" + newTileY);
            }
        }
        return correspondingTile;
    }

    /**
     * Generates the hashCode strictly on the module name.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * Checks if two modules are equal based on the name of the module.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Module other = (Module) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public String toString() {
        return name;
    }
}
