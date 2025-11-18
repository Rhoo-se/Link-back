package com.golflink.golflink.service;

import com.golflink.golflink.domain.Professional;
import com.golflink.golflink.dto.BookingInfoResponseDto;
import com.golflink.golflink.repository.ProfessionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 데이터 조회 목적이므로 readOnly = true
public class BookingService {

    private final ProfessionalRepository professionalRepository;

    public BookingInfoResponseDto getBookingInfo(Long proId) {
        // [⭐ 수정]
        // findById 대신, golfCourse 정보까지 JOIN해서 가져오는
        // findByIdWithGolfCourse 메서드를 사용합니다.
        Professional professional = professionalRepository.findByIdWithGolfCourse(proId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로를 찾을 수 없습니다: " + proId));

        // 조회한 엔티티를 DTO 생성자에 넘겨주어, API 응답에 맞는 객체로 변환합니다.
        return new BookingInfoResponseDto(professional);
    }
}