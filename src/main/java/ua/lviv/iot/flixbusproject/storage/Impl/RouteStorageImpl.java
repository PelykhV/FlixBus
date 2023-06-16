package ua.lviv.iot.flixbusproject.storage.Impl;

import ua.lviv.iot.flixbusproject.manager.FileManager;
import ua.lviv.iot.flixbusproject.model.Route;
import ua.lviv.iot.flixbusproject.model.Stop;
import ua.lviv.iot.flixbusproject.storage.RouteStorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteStorageImpl implements RouteStorage {
    private final FileManager manager = new FileManager();

    @Override
    public void writeToCSV(final Route route, final String pathToFiles) throws IOException {
        if (route == null) {
            return;
        }

        File directory = new File(pathToFiles).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Не вдалося створити директорію: " + directory.getAbsolutePath());
            return;
        }

        boolean fileExists = manager.fileExists(pathToFiles);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(pathToFiles, true),
                StandardCharsets.UTF_8))) {
            if (!fileExists) {
                writer.write(route.getHeaders());
            }
            writer.write(route.toCSV());
            writer.write("\n");
        }
    }

    @Override
    public void deleteFromCSV(final Integer id, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = manager.readLinesFromFile(file);
                for (String line : lines) {
                    Route entity = parseCSV(line);
                    if (entity.getId().equals(id)) {
                        lines.remove(line);
                        manager.writeLinesToFile(file, lines, entity);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void changeEntityByID(final Integer id, final Route entity, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = manager.readLinesFromFile(file);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Route route = parseCSV(line);
                    if (route.getId().equals(id)) {
                        entity.setId(id);
                        lines.set(i, entity.toCSV());
                        manager.writeLinesToFile(file, lines, entity);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Map<Integer, Route> readFromCSV(final File monthDirectory) {
        Map<Integer, Route> routeMap = new HashMap<>();

        if (monthDirectory.exists() && monthDirectory.isDirectory()) {
            File[] files = monthDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        List<String> lines = manager.readLinesFromFile(file);
                        for (int i = 1; i < lines.size(); i++) {
                            String line = lines.get(i);
                            Route route = parseCSV(line);
                            routeMap.put(route.getId(), route);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return routeMap;
    }

    private Route parseCSV(final String csvLine) {
        String[] values = csvLine.split(",");

        Integer id = Integer.valueOf(values[0]);
        String departureAddress = values[1];
        String destinationAddress = values[2];
        double ticketPrice = Double.parseDouble(values[3]);
        double totalDistance = Double.parseDouble(values[4]);

        List<Stop> stops = new ArrayList<>();

        return new Route(id, departureAddress, destinationAddress, stops, ticketPrice, totalDistance);
    }

    @Override
    public Integer getLastId(final Map<Integer, Route> routes) {
        return routes.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);
    }
}
