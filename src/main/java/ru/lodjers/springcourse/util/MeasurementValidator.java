package ru.lodjers.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.lodjers.springcourse.dto.MeasurementDTO;
import ru.lodjers.springcourse.models.Measurement;
import ru.lodjers.springcourse.models.Sensor;
import ru.lodjers.springcourse.services.SensorService;

@Component
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;
//        Sensor sensor = (Sensor) target;

        if (sensorService.show(measurement.getSensor().getName()).isEmpty()) {
            errors.rejectValue("name", "", "This sensor name isn't exist");
        }
    }
}

