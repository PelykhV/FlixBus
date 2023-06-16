package ua.lviv.iot.flixbusproject.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.flixbusproject.datagetter.DateGetter;
import ua.lviv.iot.flixbusproject.model.Bus;
import ua.lviv.iot.flixbusproject.model.Record;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {
    private final String WRONG_PATH = String.join(File.separator,
            "src", "test", "resources", "manager_test", "qwe.csv");
    private final String PATH = String.join(File.separator,
            "src", "test", "resources", "manager_test", "file_manager.csv");
    private final String PATH2 = String.join(File.separator,
            "src", "test", "resources", "manager_test", "file_manager2.csv");

    FileManager manager;
    Record entity;

    @BeforeEach
    void setUp() {
        manager = new FileManager();
        entity = new Bus();
    }

    @Test
    void fileExists() {
        boolean result = manager.fileExists(PATH);

        assertTrue(result);
    }

    @Test
    void fileNotExists() {
        boolean result = manager.fileExists(WRONG_PATH);

        assertFalse(result);
    }

    @Test
    void getMonthDirectoryPath() {
        String path = manager.getMonthDirectoryPath(entity);

        assertNotNull(path);
        assertTrue(path.endsWith(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1)));
    }

    @Test
    void getFileFromCurrentMonths_ExistingDirectory() {
        File[] files = manager.getFileFromCurrentMonth(entity);

        assertNotNull(files);
        assertTrue(files.length > 0);
    }

    @Test
    void readLinesFromFile_existingFile() throws IOException {
        File file = new File(PATH);

        List<String> lines = manager.readLinesFromFile(file);

        assertEquals(5, lines.size());
    }

    @Test
    void readLinesFromFile_nonExistingFile() throws IOException {
        File file = new File(PATH2);

        List<String> lines = manager.readLinesFromFile(file);

        assertEquals(0, lines.size());
    }

    @Test
    void getFilePath_returnsCorrectPath() {
        String filePath = manager.getFilePath(entity);

        assertNotNull(filePath);
        assertTrue(filePath.endsWith(String.format("%s-%s.csv", entity.getClass().getSimpleName(), DateGetter.getCurrentDate())));
    }
}
