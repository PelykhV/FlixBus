package ua.lviv.iot.flixbusproject.storage.Impl;

import ua.lviv.iot.flixbusproject.manager.FileManager;
import ua.lviv.iot.flixbusproject.model.Stop;
import ua.lviv.iot.flixbusproject.storage.StopStorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StopStorageImpl implements StopStorage {
    private final FileManager manager = new FileManager();

    @Override
    public void writeToCSV(final Stop stop, final String pathToFiles) throws IOException {
        if (stop == null) {
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
                writer.write(stop.getHeaders());
            }
            writer.write(stop.toCSV());
            writer.write("\n");
        }
    }

    @Override
    public void deleteFromCSV(final Integer id, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = manager.readLinesFromFile(file);
                for (String line : lines) {
                    Stop entity = parseCSV(line);
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
    public void changeEntityByID(final Integer id, final Stop entity, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = manager.readLinesFromFile(file);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Stop stop = parseCSV(line);
                    if (stop.getId().equals(id)) {
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
    public Map<Integer, Stop> readFromCSV(final File monthDirectory) {
        Map<Integer, Stop> stopMap = new HashMap<>();

        if (monthDirectory.exists() && monthDirectory.isDirectory()) {
            File[] files = monthDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        List<String> lines = manager.readLinesFromFile(file);
                        for (int i = 1; i < lines.size(); i++) {
                            String line = lines.get(i);
                            Stop stop = parseCSV(line);
                            stopMap.put(stop.getId(), stop);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return stopMap;
    }

    private Stop parseCSV(final String csvLine) {
        String[] values = csvLine.split(",");
        Integer id = Integer.valueOf(values[0]);
        String address = values[1];
        String name = values[2];
        double fare = Double.parseDouble(values[3]);
        double distance = Double.parseDouble(values[4]);

        return new Stop(id, name, address, fare, distance);
    }


    @Override
    public Integer getLastId(final Map<Integer, Stop> stops) {
        return stops.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);
    }
}
