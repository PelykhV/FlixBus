package ua.lviv.iot.flixbusproject.service;

import java.io.IOException;
import java.util.Collection;

public interface TemplateService<T, ID> {
    T getById(ID id);

    Collection<T> getAll();

    T create(T entity) throws IOException;

    T update(ID id, T entity) throws IOException;

    void delete(ID id) throws IOException;
}
