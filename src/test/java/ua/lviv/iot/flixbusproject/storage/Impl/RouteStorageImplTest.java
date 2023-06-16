package ua.lviv.iot.flixbusproject.storage.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.flixbusproject.model.Route;
import ua.lviv.iot.flixbusproject.storage.RouteStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteStorageImplTest {
    private RouteStorage routeStorage;
    private final String PATH_TO_ACTUAL = String.join(File.separator,
            "src", "test", "resources", "routes", "actual", "write", "actual.csv");
    private final String PATH_TO_EXPECTED = String.join(File.separator,
            "src", "test", "resources", "routes", "expected", "expected.csv");
    private final String PATH_TO_EMPTY = String.join(File.separator,
            "src", "test", "resources", "routes", "expected", "expected_empty.csv");

    @BeforeEach
    void setUp() {
        routeStorage = new RouteStorageImpl();
    }

    @Test
    void writeToCSV() throws IOException {
        Route route = new Route(1, "Departure", "Destination", new ArrayList<>(), 10.0, 100.0);

        routeStorage.writeToCSV(route, PATH_TO_ACTUAL);

        Map<Integer, Route> expectedRoutes = new HashMap<>();
        expectedRoutes.put(1, route);

        Map<Integer, Route> actualRoutes = routeStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedRoutes, actualRoutes);
    }

    @Test
    void writeToCSVWithNullRoute() throws IOException {
        routeStorage.writeToCSV(null, PATH_TO_ACTUAL);

        assertTrue(new File(PATH_TO_ACTUAL).exists());
    }

    @Test
    void deleteFromCSV() throws IOException {
        Route route1 = new Route(1, "Departure 1", "Destination 1", new ArrayList<>(), 10.0, 100.0);
        Route route2 = new Route(2, "Departure 2", "Destination 2", new ArrayList<>(), 15.0, 150.0);

        routeStorage.writeToCSV(route1, PATH_TO_ACTUAL);
        routeStorage.writeToCSV(route2, PATH_TO_ACTUAL);

        routeStorage.deleteFromCSV(1, new File[] { new File(PATH_TO_ACTUAL) });

        Map<Integer, Route> expectedRoutes = new HashMap<>();
        expectedRoutes.put(2, route2);

        Map<Integer, Route> actualRoutes = routeStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedRoutes, actualRoutes);
    }

    @Test
    void changeEntityByID() throws IOException {
        Route route1 = new Route(1, "Departure 1", "Destination 1", new ArrayList<>(), 10.0, 100.0);
        Route route2 = new Route(2, "Departure 2", "Destination 2", new ArrayList<>(), 15.0, 150.0);

        routeStorage.writeToCSV(route1, PATH_TO_ACTUAL);
        routeStorage.writeToCSV(route2, PATH_TO_ACTUAL);

        Route updatedRoute = new Route(1, "Updated Departure", "Updated Destination", new ArrayList<>(), 12.0, 120.0);
        routeStorage.changeEntityByID(1, updatedRoute, new File[] { new File(PATH_TO_ACTUAL) });

        Map<Integer, Route> expectedRoutes = new HashMap<>();
        expectedRoutes.put(1, updatedRoute);
        expectedRoutes.put(2, route2);

        Map<Integer, Route> actualRoutes = routeStorage.readFromCSV(new File(PATH_TO_ACTUAL));
        assertEquals(expectedRoutes, actualRoutes);
    }

    @Test
    void readFromCSV() {
        Route route1 = new Route(1, "Departure 1", "Destination 1", new ArrayList<>(), 10.0, 100.0);
        Route route2 = new Route(2, "Departure 2", "Destination 2", new ArrayList<>(), 15.0, 150.0);

        Map<Integer, Route> expectedRoutes = new HashMap<>();
        expectedRoutes.put(1, route1);
        expectedRoutes.put(2, route2);

        Map<Integer, Route> actualRoutes = routeStorage.readFromCSV(new File(PATH_TO_EXPECTED));
        assertEquals(expectedRoutes, actualRoutes);
    }

    @Test
    void readFromCSVWithEmptyDirectory() {
        Map<Integer, Route> actualRoutes = routeStorage.readFromCSV(new File(PATH_TO_EMPTY));
        assertTrue(actualRoutes.isEmpty());
    }

    @Test
    void getLastId() {
        Map<Integer, Route> routes = new HashMap<>();
        routes.put(1, new Route(1, "Departure 1", "Destination 1", new ArrayList<>(), 10.0, 100.0));
        routes.put(2, new Route(2, "Departure 2", "Destination 2", new ArrayList<>(), 15.0, 150.0));

        int lastId = routeStorage.getLastId(routes);
        assertEquals(2, lastId);
    }

    @Test
    void getLastIdWithEmptyRoutes() {
        Map<Integer, Route> routes = new HashMap<>();

        int lastId = routeStorage.getLastId(routes);
        assertEquals(0, lastId);
    }
}
