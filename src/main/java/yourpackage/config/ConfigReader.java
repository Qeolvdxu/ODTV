package yourpackage.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import yourpackage.gauges.Gauge;
import yourpackage.gauges.StaticGaugeArrayList;

public class ConfigReader {

    public static void readGaugesFromConfig(String filePath) {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);

            // Determine the number of gauges in the file
            int gaugeCount = (int) properties.entrySet().stream()
                    .map(entry -> entry.getKey().toString().split("\\.")[0])
                    .distinct()
                    .filter(name -> name.startsWith("Gauge"))
                    .count();

            for (int i = 0; i < gaugeCount; i++) {
                Gauge gauge = new Gauge();

                // Read and set the ranges
                gauge.setBlueRange(
                        Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".minBlueRange")),
                        Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".maxBlueRange"))
                );
                gauge.setGreenRange(
                        Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".minGreenRange")),
                        Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".maxGreenRange"))
                );
                gauge.setYellowRange(
                        Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".minYellowRange")),
                        Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".maxYellowRange"))
                );
                gauge.setRedRange(
                        Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".minRedRange")),
                        Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".maxRedRange"))
                );

                // Read and set other properties
                gauge.setUpdateFrequency(Double.parseDouble(properties.getProperty("Gauge" + (i + 1) + ".updateFrequency")));
                gauge.setGaugeTitle(properties.getProperty("Gauge" + (i + 1) + ".title", "Untitled Gauge"));

                // Add the newly created gauge directly to the StaticGaugeArrayList
                StaticGaugeArrayList.addGauge(gauge);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
