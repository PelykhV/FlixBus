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
import ua.lviv.iot.flixbusproject.controller.RouteController;
import ua.lviv.iot.flixbusproject.model.Route;
import ua.lviv.iot.flixbusproject.service.RouteService;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/routes")
public class RouteControllerImpl implements RouteController {
    private final RouteService routeService;

    @Autowired
    public RouteControllerImpl(final RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Route> getById(@PathVariable final Integer id) {
        Route route = routeService.getById(id);
        if (route != null) {
            return ResponseEntity.ok(route);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<Route>> getAll() {
        Collection<Route> routes = routeService.getAll();
        if (routes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(routes);
    }

    @Override
    @PostMapping
    public ResponseEntity<Route> create(@RequestBody final Route entity) throws IOException {
        Route route = routeService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(route);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Route> update(@PathVariable final Integer id,
                                        @RequestBody final Route entity) throws IOException {
        if (!routeService.getRoutes().containsKey(id)) {
            return ResponseEntity.noContent().build();
        }
        Route route = routeService.update(id, entity);
        return ResponseEntity.ok(route);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Route> delete(@PathVariable final Integer id) throws IOException {
        if (!routeService.getRoutes().containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        routeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
