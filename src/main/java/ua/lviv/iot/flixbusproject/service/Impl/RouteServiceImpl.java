package ua.lviv.iot.flixbusproject.service.Impl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.flixbusproject.manager.FileManager;
import ua.lviv.iot.flixbusproject.model.Route;
import ua.lviv.iot.flixbusproject.service.RouteService;
import ua.lviv.iot.flixbusproject.storage.Impl.RouteStorageImpl;
import ua.lviv.iot.flixbusproject.storage.RouteStorage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {
    private final Route entityInstance = new Route();

    private final RouteStorage storage = new RouteStorageImpl();
    private final FileManager manager = new FileManager();
    private final File[] files = manager.getFileFromCurrentMonth(entityInstance);

    private final Map<Integer, Route> routes;
    private Integer nextAvailableID;

    public RouteServiceImpl() {
        this.routes = storage.readFromCSV(new File(manager.getMonthDirectoryPath(entityInstance)));
        this.nextAvailableID = storage.getLastId(routes) + 1;
    }

    @Override
    public Map<Integer, Route> getRoutes() {
        return new HashMap<>(routes);
    }

    @Override
    public Route getById(final Integer id) {
        return routes.get(id);
    }

    @Override
    public Collection<Route> getAll() {
        return routes.values();
    }

    @Override
    public Route create(final Route entity) throws IOException {
        String fileName = manager.getFilePath(entity);
        entity.setId(nextAvailableID++);
        routes.put(entity.getId(), entity);
        storage.writeToCSV(entity, fileName);
        return entity;
    }

    @Override
    public Route update(final Integer key, final Route entity) throws IOException {
        if (routes.containsKey(key)) {
            entity.setId(key);
            storage.changeEntityByID(key, entity, files);
            routes.put(key, entity);
            return entity;
        }
        return null;
    }

    @Override
    public void delete(final Integer key) throws IOException {
        storage.deleteFromCSV(key, files);
        routes.remove(key);
    }

    @Override
    public Collection<Route> getRoutesByStop(final String stopName) {
        return routes.values().stream()
                .filter(route -> route.getStops().stream()
                        .anyMatch(stop -> stop.getName().equals(stopName)))
                .collect(Collectors.toList());
    }
}
