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
import ua.lviv.iot.flixbusproject.controller.StopController;
import ua.lviv.iot.flixbusproject.model.Stop;
import ua.lviv.iot.flixbusproject.service.StopService;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/stops")
public class StopControllerImpl implements StopController {
    private final StopService stopService;

    @Autowired
    public StopControllerImpl(final StopService stopService) {
        this.stopService = stopService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Stop> getById(@PathVariable final Integer id) {
        Stop stop = stopService.getById(id);
        if (stop != null) {
            return ResponseEntity.ok(stop);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<Stop>> getAll() {
        Collection<Stop> stops = stopService.getAll();
        if (stops.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stops);
    }

    @Override
    @PostMapping
    public ResponseEntity<Stop> create(@RequestBody final Stop entity) throws IOException {
        Stop stop = stopService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(stop);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Stop> update(@PathVariable final Integer id,
                                       @RequestBody final Stop entity) throws IOException {
        if (!stopService.getStops().containsKey(id)) {
            return ResponseEntity.noContent().build();
        }
        Stop stop = stopService.update(id, entity);
        return ResponseEntity.ok(stop);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Stop> delete(@PathVariable final Integer id) throws IOException {
        if (!stopService.getStops().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        stopService.delete(id);
        return ResponseEntity.ok().build();
    }
}
