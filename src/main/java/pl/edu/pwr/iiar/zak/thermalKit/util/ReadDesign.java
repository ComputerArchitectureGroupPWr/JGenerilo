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

import edu.byu.ece.rapidSmith.design.Design;
import edu.byu.ece.rapidSmith.device.Device;
import edu.byu.ece.rapidSmith.device.PrimitiveSite;
import edu.byu.ece.rapidSmith.util.FileConverter;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Write module description
 *
 * @author pawel
 * @version ${VERSION}
 *          Created on 07.04.14.
 */
public class ReadDesign {
    private boolean print = true;

    private static Element addElement(int col, int row, String name) {
        Element element = new Element(name);
        element.setAttribute(new Attribute("x", Integer.toString(col)));
        element.setAttribute(new Attribute("y", Integer.toString(row)));
        element.setAttribute(new Attribute("width", "1"));
        element.setAttribute(new Attribute("height", "1"));

        return element;
    }

    public ReadDesign print() {
        print = true;
        return this;
    }

    public ReadDesign toFile() {
        print = false;
        return this;
    }

    String progress_bar = "\r[] ";
    StringBuilder data = new StringBuilder(progress_bar);

    public String progressBar(int x, int length) throws IOException {
        int mod = 100 / length;
        if (x % mod == 0) {
            data.replace(2 + (x / mod), 2 + (x / mod), "#");
            return data.toString() + String.format("%d", x) + "%";
        }

        return data.toString() + String.format("%d", x + 1) + "%";
    }

    public void showDesign(String filename) throws JDOMException, IOException {
        try {
            System.out.println("Converting NCD file to XDL...");
            FileConverter.convertNCD2XDL(filename);
        } catch (NullPointerException e) {
            System.out.println("Please indicate correct ncd file!");
            System.exit(0);
        }
        Design design = new Design();
        design.loadXDLFile(filename.substring(0, filename.length() - 3) + "xdl");
        Device device = design.getDevice();

        Document writer = new Document();
        Element rootElement = new Element("device");
        writer.addContent(rootElement);
        rootElement.setAttribute(new Attribute("version", "0.2"));
        rootElement.setAttribute(new Attribute("name", device.getPartName()));

        Element size = new Element("size");
        rootElement.addContent(size);
        size.setAttribute(new Attribute("cols", String.format("%d", device.getColumns())));
        size.setAttribute(new Attribute("rows", String.format("%d", device.getRows())));

        Element clbsize = new Element("clbsize");
        rootElement.addContent(clbsize);
        clbsize.setAttribute(new Attribute("width", "0.00025"));
        clbsize.setAttribute(new Attribute("height", "0.0002"));

        int old_z = -1;
        int k = 0;
        String progress_bar = "|/-\\";
        for (int row = 0; row < device.getColumns(); row++) {
            if (print) {
                System.out.format("%d\t", row + 1);
            }
            for (int col = 0; col < device.getRows(); col++) {
                System.out.println(String.format("Col: %d, Row: %d",col,row));
                System.out.println(design.getDevice().getTile(col,row).getName());
                if (design.getDevice().getTile(col, row).getName().contains("CLB")) {
                    try {
                        boolean used = false;
                        for (PrimitiveSite ps : design.getDevice().getTile(col, row).getPrimitiveSites()) {
                            if (design.isPrimitiveSiteUsed(ps)) {
                                used = true;
                            }
                        }
                        if (used) {
                            if (print) {
                                System.out.print("@");
                            }
                            rootElement.addContent(addElement(row, col, "unit"));
                        } else {
                            if (print) {
                                System.out.print("*");
                            }
                        }
                        k++;
                    } catch (Exception e) {
                        if (print) {
                            System.out.print("#");
                        }
                        rootElement.addContent(addElement(row, col, "obstacle"));
                    }
                } else {
                    rootElement.addContent(addElement(row, col, "obstacle"));
                    if (print) {
                        System.out.print("#");
                    }
                }
            }
            if (print) {
                System.out.println();
            } else {
                int z = (int) ((row / 177.0) * 100);
                if (old_z != z) {
                    System.out.write(progressBar(z, 50).getBytes());
                    old_z = z;
                }
            }
        }
        System.out.format("\nNumber of CLB tiles %d\n", k);
        System.out.format("Number of columns %d; Number of rows %d\n", design.getDevice().getColumns(),
                design.getDevice().getRows());
        System.out.println("Creating XML file with device floorplan...");
        XMLOutputter xml = new XMLOutputter();
        xml.setFormat(Format.getPrettyFormat());

        String path = "floorplan.xml";

        if (!print) {
            PrintWriter xmlWriter = new PrintWriter(path, "UTF-8");
            xmlWriter.print(xml.outputString(writer));
            xmlWriter.close();
            System.out.println("FPGA description saved in floorplan.xml!");
        }
    }

}
