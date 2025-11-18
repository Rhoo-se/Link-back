package com.golflink.golflink.repository;// com/golflink/golflink/repository/ReservationRepository.java
import com.golflink.golflink.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 특정 날짜의 예약 목록을 조회하는 메서드
    List<Reservation> findByReservationDate(LocalDate date);
    List<Reservation> findByReservationDateAndPro_GolfCourse_CourseId(LocalDate date, Long courseId);
    List<Reservation> findByReservationDateAndReservationTime(LocalDate date, String time);
    List<Reservation> findByReservationDateAndPro_ProId(LocalDate date, Long proId);

}