package yourpackage.app;

import com.formdev.flatlaf.FlatDarkLaf;
import yourpackage.gauges.*;
import yourpackage.parsing.DataField;
import yourpackage.parsing.DataFieldParser;
import yourpackage.parsing.NumericDataField;
import java.io.File;
import java.util.ArrayList;

public class Main {
    public static ArrayList<Gauge> gauges;

    public static void main (String[] args)
    { FlatDarkLaf.setup(); // Initialize Flatlaf, the look and feel we are using for Swing.
        Gauge test = new CircleGauge(270);
        Window window = new Window();
    }
}