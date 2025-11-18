package com.golflink.golflink.service;
import com.golflink.golflink.domain.District;
import com.golflink.golflink.domain.GolfCourse;
import com.golflink.golflink.domain.Professional;
import com.golflink.golflink.dto.CourseInfoResponseDto;
import com.golflink.golflink.dto.GolfCourseAdminDto;
import com.golflink.golflink.dto.adminDto.CourseRequestDto;
import com.golflink.golflink.repository.DistrictRepository;
import com.golflink.golflink.repository.GolfCourseRepository;
import com.golflink.golflink.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GolfCourseService {

    // 사용할 리포지토리를 주입받습니다.
    private final GolfCourseRepository golfCourseRepository;
    private final DistrictRepository districtRepository;
    private final ProfessionalRepository professionalRepository;

    // [신규] ListPage를 위한 핵심 메소드
    public List<CourseInfoResponseDto> findCoursesByDistrictWithRepresentativeInfo(Long districtId) {

        // 1. districtId로 해당하는 모든 골프장을 찾습니다.
        List<GolfCourse> courses = golfCourseRepository.findByDistrict_DistrictId(districtId);

        // 2. 각 골프장을 DTO로 변환합니다.
        return courses.stream()
                .map(course -> {
                    // 3. 각 골프장에 소속된 모든 프로를 찾습니다.
                    List<Professional> professionals = professionalRepository.findByGolfCourse_CourseId(course.getCourseId());

                    // 4. 프로들 중에서 가장 가격이 낮은 프로를 찾습니다. (대표 프로)
                    Optional<Professional> representativePro = professionals.stream()
                            .min(Comparator.comparing(Professional::getPrice));

                    // 5. 골프장 정보와 대표 프로 정보를 DTO에 담아 반환합니다.
                    //    대표 프로가 없는 경우 null을 전달하여 DTO에서 기본값을 사용하도록 합니다.
                    return new CourseInfoResponseDto(course, representativePro.orElse(null));
                })
                .collect(Collectors.toList());
    }

    // [유지] DetailPage 등에서 특정 골프장 정보를 조회할 때 사용합니다. (ID 타입 Long으로 변경)
    public GolfCourse getCourseById(Long courseId) {
        return golfCourseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + courseId));
    }

    public List<GolfCourseAdminDto> findByDistrictId(Long districtId) {
        return golfCourseRepository.findByDistrict_DistrictId(districtId).stream()
                .map(GolfCourseAdminDto::new)
                .collect(Collectors.toList());
    }

    // CRUD 기능 구현

    @Transactional // 데이터 변경이 있으므로 Transactional 어노테이션이 필수입니다.
    public Long createCourse(CourseRequestDto requestDto) {
        // 1. DTO에서 받은 districtId로 실제 District 엔티티를 찾습니다.
        District district = districtRepository.findById(requestDto.getDistrictId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 지역 그룹 ID입니다."));

        GolfCourse newCourse = new GolfCourse();
        // 2. 새로운 GolfCourse 엔티티를 생성합니다.
        newCourse.setName(requestDto.getName());
        newCourse.setAddress(requestDto.getAddress());
        newCourse.setLatitude(requestDto.getLatitude());
        newCourse.setLongitude(requestDto.getLongitude());
        newCourse.setParkingInfo(requestDto.getParkingInfo());
        newCourse.setScreenInfo(requestDto.getScreenInfo());
        newCourse.setDistrict(district); // 찾은 District 엔티티를 설정

        // 3. Repository를 통해 DB에 저장합니다.
        GolfCourse savedCourse = golfCourseRepository.save(newCourse);
        return savedCourse.getCourseId();
    }

    /**
     * 기존 골프장 정보를 수정합니다.
     */
    @Transactional
    public void updateCourse(Long courseId, CourseRequestDto requestDto) {
        // 1. 수정할 골프장 엔티티를 ID로 찾습니다.
        GolfCourse courseToUpdate = golfCourseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 골프장을 찾을 수 없습니다: " + courseId));

        // 2. DTO의 내용으로 엔티티의 값을 변경합니다.
        //    (JPA의 'Dirty Checking' 기능 덕분에 save를 호출하지 않아도 트랜잭션이 끝나면 자동 업데이트됩니다.)

        courseToUpdate.update(
                requestDto.getName(),
                requestDto.getAddress(),
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getParkingInfo(),
                requestDto.getScreenInfo()
        ); // 이렇게 안하고, NoArgsConstructor 애노테이션을 GolfCourse Entity에 달아주면
        // 바로 인자 넘겨주기 가능
    }

    /**
     * 특정 골프장을 삭제합니다.
     */
    @Transactional
    public void deleteCourse(Long courseId) {
        // [안전장치] 삭제하려는 골프장에 소속된 강사가 있는지 먼저 확인합니다.
        if (!professionalRepository.findByGolfCourse_CourseId(courseId).isEmpty()) {
            throw new IllegalStateException("삭제하려는 골프장에 소속된 강사가 있어 삭제할 수 없습니다.");
        }

        // 소속된 강사가 없으면 골프장을 삭제합니다.
        golfCourseRepository.deleteById(courseId);
    }

}