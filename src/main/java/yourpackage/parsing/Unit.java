package yourpackage.parsing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

class Unit implements Serializable {
    String si;
    String name;
    List<String> subunits = new ArrayList<String>();
    Dictionary<String, Double> factors = new Hashtable<>();
    Dictionary<String, Double> constants = new Hashtable<>();

    public Unit(String name, String standard) {
        this.name = name;
        this.si = standard;
        subunits.add(this.si);
        factors.put(this.si, java.lang.Double.valueOf(1d));
        constants.put(this.si, java.lang.Double.valueOf(0d));
    }

    public String getName() {
        return this.name;
    }

    public List<String> getSubunits() {
        return this.subunits;
    }

    // Subunit setter for constant-coefficient conversions;
    // this_subunit = factor*base_unit+constant.
    void addSubunit(String name, Double factor) {
        subunits.add(name);
        factors.put(name, java.lang.Double.valueOf(factor));
        constants.put(name, java.lang.Double.valueOf(0d));
    }

    // Subunit setter with optional added constant
    // this_subunit = factor*base_unit+constant.
    void addSubunit(String name, Double factor, Double constant) {
        subunits.add(name);
        factors.put(name, java.lang.Double.valueOf(factor));
        constants.put(name, java.lang.Double.valueOf(constant));
    }

    // Use this Unit and its defined subunits to convert a
    public Double convert(String from, String to, Double value) {
        // If ( x = s*a+b; y = s*c+d ) such that s is the base unit and x,y are
        // linear subunits and a/c, b/d are the conversion factors and consants
        // respectively, x=((y-d)/c)*a+b
        Double a, b, c, d;


        if (subunits.contains(from) && subunits.contains(to)) {
            a = factors.get(to);
            b = constants.get(to);
            c = factors.get(from);
            d = constants.get(from);
            return ((value - d) / c) * a + b;
        } else {
            return null;
        }
    }
}