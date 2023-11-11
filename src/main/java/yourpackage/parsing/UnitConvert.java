package yourpackage.parsing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Dictionary;
import java.util.Hashtable;

public class UnitConvert implements java.io.Serializable {
    private List<String> unit_names = new ArrayList<>();
    private List<Unit> unit_array = new ArrayList<>();
    private Dictionary<String, List<String>> subunits = new Hashtable<>();

    public UnitConvert() {
        String tmpName;
        this.unit_array.add(new Unit("speed", "m/s"));
        unit_array.get(0).addSubunit("ft/s", 3.28084d);
        unit_array.get(0).addSubunit("mi/h", 3600f/1609.344d);
        unit_array.get(0).addSubunit("km/hr", 3.6d);
        this.unit_array.add(new Unit("power", "mAh"));
        unit_array.get(1).addSubunit("Coulombs", 3.6d);
        unit_array.get(1).addSubunit("Quintillion electrons", 22.4694351d);
        unit_array.get(1).addSubunit("Amp hrs", .001d);
        this.unit_array.add(new Unit("distance", "m"));
        unit_array.get(2).addSubunit("cm", 100d);
        unit_array.get(2).addSubunit("ft", 3.28084d);
        unit_array.get(2).addSubunit("mi", .0006213712d);
        unit_array.get(2).addSubunit("km", .0001d);
        this.unit_array.add(new Unit("temperature", "C"));
        unit_array.get(3).addSubunit("Kelvin", 1d, 273.15d);
        unit_array.get(3).addSubunit("Farenheit", 1.8d, 32d);
        this.unit_array.add(new Unit("coordinates", "lat"));
        unit_array.get(4).addSubunit("lon", 1d);

        for (Unit u : unit_array) {
            tmpName = u.getName();
            unit_names.add(tmpName);
            subunits.put(tmpName, u.getSubunits());
            System.out.print("Added unit '"+tmpName);
            System.out.print("' with subunits: ");
            System.out.println(u.getSubunits());
        }
    }


    // Returns the subunits corresponding to a unit name
    // ie the unit name "coordinates" would will return a List <"lat", "lon">
    // Returns null if the provided unit_name is not supported.
    public List<String> getSubunits(String unit_name) {
        if (this.unit_names.contains(unit_name))
            return this.subunits.get(unit_name);
        else
            return null;
    }

    public List<String> getUnitNames() { return this.unit_names; }

    // Attempts to convert input array with a unit of type 'type' between
    // the provided to/from subtypes. Returns null if unsuccessful.
    public List<Double> convert(String type, String to, String from, List<Double> input) {
        Unit tmp_unit = null;
        List<String> tmp_subunits;
        List<Double> converted_array = new ArrayList<>();
        for (Unit u : unit_array) {
            if (u.getName()==type)
                tmp_unit = u;
        }

        if (tmp_unit==null)
            return null;

        tmp_subunits = tmp_unit.getSubunits();

        if (!tmp_subunits.contains(to) || !tmp_subunits.contains(from))
            return null;

        for (Double d : input) {
            if (d!=null)
                converted_array.add(tmp_unit.convert(from, to, d));
            else
                converted_array.add(null);
        }
        return converted_array;
    }
}
