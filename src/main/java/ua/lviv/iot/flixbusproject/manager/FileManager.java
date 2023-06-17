package ua.lviv.iot.flixbusproject.manager;

import ua.lviv.iot.flixbusproject.model.Bus;
import ua.lviv.iot.flixbusproject.datagetter.DateGetter;
import ua.lviv.iot.flixbusproject.model.Record;
import ua.lviv.iot.flixbusproject.model.Route;
import ua.lviv.iot.flixbusproject.model.Stop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileManager {
    public static final String PATH_TO_BUSES = String.join(File.separator, "src", "main",
            "resources", "buses") + File.separator;
    public static final String PATH_TO_ROUTES = String.join(File.separator, "src", "main",
            "resources", "routes") + File.separator;
    public static final String PATH_TO_STOPS = String.join(File.separator, "src", "main",
            "resources", "stops") + File.separator;
    public boolean fileExists(final String fileName) {
        return Files.exists(Path.of(fileName));
    }

    public String getMonthDirectoryPath(final Object entity) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;

        return getYearDirectoryPath(entity) + month;
    }

    public File[] getFileFromCurrentMonth(final Object entity) {
        File monthDirectory = new File(getMonthDirectoryPath(entity));

        if (monthDirectory.exists() && monthDirectory.isDirectory()) {
            if (monthDirectory.listFiles() != null) {
                return monthDirectory.listFiles();
            }
        }
        return null;
    }
    public void writeLinesToFile(final File file, final List<String> lines, final Record entity) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file),
                StandardCharsets.UTF_8))) {
            writer.write(entity.getHeaders());
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    public List<String> readLinesFromFile(final File file) throws IOException {
        List<String> readLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file),
                StandardCharsets.UTF_8))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                readLines.add(line);
            }
        }
        return readLines;
    }
    public String getFilePath(final Object entity) {
        String monthDirectoryPath = getMonthDirectoryPath(entity) + File.separator;

        return String.format("%s%s-%s.csv", monthDirectoryPath, entity.getClass().getSimpleName(), DateGetter.getCurrentDate());
    }
    private String getYearDirectoryPath(final Object entity) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        if (entity instanceof Bus) {
            return FileManager.PATH_TO_BUSES + year + File.separator;
        }  else if (entity instanceof Route) {
            return FileManager.PATH_TO_ROUTES + year + File.separator;
        } else if (entity instanceof Stop) {
            return FileManager.PATH_TO_STOPS + year + File.separator;
        } else {
            return null;
        }
    }
}
