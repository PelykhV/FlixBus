package ua.lviv.iot.flixbusproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stop implements Record {
    private Integer id;
    private String name;
    private String address;
    private double fare;
    private double distance;

    @JsonIgnore
    @Override
    public String getHeaders() {
        return "id, name, address, fare, distance\n";
    }

    @Override
    public String toCSV() {
        return String.join(",", String.valueOf(id), name, address, String.valueOf(fare), String.valueOf(distance));
    }
}
