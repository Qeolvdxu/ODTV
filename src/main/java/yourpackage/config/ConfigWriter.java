package yourpackage.config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import yourpackage.gauges.Gauge;

public class ConfigWriter {

    public static void saveConfigToFile(String filePath, String gaugeName, Gauge gauge) {
        Properties properties = new Properties();

        // Save the ranges
        properties.setProperty(gaugeName + ".minBlueRange", String.valueOf(gauge.getminBlueRange()));
        properties.setProperty(gaugeName + ".maxBlueRange", String.valueOf(gauge.getmaxBlueRange()));
        properties.setProperty(gaugeName + ".minGreenRange", String.valueOf(gauge.getminGreenRange()));
        properties.setProperty(gaugeName + ".maxGreenRange", String.valueOf(gauge.getmaxGreenRange()));
        properties.setProperty(gaugeName + ".minYellowRange", String.valueOf(gauge.getminYellowRange()));
        properties.setProperty(gaugeName + ".maxYellowRange", String.valueOf(gauge.getmaxYellowRange()));
        properties.setProperty(gaugeName + ".minRedRange", String.valueOf(gauge.getminRedRange()));
        properties.setProperty(gaugeName + ".maxRedRange", String.valueOf(gauge.getmaxRedRange()));

        // Save other properties
        properties.setProperty(gaugeName + ".updateFrequency", String.valueOf(gauge.getDataFrequency()));

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            properties.store(fileOutputStream, "Gauge Configurations");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}