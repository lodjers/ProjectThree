package ru.lodjers.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.lodjers.springcourse.dto.SensorDTO;
import ru.lodjers.springcourse.models.Sensor;
import ru.lodjers.springcourse.services.SensorService;
import ru.lodjers.springcourse.util.SensorErrorResponse;
import ru.lodjers.springcourse.util.SensorNotCreatedException;
import ru.lodjers.springcourse.util.SensorValidator;


import java.util.List;
import java.util.stream.Collectors;


@RestController // @Controller + @ResponseBody над каждым методом
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
    }

    @GetMapping()
    public List<SensorDTO> getSensors() {
        return sensorService.findAll().stream()
                .map(sensorService::convertToSensorDTO).collect(Collectors.toList()); // Jackson конвертирует эти объекты в JSON
    }

    @PostMapping(value ="/registration",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SensorDTO sensorDTO,
                                             BindingResult bindingResult) {

        sensorValidator.validate(sensorService.convertToSensor(sensorDTO), bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new SensorNotCreatedException(errorMsg.toString());
        }
        sensorService.save(sensorService.convertToSensor(sensorDTO));

        //Отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        //в HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //NOT_FOUND - 494 статус
    }



    //В данном случае enrich делать не надо, т.к. у Sensor-a и SensorDTO нет полей типо Timespand,
    // а id выставляется автоматическаи
}