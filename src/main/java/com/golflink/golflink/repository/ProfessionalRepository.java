package com.golflink.golflink.repository;

import com.golflink.golflink.domain.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    List<Professional> findByGolfCourse_CourseId(Long courseId);


    @Query("SELECT p FROM Professional p JOIN p.golfCourse gc WHERE gc.district.districtId = :districtId")
    List<Professional> findPrimaryByDistrictId(@Param("districtId") Long districtId);



    List<Professional> findByActivityDistricts_DistrictId(Long districtId);


    @Query("SELECT p FROM Professional p JOIN FETCH p.golfCourse gc WHERE p.proId = :proId")
    Optional<Professional> findByIdWithGolfCourse(@Param("proId") Long proId);
}