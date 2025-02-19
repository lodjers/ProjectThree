package ru.lodjers.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lodjers.springcourse.dto.SensorDTO;
import ru.lodjers.springcourse.models.Sensor;
import ru.lodjers.springcourse.repositories.SensorRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    @Transactional
    public void save(Sensor sensor) {
        sensorRepository.save(sensor);
    }

    public Optional<Sensor> show(String name) {
        return sensorRepository.findByName(name);
    }
    public Sensor convertToSensor(SensorDTO sensorDTO) {

        Sensor sensor = new Sensor();
        sensor.setName(sensorDTO.getName());

        return sensor;
    }

    public SensorDTO convertToSensorDTO(Sensor sensor) {

        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName(sensor.getName());

        return sensorDTO;
    }
}
