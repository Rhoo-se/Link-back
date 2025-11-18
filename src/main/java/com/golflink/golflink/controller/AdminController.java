package com.golflink.golflink.controller; // 패키지명은 본인 프로젝트에 맞게 확인해주세요.

import com.golflink.golflink.domain.District;
import com.golflink.golflink.domain.GolfCourse;
import com.golflink.golflink.domain.Region;
import com.golflink.golflink.dto.*;
import com.golflink.golflink.dto.adminDto.AdminCourseDetailDto;
import com.golflink.golflink.dto.adminDto.AdminProListDto;
import com.golflink.golflink.dto.adminDto.CourseRequestDto;
import com.golflink.golflink.dto.adminDto.ProRequestDto;
import com.golflink.golflink.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    // [수정] 역할에 맞게 필요한 모든 서비스를 주입받습니다.
    private final ReservationService reservationService;
    private final RegionService regionService;
    private final DistrictService districtService;
    private final GolfCourseService golfCourseService;
    private final CourseAdminService courseAdminService;
    private final ProfessionalAdminService professionalAdminService;


    /**
     * [수정] 첫 번째 드롭다운: 모든 Region 목록을 가져옵니다.
     * 이제 RegionService를 호출합니다.
     */
    @GetMapping("/regions")
    public ResponseEntity<List<RegionDto>> getAllRegions() {
        return ResponseEntity.ok(regionService.findAll());
    }

    /**
     * [신규] 두 번째 드롭다운: 특정 Region에 속한 District 목록을 가져옵니다.
     */
    @GetMapping("/districts/all") // 기존 API와 주소가 겹치지 않도록 /all을 추가
    public ResponseEntity<List<DistrictDto>> getAllDistricts() {
        // DistrictService에 모든 District를 조회하는 findAll() 메서드를 호출합니다.
        return ResponseEntity.ok(districtService.findAll());
    }

    @GetMapping("/districts")
    public ResponseEntity<List<DistrictDto>> getDistrictsByRegion(@RequestParam("regionId") Long regionId) {
        return ResponseEntity.ok(districtService.findByRegionId(regionId));
    }



    @GetMapping("/courses")
    public ResponseEntity<List<GolfCourseAdminDto>> getCoursesByDistrict(@RequestParam("districtId") Long districtId) {
        return ResponseEntity.ok(golfCourseService.findByDistrictId(districtId));
    }


    @GetMapping("/all-reservations")
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> reservations = reservationService.findAllReservations();
        return ResponseEntity.ok(reservations);
    }


    // --- 이하 Reservation 관련 API는 ID 타입만 Long으로 통일합니다 ---

    @GetMapping("/timeslots")
    public ResponseEntity<List<TimeSlotDto>> getTimeSlotsByDate(
            @RequestParam("date") String dateString,
            @RequestParam("courseId") Long courseId) { // [수정] Integer -> Long
        LocalDate date = LocalDate.parse(dateString);
        return ResponseEntity.ok(reservationService.getTimeSlotsByDateAndCourse(date, courseId));
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/blocked-slots")
    public ResponseEntity<Void> blockSlot(@RequestBody BlockedSlotDto blockedSlotDto) {
        reservationService.blockSlot(blockedSlotDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/blocked-slots/{blockedSlotId}") // 👈 주소 끝에 /{...} 추가
    public ResponseEntity<Void> unblockSlot(@PathVariable Long blockedSlotId) { // 👈 @PathVariable로 받기
        reservationService.unblockSlotById(blockedSlotId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reservations/{reservationId}/confirm")
    public ResponseEntity<Void> confirmReservation(@PathVariable Long reservationId) {
        reservationService.confirmReservation(reservationId);
        return ResponseEntity.ok().build();
    }

    /* 이하 골프장 관리 및 강사 관리 컨트롤러 */

    // 1. 특정 골프장의 상세 정보를 조회하는 API
    // GET /api/admin/courses/{id}
    @GetMapping("/courses/{id}")
    public ResponseEntity<AdminCourseDetailDto> getCourseDetails(@PathVariable Long id) {
        AdminCourseDetailDto courseDetails = courseAdminService.findCourseDetailsById(id);
        return ResponseEntity.ok(courseDetails);
    }

    // 2. 특정 골프장에 소속된 강사 목록을 조회하는 API
    // GET /api/admin/courses/{id}/professionals
    @GetMapping("/courses/{id}/professionals")
    public ResponseEntity<List<AdminProListDto>> getProfessionalsByCourse(@PathVariable Long id) {
        List<AdminProListDto> professionals = courseAdminService.findProfessionalsByCourse(id);
        return ResponseEntity.ok(professionals);
    }
    
    // 골프장 CRUD 기능

    @PostMapping("/courses")
    public ResponseEntity<Long> createCourse(@RequestBody CourseRequestDto requestDto) {
        Long newCourseId = golfCourseService.createCourse(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourseId);
    }

    /**
     * 기존 골프장 정보를 수정합니다.
     * PUT /api/admin/courses/{id}
     */
    @PutMapping("/courses/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long id, @RequestBody CourseRequestDto requestDto) {
        golfCourseService.updateCourse(id, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 골프장을 삭제합니다.
     * DELETE /api/admin/courses/{id}
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        golfCourseService.deleteCourse(id);
        return ResponseEntity.noContent().build(); // 성공적으로 삭제되었으나 본문 내용 없음 (204)
    }



    //강사 CRUD 기능/////////////////

    /**
     * 신규 강사를 생성합니다. (이미지 포함)
     */

    @PostMapping("/professionals")
    public ResponseEntity<Long> createProfessional(
            @RequestPart("proDto") ProRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

        Long newProId = professionalAdminService.createProfessional(requestDto, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProId);
    }

    /**
     * 기존 강사 정보를 수정합니다. (이미지 포함)
     */
    @PutMapping("/professionals/{id}")
    public ResponseEntity<Void> updateProfessional(
            @PathVariable Long id,
            @RequestPart("proDto") ProRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

        professionalAdminService.updateProfessional(id, requestDto, imageFile);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 강사를 삭제합니다.
     */
    @DeleteMapping("/professionals/{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Long id) {
        professionalAdminService.deleteProfessional(id);
        return ResponseEntity.noContent().build();
    }
}