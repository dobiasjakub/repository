package com.dobiasdev.spa.converters;

import com.dobiasdev.spa.serialization.JsonSerialization;
import org.springframework.stereotype.Component;

@Component
public class ConverterManager {
    private final UserConverter userConverter;
    private final TripConverter tripConverter;
    private final TripSegmentConverter tripSegmentConverter;

    public ConverterManager(JsonSerialization jsonSerialization) {
    userConverter = new UserConverter(this);
    tripConverter = new TripConverter(this);
    tripSegmentConverter = new TripSegmentConverter(this);
    }

    public UserConverter user() {return userConverter; }
    public TripConverter trip() {return tripConverter; }
    public TripSegmentConverter tripSegment() {return tripSegmentConverter; }
}
