package com.golflink.golflink.repository;


import com.golflink.golflink.domain.MatchingReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<MatchingReservation, Long> {
    // JpaRepository를 상속받는 것만으로 기본적인 save(), findById(), findAll(), deleteById() 등의
    // 메서드가 자동으로 제공됩니다.
    // 현재로서는 별도의 커스텀 쿼리 메서드가 필요 없습니다.
    List<MatchingReservation> findAllByOrderByMatchingIdDesc();
}