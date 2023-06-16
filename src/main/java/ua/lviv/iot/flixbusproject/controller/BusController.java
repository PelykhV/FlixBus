package ua.lviv.iot.flixbusproject.controller;

import org.springframework.http.ResponseEntity;
import ua.lviv.iot.flixbusproject.model.Bus;

import java.util.Collection;

public interface BusController extends TemplateController<Bus, Integer> {
    ResponseEntity<Collection<Bus>> getReservedBuses();
}

