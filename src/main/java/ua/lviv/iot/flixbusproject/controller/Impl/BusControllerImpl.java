package ua.lviv.iot.flixbusproject.controller.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lviv.iot.flixbusproject.controller.BusController;
import ua.lviv.iot.flixbusproject.model.Bus;
import ua.lviv.iot.flixbusproject.service.BusService;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/buses")
public class BusControllerImpl implements BusController {
    private final BusService busService;

    @Autowired
    public BusControllerImpl(final BusService busService) {
        this.busService = busService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Bus> getById(@PathVariable final Integer id) {
        Bus bus = busService.getById(id);
        if (bus != null) {
            return ResponseEntity.ok(bus);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/reserved")
    public ResponseEntity<Collection<Bus>> getReservedBuses() {
        Collection<Bus> buses = busService.getReservedBuses();
        if (buses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(buses);
    }


    @Override
    @GetMapping
    public ResponseEntity<Collection<Bus>> getAll() {
        Collection<Bus> buses = busService.getAll();
        if (buses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(buses);
    }
    @Override
    @PostMapping
    public ResponseEntity<Bus> create(@RequestBody final Bus entity) throws IOException {
        Bus bus = busService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(bus);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Bus> update(@PathVariable final Integer id,
                                      @RequestBody final Bus entity) throws IOException {
        if (!busService.getBuses().containsKey(id)) {
            return ResponseEntity.noContent().build();
        }
        Bus bus = busService.update(id, entity);
        return ResponseEntity.ok(bus);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Bus> delete(@PathVariable final Integer id) throws IOException {
        if (!busService.getBuses().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        busService.delete(id);
        return ResponseEntity.ok().build();
    }
}
