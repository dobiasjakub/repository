package com.dobiasdev.spa.converters;

import com.dobiasdev.spa.model.dtos.TripDto;
import com.dobiasdev.spa.model.dtos.TripSegmentDto;
import com.dobiasdev.spa.persistance.entities.Trip;

import java.text.ParseException;


public class TripConverter extends ConverterBase implements EntityConverter<Trip, TripDto> {

    public TripConverter(ConverterManager converterManager) {
        super(converterManager);
    }
    @Override
    public TripDto convertToDto(Trip entity, NavigationFields navigation, ConvertingContext context) {
        if (entity == null) return null;

        var dto = new TripDto();

        read("Trip.id", f-> dto.setId(entity.getId()), context);
        read("Trip.created", f-> dto.setCreated(entity.getCreated()), context);
        read("Trip.modified", f-> dto.setModified(entity.getModified()), context);

        navigation("Trip.tripSegments", n -> dto.setTripSegments(entity.getTripSegments().stream().map(t -> converterManager.tripSegment().convertToDto(t, n, context)).toArray(TripSegmentDto[]::new)), navigation, context);
        System.out.println(dto);
        return dto;
    }

    @Override
    public Trip convertToEntity(TripDto dto) throws ParseException {
        return null;
    }
}
