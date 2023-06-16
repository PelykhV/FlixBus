package ua.lviv.iot.flixbusproject.storage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface TemplateStorage<T, ID> {
    void writeToCSV(T entity, String pathToFiles) throws IOException;

    void deleteFromCSV(ID id, File[] files) throws IOException;

    void changeEntityByID(ID id, T entity, File[] files) throws IOException;

    Map<ID, T> readFromCSV(File monthDirectory);

    Integer getLastId(Map<ID, T> entities);
}
