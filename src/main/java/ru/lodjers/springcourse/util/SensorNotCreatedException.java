package ru.lodjers.springcourse.util;

public class SensorNotCreatedException extends RuntimeException{
    public SensorNotCreatedException(String msg) {
        super(msg);
    }
}
