package com.dobiasdev.spa.repositories;

import com.dobiasdev.spa.persistance.entities.Account;
import com.dobiasdev.spa.persistance.entities.TripSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripSegmentRepository extends JpaRepository<TripSegment, Long> {

}
