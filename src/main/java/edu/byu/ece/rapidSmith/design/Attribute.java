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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * The Attribute object in XDL is used in several places, Design, Instance, Net
 * and Module.  Each are generally a list of attributes.  An attribute in XDL consists
 * of a triplet of Strings separated by colons: "Physical Name":"Logical Name":"Value".
 * This class captures these elements of an attribute.
 *
 * @author Chris Lavin
 *         Created on: Jun 22, 2010
 */
public class Attribute implements Serializable {

    private static final long serialVersionUID = -7266101885597264094L;

    /**
     * Physical name of the attribute (_::)
     */
    @JsonProperty("physicalName")
    private String physicalName;
    /**
     * Logical or user name of the attribute (:_:)
     */
    @JsonProperty("logicalName")
    private String logicalName;
    /**
     * Value of the attribute (::_)
     */
    @JsonProperty("value")
    private String value;

    /**
     * @param physicalName Physical name of the attribute (_::)
     * @param logicalName  Logical or user name of the attribute (:_:)
     * @param value        Value of the attribute (::_)
     */
    public Attribute(String physicalName, String logicalName, String value) {
        this.physicalName = physicalName;
        this.logicalName = logicalName;
        this.value = value;
    }

    /**
     * Creates a new attribute by copying the class members from attr.
     *
     * @param attr The attribute to model the new attribute after.
     */
    public Attribute(Attribute attr) {
        this.physicalName = attr.physicalName;
        this.logicalName = attr.logicalName;
        this.value = attr.value;
    }

    /**
     * Dummy constructor creates new attribute by copying th class
     * members from MongoDB
     */
    public Attribute() {

    }

    /**
     * Gets the physical name of the attribute (_::)
     *
     * @return Physical name of the attribute (_::)
     */
    public String getPhysicalName() {
        return physicalName;
    }

    /**
     * Sets the physical name of the attribute (_::)
     *
     * @param physicalName physical name of the attribute (_::)
     */
    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    /**
     * Gets the logical or user name of the attribute (:_:)
     *
     * @return Logical or user name of the attribute (:_:)
     */
    public String getLogicalName() {
        return logicalName;
    }

    /**
     * Sets the logical or user name of the attribute (:_:)
     *
     * @param logicalName Logical or user name of the attribute (:_:)
     */
    public void setLogicalName(String logicalName) {
        this.logicalName = logicalName;
    }

    /**
     * Gets the value of the attribute (::_)
     *
     * @return Value of the attribute (::_)
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the attribute (::_)
     *
     * @param value Value of the attribute (::_)
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Creates a string representation of the attribute that follows
     * how it would appear in and XDL file.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(physicalName);
        sb.append(":");
        sb.append(logicalName);
        sb.append(":");
        sb.append(value);
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Attribute)) return false;
        Attribute newAttribute = (Attribute) obj;

        boolean isEqual = true;

        if (!this.physicalName.equals(newAttribute.physicalName))
            isEqual = false;
        if (!this.logicalName.equals(newAttribute.logicalName))
            isEqual = false;
        if (!this.value.equals(newAttribute.value))
            isEqual = false;

        return isEqual;
    }
}
