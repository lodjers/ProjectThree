package ru.lodjers.springcourse.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lodjers.springcourse.dto.MeasurementDTO;
import ru.lodjers.springcourse.models.Measurement;
import ru.lodjers.springcourse.models.Sensor;
import ru.lodjers.springcourse.repositories.MeasurementRepository;
import ru.lodjers.springcourse.repositories.SensorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorRepository sensorRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService, ModelMapper modelMapper, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorRepository = sensorRepository;
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

        return modelMapper.map(measurementDTO, Measurement.class);
    }

    public MeasurementDTO convertToMeasurementDTO(Measurement measurement) {

        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    private void enrichMeasurement(Measurement measurement) {
        Sensor sensor = sensorRepository.findByName(measurement.getSensor().getName()).get();
        measurement.setCreatedAt(LocalDateTime.now());
        measurement.setSensor(sensor);
    }
}
