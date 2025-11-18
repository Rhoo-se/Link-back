package com.golflink.golflink.repository;// com/golflink/golflinkbackend/repository/GolfCourseRepository.java
import com.golflink.golflink.domain.GolfCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GolfCourseRepository extends JpaRepository<GolfCourse, Long> {
    List<GolfCourse> findByDistrict_DistrictId(Long districtId);
}