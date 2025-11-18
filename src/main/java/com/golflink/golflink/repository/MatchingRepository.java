package com.golflink.golflink.repository;


import com.golflink.golflink.domain.MatchingReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<MatchingReservation, Long> {

    List<MatchingReservation> findAllByOrderByMatchingIdDesc();
}