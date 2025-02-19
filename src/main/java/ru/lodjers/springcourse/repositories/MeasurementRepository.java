package ru.lodjers.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lodjers.springcourse.models.Measurement;
import ru.lodjers.springcourse.models.Sensor;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
}
