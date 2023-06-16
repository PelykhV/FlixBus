package ua.lviv.iot.flixbusproject.service;

import ua.lviv.iot.flixbusproject.model.Route;

import java.util.Collection;
import java.util.Map;

public interface RouteService extends TemplateService<Route, Integer> {
    Map<Integer, Route> getRoutes();

    Collection<Route> getRoutesByStop(String stopName);
}
