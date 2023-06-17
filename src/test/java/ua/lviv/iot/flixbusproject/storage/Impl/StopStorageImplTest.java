package ua.lviv.iot.flixbusproject.storage.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.flixbusproject.model.Stop;
import ua.lviv.iot.flixbusproject.storage.StopStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StopStorageImplTest {
    private StopStorage stopStorage;
    private final String PATH_TO_ACTUAL = String.join(File.separator,
            "src", "test", "resources", "stops", "actual", "write", "actual.csv");
    private final String PATH_TO_EXPECTED = String.join(File.separator,
            "src", "test", "resources", "stops", "expected", "expected.csv");
    private final String PATH_TO_EMPTY = String.join(File.separator,
            "src", "test", "resources", "stops", "expected", "expected_empty.csv");

    @BeforeEach
    void setUp() {
        stopStorage = new StopStorageImpl();
    }

    @Test
    void writeToCSV() throws IOException {
        Stop stop = new Stop(1, "Stop 1", "Address 1", 10.0, 20.0);

        stopStorage.writeToCSV(stop, PATH_TO_ACTUAL);

        Map<Integer, Stop> expectedStops = new HashMap<>();
        expectedStops.put(1, stop);

        Map<Integer, Stop> actualStops = stopStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedStops, actualStops);
    }

    @Test
    void writeToCSVWithNullStop() throws IOException {
        stopStorage.writeToCSV(null, PATH_TO_ACTUAL);

        assertTrue(new File(PATH_TO_ACTUAL).exists());
    }

    @Test
    void deleteFromCSV() throws IOException {
        Stop stop1 = new Stop(1, "Stop 1", "Address 1", 10.0, 20.0);
        Stop stop2 = new Stop(2, "Stop 2", "Address 2", 15.0, 25.0);

        stopStorage.writeToCSV(stop1, PATH_TO_ACTUAL);
        stopStorage.writeToCSV(stop2, PATH_TO_ACTUAL);

        stopStorage.deleteFromCSV(1, new File[] { new File(PATH_TO_ACTUAL) });

        Map<Integer, Stop> expectedStops = new HashMap<>();
        expectedStops.put(2, stop2);

        Map<Integer, Stop> actualStops = stopStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedStops, actualStops);
    }

    @Test
    void changeEntityByID() throws IOException {
        Stop stop1 = new Stop(1, "Stop 1", "Address 1", 10.0, 20.0);
        Stop stop2 = new Stop(2, "Stop 2", "Address 2", 15.0, 25.0);

        stopStorage.writeToCSV(stop1, PATH_TO_ACTUAL);
        stopStorage.writeToCSV(stop2, PATH_TO_ACTUAL);

        Stop updatedStop = new Stop(1, "Updated Stop", "Updated Address", 12.0, 22.0);
        stopStorage.changeEntityByID(1, updatedStop, new File[] { new File(PATH_TO_ACTUAL) });

        Map<Integer, Stop> expectedStops = new HashMap<>();
        expectedStops.put(1, updatedStop);
        expectedStops.put(2, stop2);

        Map<Integer, Stop> actualStops = stopStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedStops, actualStops);
    }

    @Test
    void readFromCSV() {
        Map<Integer, Stop> expectedStops = new HashMap<>();
        expectedStops.put(1, new Stop(1, "Stop 1", "Address 1", 10.0, 20.0));
        expectedStops.put(2, new Stop(2, "Stop 2", "Address 2", 15.0, 25.0));

        Map<Integer, Stop> actualStops = stopStorage.readFromCSV(new File(PATH_TO_EXPECTED));
        assertEquals(expectedStops, actualStops);
    }

    @Test
    void readFromCSVWithEmptyDirectory() {
        Map<Integer, Stop> actualStops = stopStorage.readFromCSV(new File(PATH_TO_EMPTY));
        assertTrue(actualStops.isEmpty());
    }

    @Test
    void getLastId() {
        Map<Integer, Stop> stops = new HashMap<>();
        stops.put(1, new Stop(1, "Stop 1", "Address 1", 10.0, 20.0));
        stops.put(2, new Stop(2, "Stop 2", "Address 2", 15.0, 25.0));

        int lastId = stopStorage.getLastId(stops);
        assertEquals(2, lastId);
    }

    @Test
    void getLastIdWithEmptyStops() {
        Map<Integer, Stop> stops = new HashMap<>();

        int lastId = stopStorage.getLastId(stops);
        assertEquals(0, lastId);
    }
}
