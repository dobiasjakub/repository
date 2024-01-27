package com.dobiasdev.spa.controllers.api;

import com.dobiasdev.spa.converters.ConverterManager;
import com.dobiasdev.spa.converters.ConvertingContext;
import com.dobiasdev.spa.converters.NavigationFields;
import com.dobiasdev.spa.model.dtos.TripDto;
import com.dobiasdev.spa.persistance.entities.Trip;
import com.dobiasdev.spa.persistance.entities.TripSegment;
import com.dobiasdev.spa.repositories.TripRepository;
import com.dobiasdev.spa.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/trips")
public class TripApiController {
    private final ConverterManager converterManager;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    protected TripApiController(ConverterManager converterManager, TripRepository tripRepository, UserRepository userRepository) {
        this.converterManager = converterManager;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public List<TripDto> getTrips()
    {
        System.out.println("TripController");
        var trips = tripRepository.findAll();
        var navigation = NavigationFields.create(new String[]{"user.id", "tripSegments"});
        System.out.println(trips.get(0).getTripSegments().size());
        System.out.println(trips.size());

        var a = trips.stream().map(t -> converterManager.trip().convertToDto(t, navigation, new ConvertingContext())).collect(Collectors.toList());
        System.out.println(a);
        return a;
    }

    @PostMapping("save/{userId}")
    @ResponseBody
    public boolean createTrip(@RequestBody TripDto newTrip, @PathVariable Long userId)
    {
        var user = userRepository.findById(userId).orElse(null);
        List<TripSegment> tripSegments = new ArrayList<>();
        if (newTrip == null || user == null)
            return false;

        var dbTrip = new Trip();
        dbTrip.setCreated(LocalDateTime.now());
        dbTrip.setUser(user);

        for (var dtoTripSegment : newTrip.getTripSegments()) {
            var dbTripSegment = new TripSegment();

            dbTripSegment.setCreated(LocalDateTime.now());
            dbTripSegment.setTripStart(dtoTripSegment.getTripStart());
            dbTripSegment.setTripEnd(dtoTripSegment.getTripEnd());
            dbTripSegment.setStartDestination(dtoTripSegment.getStartDestination());
            dbTripSegment.setEndDestination(dtoTripSegment.getEndDestination());
            dbTripSegment.setDistance(dtoTripSegment.getDistance());
            dbTripSegment.setTravelType(dtoTripSegment.getTravelType());
            dbTripSegment.setTrip(dbTrip);

            tripSegments.add(dbTripSegment);
        }

        dbTrip.setTripSegments(tripSegments);

        tripRepository.save(dbTrip);

        return true;
    }
}
