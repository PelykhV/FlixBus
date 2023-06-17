package ua.lviv.iot.flixbusproject.datagetter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateGetter {
    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
}
