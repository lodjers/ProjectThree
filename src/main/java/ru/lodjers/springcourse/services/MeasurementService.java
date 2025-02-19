package ru.lodjers.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lodjers.springcourse.dto.MeasurementDTO;
import ru.lodjers.springcourse.models.Measurement;
import ru.lodjers.springcourse.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    @Transactional
    public void save(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public long findRainyDays() {
        return measurementRepository.findAll().stream().filter(m -> m.isRaining()).count();
    }

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) {

        Measurement measurement = new Measurement();

        measurement.setRaining(measurementDTO.isRaining());
        measurement.setValue(measurementDTO.getValue());
        measurement.setSensor(sensorService.convertToSensor(measurementDTO.getSensorDTO()));

        return measurement;
    }

    public MeasurementDTO convertToMeasurementDTO(Measurement measurement) {

        MeasurementDTO measurementDTO = new MeasurementDTO();

        measurementDTO.setRaining(measurement.isRaining());
        measurementDTO.setValue(measurement.getValue());
        measurementDTO.setSensorDTO(sensorService.convertToSensorDTO(measurement.getSensor()));

        return measurementDTO;
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setCreatedAt(LocalDateTime.now());
    }
}
