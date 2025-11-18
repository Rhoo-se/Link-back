package com.golflink.golflink.repository;

import com.golflink.golflink.domain.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    // 기본적인 CRUD(Create, Read, Update, Delete) 메서드는
    // JpaRepository가 자동으로 제공하므로, 추가 코드가 필요 없습니다.
    List<Professional> findByGolfCourse_CourseId(Long courseId);

    // [개선된 코드] - 주 소속 프로 조회
    // 기존 findByDistrictId 메서드의 이름을 역할이 더 명확하도록 변경합니다.
    @Query("SELECT p FROM Professional p JOIN p.golfCourse gc WHERE gc.district.districtId = :districtId")
    List<Professional> findPrimaryByDistrictId(@Param("districtId") Long districtId);


    // [⭐ 추가할 코드] - 추가 활동 지역 프로 조회
    // Professional 엔티티에 @ManyToMany로 선언된 'activityDistricts' 필드를 기반으로
    // 해당 지역(District)이 포함된 프로를 찾아옵니다.
    List<Professional> findByActivityDistricts_DistrictId(Long districtId);


    @Query("SELECT p FROM Professional p JOIN FETCH p.golfCourse gc WHERE p.proId = :proId")
    Optional<Professional> findByIdWithGolfCourse(@Param("proId") Long proId);
}