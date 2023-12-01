package yourpackage.gauges;


import java.util.ArrayList;

public class StaticGaugeArrayList {
    private static ArrayList<Gauge> Gauges = new ArrayList<Gauge>();

    public static void addGauge(Gauge g) {
        Gauges.add(g);
    }

    public static int getSize() { return Gauges.size(); }

    public static void removeGauges()
    {
        for (Gauge currentGauge : Gauges) {
            currentGauge.frame.setVisible(false);
            currentGauge.frame.dispose();
            currentGauge.setInvisible();
        }
        Gauges.clear();
    }
}
