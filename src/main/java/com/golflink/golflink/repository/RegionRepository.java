package com.golflink.golflink.repository;
import com.golflink.golflink.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    // JpaRepository가 기본적인 CRUD 메소드(findAll, findById 등)를 모두 제공합니다.
}