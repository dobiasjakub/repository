package com.dobiasdev.spa.converters;

import com.dobiasdev.spa.model.dtos.TripSegmentDto;
import com.dobiasdev.spa.persistance.entities.TripSegment;

import java.text.ParseException;


public class TripSegmentConverter extends ConverterBase implements EntityConverter<TripSegment, TripSegmentDto> {

    public TripSegmentConverter(ConverterManager converterManager) {
        super(converterManager);
    }
    @Override
    public TripSegmentDto convertToDto(TripSegment entity, NavigationFields navigation, ConvertingContext context) {
        if (entity==null) return null;

        var dto = new TripSegmentDto();

        read("TripSegment.id", f-> dto.setId(entity.getId()), context);
        read("TripSegment.startDestination", f-> dto.setStartDestination(entity.getStartDestination()), context);
        read("TripSegment.endDestination", f-> dto.setEndDestination(entity.getEndDestination()), context);
        read("TripSegment.distance", f-> dto.setDistance(entity.getDistance()), context);
        read("TripSegment.tripStart", f-> dto.setTripStart(entity.getTripStart()), context);
        read("TripSegment.tripEnd", f-> dto.setTripEnd(entity.getTripEnd()), context);
        read("TripSegment.created", f-> dto.setCreated(entity.getCreated()), context);;
        read("TripSegment.modified", f-> dto.setModified(entity.getModified()), context);
        read("TripSegment.travelType", f-> dto.setTravelType(entity.getTravelType()), context);

        navigation("TripSegment.trip", n->  dto.setTrip(converterManager.trip().convertToDto(entity.getTrip(),n, context)), navigation, context);

        return dto;
    }

    @Override
    public TripSegment convertToEntity(TripSegmentDto dto) throws ParseException {
        return null;
    }
}
