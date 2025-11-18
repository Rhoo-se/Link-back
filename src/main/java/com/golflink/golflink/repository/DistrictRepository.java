package com.golflink.golflink.repository;
import com.golflink.golflink.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    List<District> findByRegion_RegionId(Long regionId);

    // Spring Data JPA가 메소드 이름을 분석해서 자동으로 쿼리를 생성합니다.
    // 이 메소드는 나중에 특정 지역에 속한 그룹만 찾고 싶을 때 사용할 수 있습니다.
    // List<District> findByRegion_RegionId(Long regionId);
}