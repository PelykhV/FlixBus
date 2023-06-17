package ua.lviv.iot.flixbusproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Bus implements Record {
    private Integer id;
    private boolean isReserved = false;
    private int age;
    private int capacity;
    private int mileage;
    private String manufacturer;

    @JsonIgnore
    @Override
    public String getHeaders() {
        return "id, isReserved, age, capacity, mileage, manufacturer\n";
    }

    @Override
    public String toCSV() {
        return String.join(", ", String.valueOf(id), String.valueOf(isReserved), String.valueOf(age), String.valueOf(capacity), String.valueOf(mileage), String.valueOf(manufacturer));
    }
}
