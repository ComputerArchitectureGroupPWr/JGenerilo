package pl.edu.pwr.iiar.zak.thermalKit.exceptions;

import pl.edu.pwr.iiar.zak.thermalKit.util.MapFormat;

import java.util.HashMap;

/**
 * The JGeneriloException class provides informations about pl.edu.pwr.iiar.tywin.tywin.pl.edu.pwr.iiar.zak.thermalKit.exceptions in execution
 * of pl.edu.pwr.iiar.tywin.tywin.pl.edu.pwr.iiar.zak.thermalKit.util.JGenerilo software.
 *
 * @author Pawel Weber
 * @version 0.1
 *          Created on 13.03.14.
 */

public class JGeneriloException extends Exception {

    /**
     * Type of error to rise
     */
    JGeneriloExceptionType type;
    /**
     * Format values for error message
     */
    HashMap<String, Object> format;
    /**
     * Custom error message
     */
    String message;

    public JGeneriloException(JGeneriloExceptionType type) {
        this.type = type;
        this.format = null;
        this.message = null;
    }

    public JGeneriloException(JGeneriloExceptionType type, HashMap<String, Object> format) {
        this.type = type;
        this.format = format;
        this.message = null;
    }

    public JGeneriloException(String message) {
        this.type = JGeneriloExceptionType.CustomError;
        this.format = null;
        this.message = message;
    }

    public String getMessage() {
        switch (type) {
            case CustomError:
                return message;
            case HeaterTypeError:
                return "No such heater type!";
            case ThermometerTypeError:
                break;
            case HeaterPlacementError:
                return MapFormat.format(
                        "Chosen tile {tile_name} in col: {col}, row: {row}, has incompatible type for {heater_type} " +
                                "heater type.", format);
            case HeaterNotExists:
                return MapFormat.format(
                        "HeaterInstance named {instance_name} doesn't exists in the thermal design",
                        format);
            case ThermNotExists:
                return MapFormat.format("Thermometer named {therm_name} doesn't exists in the design",
                        format);
            case NetNotExists:
                return MapFormat.format("Net named {net_name} doesn't exists in the design",
                        format);
        }
        return "Not recognized error";
    }

}
