package ua.lviv.iot.flixbusproject.storage.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.flixbusproject.model.Bus;
import ua.lviv.iot.flixbusproject.storage.BusStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusStorageImplTest {
    private BusStorage busStorage;
    private final String PATH_TO_ACTUAL = String.join(File.separator,
            "src", "test", "resources", "buses", "actual", "write", "actual.csv");
    private final String PATH_TO_EXPECTED = String.join(File.separator,
            "src", "test", "resources", "buses", "expected", "expected.csv");
    private final String PATH_TO_EMPTY = String.join(File.separator,
            "src", "test", "resources", "buses", "expected", "expected_empty.csv");

    @BeforeEach
    void setUp() {
        busStorage = new BusStorageImpl();
    }

    @Test
    void writeToCSV() throws IOException {
        Bus bus = new Bus(1, false, 5, 50, 10000, "Manufacturer");

        busStorage.writeToCSV(bus, PATH_TO_ACTUAL);

        Map<Integer, Bus> expectedBuses = new HashMap<>();
        expectedBuses.put(1, bus);

        Map<Integer, Bus> actualBuses = busStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedBuses, actualBuses);
    }

    @Test
    void writeToCSVWithNullBus() throws IOException {
        busStorage.writeToCSV(null, PATH_TO_ACTUAL);

        assertTrue(new File(PATH_TO_ACTUAL).exists());
    }

    @Test
    void deleteFromCSV() throws IOException {
        Bus bus1 = new Bus(1, false, 5, 50, 10000, "Manufacturer 1");
        Bus bus2 = new Bus(2, true, 3, 40, 5000, "Manufacturer 2");

        busStorage.writeToCSV(bus1, PATH_TO_ACTUAL);
        busStorage.writeToCSV(bus2, PATH_TO_ACTUAL);

        busStorage.deleteFromCSV(1, new File[]{new File(PATH_TO_ACTUAL)});

        Map<Integer, Bus> expectedBuses = new HashMap<>();
        expectedBuses.put(2, bus2);

        Map<Integer, Bus> actualBuses = busStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedBuses, actualBuses);
    }

    @Test
    void changeEntityByID() throws IOException {
        Bus bus1 = new Bus(1, false, 5, 50, 10000, "Manufacturer 1");
        Bus bus2 = new Bus(2, true, 3, 40, 5000, "Manufacturer 2");

        busStorage.writeToCSV(bus1, PATH_TO_ACTUAL);
        busStorage.writeToCSV(bus2, PATH_TO_ACTUAL);

        Bus updatedBus = new Bus(1, true, 4, 60, 12000, "Updated Manufacturer");
        busStorage.changeEntityByID(1, updatedBus, new File[]{new File(PATH_TO_ACTUAL)});

        Map<Integer, Bus> expectedBuses = new HashMap<>();
        expectedBuses.put(1, updatedBus);
        expectedBuses.put(2, bus2);

        Map<Integer, Bus> actualBuses = busStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedBuses, actualBuses);
    }

    @Test
    void readFromCSV() {
        Bus bus1 = new Bus(1, false, 5, 50, 10000, "Manufacturer 1");
        Bus bus2 = new Bus(2, true, 3, 40, 5000, "Manufacturer 2");

        Map<Integer, Bus> expectedBuses = new HashMap<>();
        expectedBuses.put(1, bus1);
        expectedBuses.put(2, bus2);

        Map<Integer, Bus> actualBuses = busStorage.readFromCSV(new File(PATH_TO_EXPECTED));
        assertEquals(expectedBuses, actualBuses);
    }

    @Test
    void readFromCSVWithEmptyDirectory() {
        Map<Integer, Bus> actualBuses = busStorage.readFromCSV(new File(PATH_TO_EMPTY));
        assertTrue(actualBuses.isEmpty());
    }

    @Test
    void getLastId() {
        Map<Integer, Bus> buses = new HashMap<>();
        buses.put(1, new Bus(1, false, 5, 50, 10000, "Manufacturer 1"));
        buses.put(2, new Bus(2, true, 3, 40, 5000, "Manufacturer 2"));

        int lastId = busStorage.getLastId(buses);
        assertEquals(2, lastId);
    }

    @Test
    void getLastIdWithEmptyBuses() {
        Map<Integer, Bus> buses = new HashMap<>();

        int lastId = busStorage.getLastId(buses);
        assertEquals(0, lastId);
    }
}
