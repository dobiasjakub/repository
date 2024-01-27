package com.dobiasdev.spa.repositories;

import com.dobiasdev.spa.persistance.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

}
