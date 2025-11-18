package com.golflink.golflink.repository;

import com.golflink.golflink.domain.BlockedSlot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface BlockedSlotRepository extends JpaRepository<BlockedSlot, Long> {


    List<BlockedSlot> findByBlockedDate(LocalDate date);
    @Transactional
    void deleteByBlockedDateAndBlockedTime(LocalDate date, String time);
    List<BlockedSlot> findByBlockedDateAndPro_ProId(LocalDate date, Long proId);
    List<BlockedSlot> findByBlockedDateAndPro_GolfCourse_CourseId(LocalDate date, Long courseId);
}