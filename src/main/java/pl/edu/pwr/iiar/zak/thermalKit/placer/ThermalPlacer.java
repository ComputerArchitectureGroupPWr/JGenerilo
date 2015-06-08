package pl.edu.pwr.iiar.zak.thermalKit.placer;

import edu.byu.ece.rapidSmith.design.*;
import edu.byu.ece.rapidSmith.device.PrimitiveSite;
import edu.byu.ece.rapidSmith.device.PrimitiveType;
import edu.byu.ece.rapidSmith.device.Tile;
import pl.edu.pwr.iiar.zak.thermalKit.ThermalDesign.*;
import pl.edu.pwr.iiar.zak.thermalKit.exceptions.JGeneriloException;

/**
 * Created by pawel on 10.05.14.
 */
public class ThermalPlacer {

    private ThermalDesign design;

    public ThermalPlacer() {
        design = null;
    }

    public ThermalPlacer(ThermalDesign design) {
        this.design = design;
    }

    public void placeHeaters() throws JGeneriloException {
        System.out.println("Place Heaters");
        connectHeaterEnableNets();
        connectFFtoCLK();
        connectIBtoCLK();
        for (HeaterUnit heaterUnit : design.getHeaterUnits()) {
            int failed_counter = 0;
            Tile heaterTile = design.getDevice().getTile(heaterUnit.getRow(), heaterUnit.getCol());

            String logic0instance_name = String.format("logic0x%dy%d",heaterUnit.getRow(), heaterUnit.getCol());
            String logic0net_name = String.format("logic0netx%dy%d",heaterUnit.getRow(), heaterUnit.getCol());
            String logic1net_name = String.format("logic1netx%dy%d",heaterUnit.getRow(), heaterUnit.getCol());

            Instance logic0instance = new Instance(logic0instance_name, PrimitiveType.TIEOFF);
            Net logic0net = new Net(logic0net_name, NetType.GND);
            Net logic1net = new Net(logic1net_name, NetType.VCC);

            if (heaterUnit.getType().equals("SRL16")) {

                logic0instance.addAttribute(new Attribute("_NO_USER_LOGIC","",""));
                logic0instance.addAttribute(new Attribute("_GND_SOURCE","","HARD0"));

                logic0instance.place(design.getDevice().getTile(heaterTile.getRow(),heaterTile.getColumn()-1).getPrimitiveSites()[0]);

                logic0net.addPin(new Pin(true,"HARD0",logic0instance));
                logic1net.addPin(new Pin(true,"HARD1",logic0instance));
            }
            for (ThermalInstance heaterInstance : heaterUnit.getInstances()) {
                for (PrimitiveSite primitiveSite : heaterTile.getPrimitiveSites()) {
                    if (primitiveSite.isCompatiblePrimitiveType(heaterInstance.getType())) {
                        if (!design.isPrimitiveSiteUsed(primitiveSite)) {
                            heaterInstance.place(primitiveSite);
                            heaterInstance.setName(String.format("%s_%s",
                                    heaterUnit.getName(), heaterInstance.getPrimitiveSiteName()));
                            design.addInstance(heaterInstance);
                            break;
                        }
                    } else {
                        failed_counter++;
                    }
                }
            }
            if (failed_counter==2) {
                System.out.format("Heater type %s cannot be placed at %d %d. Incorrect SLICE type!\n",
                        heaterUnit.getType(), heaterUnit.getCol(), heaterUnit.getRow());
            } else {
                addInternalNets(heaterUnit);
                System.out.format("Heater type %s is placed at %d %d\n",
                    heaterUnit.getType(), heaterUnit.getCol(), heaterUnit.getRow());

                if (heaterUnit.getType().equals("SRL16")) {
                    logic0net.addPin(new Pin(false,"A1",heaterUnit.getInstances().get(0)));
                    logic1net.addPin(new Pin(false,"A2",heaterUnit.getInstances().get(0)));
                    logic0net.addPin(new Pin(false,"A3",heaterUnit.getInstances().get(0)));
                    logic1net.addPin(new Pin(false,"A4",heaterUnit.getInstances().get(0)));
                    logic0net.addPin(new Pin(false,"B1",heaterUnit.getInstances().get(0)));
                    logic1net.addPin(new Pin(false,"B2",heaterUnit.getInstances().get(0)));
                    logic0net.addPin(new Pin(false,"B3",heaterUnit.getInstances().get(0)));
                    logic1net.addPin(new Pin(false,"B4",heaterUnit.getInstances().get(0)));
                    logic0net.addPin(new Pin(false,"C1",heaterUnit.getInstances().get(0)));
                    logic1net.addPin(new Pin(false,"C2",heaterUnit.getInstances().get(0)));
                    logic0net.addPin(new Pin(false,"C3",heaterUnit.getInstances().get(0)));
                    logic1net.addPin(new Pin(false,"C4",heaterUnit.getInstances().get(0)));
                    logic0net.addPin(new Pin(false,"D1",heaterUnit.getInstances().get(0)));
                    logic1net.addPin(new Pin(false,"D2",heaterUnit.getInstances().get(0)));
                    logic0net.addPin(new Pin(false,"D3",heaterUnit.getInstances().get(0)));
                    logic1net.addPin(new Pin(false,"D4",heaterUnit.getInstances().get(0)));
                    design.addInstance(logic0instance);
                    design.addNet(logic0net);
                    design.addNet(logic1net);
                }
            }
        }
    }

