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

import org.jongo.Mapper;
import org.jongo.ObjectIdUpdater;
import org.jongo.marshall.Marshaller;
import org.jongo.marshall.Unmarshaller;
import org.jongo.query.QueryFactory;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 * @see optional
 * Created on 13.05.14.
 */
public class ThermalMapper implements Mapper {
    @Override
    public Marshaller getMarshaller() {
        return null;
    }

    @Override
    public Unmarshaller getUnmarshaller() {
        return null;
    }

    @Override
    public ObjectIdUpdater getObjectIdUpdater() {
        return null;
    }

    @Override
    public QueryFactory getQueryFactory() {
        return null;
    }
}
