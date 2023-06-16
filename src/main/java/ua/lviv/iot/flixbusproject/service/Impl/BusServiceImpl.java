package ua.lviv.iot.flixbusproject.service.Impl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.flixbusproject.model.Bus;
import ua.lviv.iot.flixbusproject.manager.FileManager;
import ua.lviv.iot.flixbusproject.service.BusService;
import ua.lviv.iot.flixbusproject.storage.BusStorage;
import ua.lviv.iot.flixbusproject.storage.Impl.BusStorageImpl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusServiceImpl implements BusService {
    private final Bus entityInstance = new Bus();

    private final BusStorage storage = new BusStorageImpl();
    private final FileManager manager = new FileManager();
    private final File[] files = manager.getFileFromCurrentMonth(entityInstance);

    private final Map<Integer, Bus> buses;
    private Integer nextAvailableID;

    public BusServiceImpl() {
        this.buses = storage.readFromCSV(new File(manager.getMonthDirectoryPath(entityInstance)));
        this.nextAvailableID = storage.getLastId(buses) + 1;
    }

    @Override
    public Map<Integer, Bus> getBuses() {
        return new HashMap<>(buses);
    }

    @Override
    public Bus getById(final Integer id) {
        return buses.get(id);
    }

    @Override
    public Collection<Bus> getAll() {
        return buses.values();
    }

    @Override
    public Bus create(final Bus entity) throws IOException {
        String fileName = manager.getFilePath(entity);
        entity.setId(nextAvailableID++);
        buses.put(entity.getId(), entity);
        storage.writeToCSV(entity, fileName);
        return entity;
    }

    public Collection<Bus> getReservedBuses() {
        return buses.values().stream()
                .filter(Bus::isReserved)
                .collect(Collectors.toList());
    }

    @Override
    public Bus update(final Integer key, final Bus entity) throws IOException {
        if (buses.containsKey(key)) {
            entity.setId(key);
            storage.changeEntityByID(key, entity, files);
            buses.put(key, entity);
            return entity;
        }
        return null;
    }

    @Override
    public void delete(final Integer key) throws IOException {
        storage.deleteFromCSV(key, files);
        buses.remove(key);
    }
}