    public void placeThermometers() throws JGeneriloException {
        System.out.println("Place Thermometers");

        for (ThermometerUnit thermometerUnit : design.getThermometerUnits()) {
            Tile thermometerTile = design.getDevice().getTile(thermometerUnit.getRow(), thermometerUnit.getCol());
            for (ThermalInstance thermometerInstance : thermometerUnit.getInstances()) {
                for (PrimitiveSite primitiveSite : thermometerTile.getPrimitiveSites()) {
                    if (primitiveSite.isCompatiblePrimitiveType(primitiveSite)) {
                        if (!design.isPrimitiveSiteUsed(primitiveSite)) {
                            thermometerInstance.place(primitiveSite);
                            thermometerInstance.setName(String.format("%s_%s",
                                    thermometerUnit.getName(), thermometerInstance.getPrimitiveSiteName()));
                            design.addInstance(thermometerInstance);
                            break;
                        }
                    }
                }
            }
            addInternalNets(thermometerUnit);
            System.out.format("Thermometer type %s is placed at %d %d\n",
                    thermometerUnit.getType(), thermometerUnit.getCol(), thermometerUnit.getRow());
        }

        connectThermometerEnableNets();
        connectThermometerWithCounter();
    }

    public void addInternalNets(ThermalUnit thermalUnit) {
        for (Net net : thermalUnit.getInternalNets()) {
            net.setName(String.format("%s_%s", net.getName(), thermalUnit.getName()));
            design.addNet(net);
        }
    }

    public void connectHeaterEnableNets() throws JGeneriloException {
        String heaterEnableNetName = "InstHeatersLogic/Heaters_instances[%d].InstHeater/heaterEnable";

        for (HeaterUnit heaterUnit : design.getHeaterUnits()) {
            Net enableNet = design.getNet(String.format(heaterEnableNetName, heaterUnit.getId()));
            for (ThermalInstance inst : heaterUnit.getInstances()) {
                for (Pin enablePin : inst.getEnablePins()) {
                    try {
                        enableNet.addPin(enablePin);
                    } catch (NullPointerException e) {
                        System.out.format("[ERROR] No such net %s in the design, check your settings!\n",
                                System.out.format(heaterEnableNetName, heaterUnit.getId()));
                    }
                }
            }
        }
    }

    public void connectThermometerEnableNets() throws JGeneriloException {
        String thermometerEnableNet = "InstThermometersLogic/termometrEnable<%d>";

        for (ThermometerUnit thermometerUnit : design.getThermometerUnits()) {
            //thermometerUnit.connectWithCounter(design);
            Net enableNet = design.getNet(String.format(thermometerEnableNet, thermometerUnit.getId() + 2));
            for (ThermalInstance inst : thermometerUnit.getInstances()) {
                for (Pin enablePin : inst.getEnablePins()) {
                    try {
                        enableNet.addPin(enablePin);
                    } catch (NullPointerException e) {
                        System.out.format("[ERROR] No such net %d in the design, check your settings!",
                                thermometerEnableNet);
                    }
                }
            }
        }
    }

    public void connectThermometerWithCounter() {
        String counterInstanceName = "InstThermometersLogic/termometr<%d>";

        for (ThermometerUnit thermometerUnit : design.getThermometerUnits()) {
            Instance counterInstance = design.getInstance(String.format(counterInstanceName, thermometerUnit.getId() + 2));
            thermometerUnit.getInternalNets().get(7).addPin(new Pin(false, "CLK", counterInstance));
        }
    }

    public void connectFFtoCLK() throws JGeneriloException {
        String clockNetName = design.getGeneratorNet();
        Net clkNet = design.getNet(clockNetName);

        for (HeaterUnit heaterUnit : design.getHeaterUnits()) {
            if (heaterUnit.getType().equals("FF") || heaterUnit.getType().equals("FFPL") || heaterUnit.getType().equals("SRL16")) {
                for (ThermalInstance instance : heaterUnit.getInstances()) {
                    clkNet.addPin(new Pin(false, "CLK", instance));
                }
            } else
                continue;
        }
    }

    public void connectIBtoCLK() throws JGeneriloException {
        String clockNetName = design.getGeneratorNet();
        Net clkNet = design.getNet(clockNetName);

        for (HeaterUnit heaterUnit : design.getHeaterUnits()) {
            if (heaterUnit.getType().equals("IB")) {
                for (ThermalInstance instance : heaterUnit.getInstances()) {
                    clkNet.addPin(new Pin(false, "A2", instance));
                    clkNet.addPin(new Pin(false, "A3", instance));
                    clkNet.addPin(new Pin(false, "A4", instance));
                    clkNet.addPin(new Pin(false, "A5", instance));
                    clkNet.addPin(new Pin(false, "A6", instance));
                    clkNet.addPin(new Pin(false, "B2", instance));
                    clkNet.addPin(new Pin(false, "B3", instance));
                    clkNet.addPin(new Pin(false, "B4", instance));
                    clkNet.addPin(new Pin(false, "B5", instance));
                    clkNet.addPin(new Pin(false, "B6", instance));
                    clkNet.addPin(new Pin(false, "C2", instance));
                    clkNet.addPin(new Pin(false, "C3", instance));
                    clkNet.addPin(new Pin(false, "C4", instance));
                    clkNet.addPin(new Pin(false, "C5", instance));
                    clkNet.addPin(new Pin(false, "C6", instance));
                    clkNet.addPin(new Pin(false, "D2", instance));
                    clkNet.addPin(new Pin(false, "D3", instance));
                    clkNet.addPin(new Pin(false, "D4", instance));
                    clkNet.addPin(new Pin(false, "D5", instance));
                    clkNet.addPin(new Pin(false, "D6", instance));
                }
            } else
                continue;
        }
    }


}
