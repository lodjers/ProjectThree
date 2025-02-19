package ru.lodjers.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.lodjers.springcourse.dto.MeasurementDTO;
import ru.lodjers.springcourse.models.Measurement;
import ru.lodjers.springcourse.services.MeasurementService;
import ru.lodjers.springcourse.services.SensorService;
import ru.lodjers.springcourse.util.MeasurementNotCreatedException;
import ru.lodjers.springcourse.util.MeasurementValidator;
import ru.lodjers.springcourse.util.SensorErrorResponse;
import ru.lodjers.springcourse.util.SensorNotCreatedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController // @Controller + @ResponseBody над каждым методом
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementController(MeasurementService measurementService, MeasurementValidator measurementValidator) {
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MeasurementDTO measurementDTO,
                                             BindingResult bindingResult) {

        measurementValidator.validate(measurementService.convertToMeasurement(measurementDTO), bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new MeasurementNotCreatedException(errorMsg.toString());
        }
        measurementService.save(measurementService.convertToMeasurement(measurementDTO));

        //Отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(MeasurementNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        //в HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //NOT_FOUND - 494 статус
    }

    @GetMapping
    public List<MeasurementDTO> getMeasurements() {
        return measurementService.findAll().stream()
                .map(measurementService::convertToMeasurementDTO).collect(Collectors.toList());
    }
    @GetMapping("/rainyDaysCount")
    public long rainyDaysCount() {
        return measurementService.findRainyDays();
    }



}
