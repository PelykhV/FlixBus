package ua.lviv.iot.flixbusproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Route implements Record {
    private Integer id;
    private String departureAddress;
    private String destinationAddress;
    private List<Stop> stops;
    private double ticketPrice;
    private double totalDistance;

    @JsonIgnore
    @Override
    public String getHeaders() {
        return "id, departureAddress, destinationAddress, stops, ticketPrice, totalDistance\n";
    }

    @Override
    public String toCSV() {
        return String.join(",", String.valueOf(id), String.valueOf(departureAddress), String.valueOf(destinationAddress), String.valueOf(stops), String.valueOf(ticketPrice), String.valueOf(totalDistance));
    }
}

