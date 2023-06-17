package ua.lviv.iot.flixbusproject.service.Impl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.flixbusproject.manager.FileManager;
import ua.lviv.iot.flixbusproject.model.Stop;
import ua.lviv.iot.flixbusproject.service.StopService;
import ua.lviv.iot.flixbusproject.storage.Impl.StopStorageImpl;
import ua.lviv.iot.flixbusproject.storage.StopStorage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StopServiceImpl implements StopService {
    private final Stop entityInstance = new Stop(1, "Stop 1", "Address 1", 0, 0.0);

    private final StopStorage storage = new StopStorageImpl();
    private final FileManager manager = new FileManager();
    private final File[] files = manager.getFileFromCurrentMonth(entityInstance);

    private final Map<Integer, Stop> stops;
    private Integer nextAvailableID;

    public StopServiceImpl() {
        this.stops = storage.readFromCSV(new File(manager.getMonthDirectoryPath(entityInstance)));
        this.nextAvailableID = storage.getLastId(stops) + 1;
    }

    @Override
    public Map<Integer, Stop> getStops() {
        return new HashMap<>(stops);
    }

    @Override
    public Stop getById(final Integer id) {
        return stops.get(id);
    }

    @Override
    public Collection<Stop> getAll() {
        return stops.values();
    }

    @Override
    public Stop create(final Stop entity) throws IOException {
        String fileName = manager.getFilePath(entity);
        entity.setId(nextAvailableID++);
        stops.put(entity.getId(), entity);
        storage.writeToCSV(entity, fileName);
        return entity;
    }

    @Override
    public Stop update(final Integer key, final Stop entity) throws IOException {
        if (stops.containsKey(key)) {
            entity.setId(key);
            storage.changeEntityByID(key, entity, files);
            stops.put(key, entity);
            return entity;
        }
        return null;
    }

    @Override
    public void delete(final Integer key) throws IOException {
        storage.deleteFromCSV(key, files);
        stops.remove(key);
    }

    @Override
    public Collection<Stop> getStopsByAddress(final String address) {
        return stops.values().stream()
                .filter(stop -> stop.getAddress().contains(address))
                .collect(Collectors.toList());
    }
}
