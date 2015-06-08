/*
 * Copyright (c) 2014 Wroclaw University of Technology
 *
 * This file is part of the pl.edu.pwr.iiar.tywin.tywin.pl.edu.pwr.iiar.zak.thermalKit.util.JGenerilo Thermal Emulation Tools.
 *
 * pl.edu.pwr.iiar.tywin.tywin.pl.edu.pwr.iiar.zak.thermalKit Thermal Emulation Tools are free software: you may redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * pl.edu.pwr.iiar.tywin.tywin.pl.edu.pwr.iiar.zak.thermalKit.util.JGenerilo is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License is available
 * at <http://www.gnu.org/licenses/>.
 *
 */
package pl.edu.pwr.iiar.zak.thermalKit.util;

import edu.byu.ece.rapidSmith.util.FileConverter;
import edu.byu.ece.rapidSmith.util.RunXilinxTools;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.jdom2.JDOMException;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.ThermalDesign;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class of pl.edu.pwr.iiar.tywin.tywin.pl.edu.pwr.iiar.zak.thermalKit.util.JGenerilo. It parses
 * the args from command line. It creates Netlists (NCD files) and Bitstreams
 * (BIT files) according to control pl.edu.pwr.iiar.tywin.tywin.pl.edu.pwr.iiar.zak.thermalKit.parser
 * files. It also provides functionality of creating the XML file with
 * description of resources which cen be used in emulation design as well as
 * prints the availability of resources
 *
 * @author Pawel Weber
 * @version 0.1
 *          Created on 11.03.14.
 */

public class JGenerilo {

    private static final Map TERMINAL_COMANDS = new HashMap() {{
        put("g", ";32m");
        put("r", ";31m");
        put("b", ";34m");
        put("y", ";33m");
        put("c", ";0m");
        put("normal", "\033[0");
        put("bold", "\033[1");
        put("n", ";38m");
    }};

    public static void main(String[] args) throws JDOMException, IOException, JGeneriloException, CloneNotSupportedException {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("JGenerilo")
                .defaultHelp(false)
                .description(MapFormat.format("{bold}{g}Generilo toolset which help to work with low-level " +
                        "edition of FPGA devices{normal}{n}", TERMINAL_COMANDS))
                .epilog(MapFormat.format("{bold}{g}PROJECT THERMAL 2013 - all rights reserved{normal}{n}",
                        TERMINAL_COMANDS))
                .usage(MapFormat.format("" +
                        "{normal}{y}generilo [[-g] XML_FILE or [-dp] XDL_FILE] (device) ([-o] OUTPUT_FILE)\n" +
                        "---------------------------------------------------------------------------\n" +
                        "{bold}{g}EXMAPLES:{normal}{y}\n" +
                        "generilo {bold}{y}-g{normal}{y} generilo.xml{bold}{y}(device){normal}{y}\n" +
                        "generilo {bold}{y}-d{normal}{y} simulationcore.ncd {bold}{y}(device){normal}{y} ({bold}{y}-o{normal}{y} floorplan.parser)\n" +
                        "generilo {bold}{y}-p{normal}{y} simulationcore.ncd {bold}{y}(device){normal}{y}\n" +
                        "---------------------------------------------------------------------------\n" +
                        "(device) is optional: default s3e1600{normal}{c}", TERMINAL_COMANDS));

        parser.addArgument("-g", "--generate-bitstream")
                .dest("xml_file")
                .action(Arguments.store())
                .help("generate the bitstream with user hotfloorplan specified in parser file.");

        parser.addArgument("-d", "--design2file")
                .dest("design_file")
                .action(Arguments.store())
                .help("generate parser file with FPGA description for FloorplanMaker");

        parser.addArgument("-p", "--print-design")
                .dest("print_file")
                .action(Arguments.store())
                .help("print the FPGA description to console");

        parser.addArgument("device")
                .action(Arguments.store())
                .choices("spartan3e", "spartan6", "virtex5")
                .setDefault("virtex5")
                .metavar(MapFormat.format("{bold}{g}device {normal}{n}[spartan3e, spartan6 or virtex5]" +
                        "{normal}{n}", TERMINAL_COMANDS));

        parser.addArgument("-o", "--output_file")
                .dest("output_file")
                .action(Arguments.store())
                .nargs(1)
                .help("Define output file for options -d (*.parser) and -g (*.ncd)");

        try {
            Namespace arguments = parser.parseArgs(args);

            if (arguments.get("design_file") != null) {
                ReadDesign readDesign = new ReadDesign().toFile();
                readDesign.showDesign((String) arguments.get("design_file"));
            }

            if (arguments.get("print_file") != null) {
                ReadDesign readDesign = new ReadDesign().print();
                readDesign.showDesign((String) arguments.get("print_file"));
            }

            if (arguments.get("xml_file") != null) {
                ThermalDesign thermalDesign = new ThermalDesign();
                thermalDesign.loadXMLFile((String) arguments.get("xml_file"));
                thermalDesign.placement();
                thermalDesign.saveXDLFile();
                String filename = thermalDesign.getXDLOutFile().substring(0, thermalDesign.getXDLOutFile().length() - 3);
                System.out.println("Start converting xdl to ncd file...");
                FileConverter.convertXDL2NCD(String.format("%sncd", filename));
                System.out.println("Start to routing the design...");
                String par_command = String.format("par -p -k -w %sncd %sncd", filename, filename);
                RunXilinxTools.runCommand(par_command, true);
                System.out.println("Start to create a bitstream...");
                String bit_command = String.format("bitgen -d -w %sncd", filename);
                RunXilinxTools.runCommand(bit_command, true);
                System.out.println("Everything is done!");
            }
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}