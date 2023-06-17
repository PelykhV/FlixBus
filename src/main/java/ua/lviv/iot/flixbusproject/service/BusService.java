package ua.lviv.iot.flixbusproject.service;

import ua.lviv.iot.flixbusproject.model.Bus;

import java.util.Collection;
import java.util.Map;

public interface BusService extends TemplateService<Bus, Integer> {
    Collection<Bus> getReservedBuses();

    Map<Integer, Bus> getBuses();
}
