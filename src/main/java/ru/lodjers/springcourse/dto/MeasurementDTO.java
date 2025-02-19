package ru.lodjers.springcourse.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import ru.lodjers.springcourse.models.Sensor;

public class MeasurementDTO {

    @NotNull
    @Min(value = -100)
    @Max(value = 100)
    private int value;

    @NotNull
    private boolean raining;

    @NotNull
    private SensorDTO sensorDTO;


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensorDTO() {
        return sensorDTO;
    }

    public void setSensorDTO(SensorDTO sensorDTO) {
        this.sensorDTO = sensorDTO;
    }
}
