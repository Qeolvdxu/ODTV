package yourpackage.gauges;

import java.util.ArrayList;

public class StaticGaugeArrayList {
    private static ArrayList<Gauge> gauges = new ArrayList<>();

    public static void addGauge(Gauge g) {
        gauges.add(g);
    }

    public static int getSize() {
        return gauges.size();
    }

    public static ArrayList<Gauge> getGauges() {
        return new ArrayList<>(gauges); // Return a copy to avoid external modification
    }

    public static void setGauges(ArrayList<Gauge> newGauges) {
        gauges = new ArrayList<>(newGauges); // Set a new copy to avoid external modification
    }

    public static void removeGauges() {
        for (Gauge currentGauge : gauges) {
            currentGauge.frame.setVisible(false);
            currentGauge.frame.dispose();
            currentGauge.setInvisible();
        }
        gauges.clear();
    }
}