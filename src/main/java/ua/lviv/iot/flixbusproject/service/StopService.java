package ua.lviv.iot.flixbusproject.service;

import ua.lviv.iot.flixbusproject.model.Stop;

import java.util.Collection;
import java.util.Map;

public interface StopService extends TemplateService<Stop, Integer> {
    Map<Integer, Stop> getStops();

    Collection<Stop> getStopsByAddress(String city);
}
