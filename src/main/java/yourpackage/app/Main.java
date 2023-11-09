package yourpackage.app;

import com.formdev.flatlaf.FlatDarkLaf;
import yourpackage.gauges.Gauge;

import java.util.ArrayList;

public class Main {
    public static ArrayList<Gauge> gauges;

    public static void main (String[] args)
    { FlatDarkLaf.setup(); // Initialize Flatlaf, the look and feel we are using for Swing.
        Window window = new Window(); }
}