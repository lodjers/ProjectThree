package ru.lodjers.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lodjers.springcourse.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

}
