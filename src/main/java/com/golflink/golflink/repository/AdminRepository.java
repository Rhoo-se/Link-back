package com.golflink.golflink.repository;

import com.golflink.golflink.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// repository 패키지에 생성
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);
}