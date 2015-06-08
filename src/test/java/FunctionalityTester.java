import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import edu.byu.ece.rapidSmith.design.Attribute;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pawel on 12.03.14.
 */
public class FunctionalityTester {

    public static void main(String[] argv) throws Exception {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher("term123");
        matcher.find();
        System.out.println(matcher.group(0));
        Integer i = new Integer("001");
        System.out.println(i);
    }

}

class Heater {

    @JsonProperty("type")
    private String type;

    @JsonProperty("primitiveType")
    private String primitiveType;

    @JsonProperty("CLBsize")
    private int CLBsize;

    //@JsonProperty("instances")
    private ArrayList<Instance> instances;

    //@JsonProperty("nets")
    //private ArrayList<Net> nets;

    public Heater(String type, String primitiveType, int CLBsize, ArrayList<Instance> instances) {
        this.type = type;
        this.primitiveType = primitiveType;
        this.CLBsize = CLBsize;
        this.instances = instances;
    }

    public Heater() {

    }

    public String toString() {
        return String.format("<Type: %s, primitiveType: %s, instances: %s>", type, primitiveType, instances);
    }

}

class Instance {
    @JsonProperty("name")
    private String name;

    @JsonProperty("attributes")
    private ArrayList<Attribute> attributes;

    @JsonProperty("enablePins")
    private ArrayList<String> enablePins;

    public Instance(String name, ArrayList<Attribute> attributes, ArrayList<String> enablePins) {
        this.name = name;
        this.attributes = attributes;
        this.enablePins = enablePins;
    }

    public Instance() {

    }

    public String toString() {
        return String.format("<Name: %s, Attributes: %s, EnablePins: %s>",
                name, attributes.get(0), enablePins);
    }
}
