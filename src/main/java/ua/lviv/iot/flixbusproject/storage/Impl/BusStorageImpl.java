package ua.lviv.iot.flixbusproject.storage.Impl;


import ua.lviv.iot.flixbusproject.manager.FileManager;
import ua.lviv.iot.flixbusproject.model.Bus;
import ua.lviv.iot.flixbusproject.storage.BusStorage;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusStorageImpl implements BusStorage {
    private final FileManager manager = new FileManager();

    @Override
    public void writeToCSV(final Bus bus, final String pathToFiles) throws IOException {
        if (bus == null) {
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
                writer.write(bus.getHeaders());
            }
            writer.write(bus.toCSV());
            writer.write("\n");
        }
    }

    @Override
    public void deleteFromCSV(final Integer id, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = manager.readLinesFromFile(file);
                for (String line : lines) {
                    Bus entity = parseCSV(line);
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
    public void changeEntityByID(final Integer id, final Bus entity, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = manager.readLinesFromFile(file);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Bus car = parseCSV(line);
                    if (car.getId().equals(id)) {
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
    public Map<Integer, Bus> readFromCSV(final File monthDirectory) {
        Map<Integer, Bus> busMap = new HashMap<>();

        if (monthDirectory.exists() && monthDirectory.isDirectory()) {
            File[] files = monthDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file),
                            StandardCharsets.UTF_8))) {
                        String line;
                        reader.readLine();
                        while ((line = reader.readLine()) != null) {
                            Bus bus = parseCSV(line);
                            busMap.put(bus.getId(), bus);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return busMap;
    }

    private Bus parseCSV(final String csvLine) {
        String[] values = csvLine.split(",");

        Integer id = Integer.valueOf(values[0]);
        boolean isReserved = Boolean.parseBoolean(values[1]);
        int age = Integer.parseInt(values[2]);
        int capacity = Integer.parseInt(values[3]);
        int mileage = Integer.parseInt(values[4]);
        String manufacturer = values[5];

        return new Bus(id, isReserved, age, capacity, mileage, manufacturer);
    }


    @Override
    public Integer getLastId(final Map<Integer, Bus> buses) {
        return buses.values().stream()
                .mapToInt(Bus::getId)
                .max()
                .orElse(0);
    }
}

