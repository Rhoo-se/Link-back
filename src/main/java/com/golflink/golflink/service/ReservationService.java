package com.golflink.golflink.service;

import com.golflink.golflink.config.TimeTableConstants;
import com.golflink.golflink.domain.BlockedSlot;
import com.golflink.golflink.domain.Professional;
import com.golflink.golflink.domain.Reservation;
import com.golflink.golflink.dto.BlockedSlotDto;
import com.golflink.golflink.dto.ReservationRequestDto;
import com.golflink.golflink.dto.ReservationResponseDto;
import com.golflink.golflink.dto.TimeSlotDto;
import com.golflink.golflink.repository.BlockedSlotRepository;
import com.golflink.golflink.repository.ProfessionalRepository;
import com.golflink.golflink.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BlockedSlotRepository blockedSlotRepository;
    @Autowired
    private ProfessionalRepository professionalRepository;

    public List<String> getReservedSlotsByDate(LocalDate date, Long proId) {
        List<Reservation> reservations = reservationRepository.findByReservationDateAndPro_ProId(date, proId);
        List<String> reservedTimes = reservations.stream()
                .map(Reservation::getReservationTime)
                .collect(Collectors.toList());

        List<BlockedSlot> blockedSlots = blockedSlotRepository.findByBlockedDateAndPro_ProId(date, proId);
        List<String> blockedTimes = blockedSlots.stream()
                .map(BlockedSlot::getBlockedTime)
                .collect(Collectors.toList());

        return Stream.concat(reservedTimes.stream(), blockedTimes.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        LocalDate date = LocalDate.parse(requestDto.getReservationDate());
        String time = requestDto.getReservationTime();

        List<Reservation> existingReservations = reservationRepository.findByReservationDateAndReservationTime(date, time);
        if (!existingReservations.isEmpty()) {
            throw new IllegalStateException("이미 예약된 시간대입니다.");
        }

        Professional pro = professionalRepository.findById(requestDto.getProId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid pro ID: " + requestDto.getProId()));

        Reservation newReservation = new Reservation();
        newReservation.setPro(pro);
        newReservation.setUserName(requestDto.getUserName());
        newReservation.setPhoneNumber(requestDto.getPhoneNumber());
        newReservation.setExperience(requestDto.getExperience());
        newReservation.setSwingCount(requestDto.getSwingCount());
        newReservation.setCoachingPart(requestDto.getCoachingPart());
        newReservation.setChannel(requestDto.getChannel());
        newReservation.setReservationDate(date);
        newReservation.setReservationTime(time);
        newReservation.setStatus("PENDING");

        Reservation savedReservation = reservationRepository.save(newReservation);
        return new ReservationResponseDto(savedReservation);
    }

    @Transactional
    public List<TimeSlotDto> getTimeSlotsByDateAndCourse(LocalDate date, Long courseId) {
        // [핵심 로직 개선]
        // 1. 해당 골프장에 소속된 모든 프로 목록을 먼저 가져옵니다.
        List<Professional> professionals = professionalRepository.findByGolfCourse_CourseId(courseId);
        if (professionals.isEmpty()) {
            log.warn("골프장 ID [{}]에 소속된 프로가 없습니다.", courseId);
            return new ArrayList<>(); // 프로가 없으면 빈 리스트를 반환합니다.
        }

        // 2. 해당 날짜와 골프장의 모든 예약 및 마감 정보를 한 번에 조회합니다.
        List<Reservation> reservations = reservationRepository.findByReservationDateAndPro_GolfCourse_CourseId(date, courseId);
        List<BlockedSlot> blockedSlots = blockedSlotRepository.findByBlockedDateAndPro_GolfCourse_CourseId(date, courseId);

        log.info("날짜 [{}], 골프장 ID [{}] 조회 결과: 프로 {}명, 예약 {}건, 관리자 마감 {}건", date, courseId, professionals.size(), reservations.size(), blockedSlots.size());

        // 3. 빠른 조회를 위해 Map으로 변환합니다. Key를 "proId-time" 형태로 만들어 프로별 시간 중복을 처리합니다.
        Map<String, Reservation> reservationMap = reservations.stream()
                .collect(Collectors.toMap(r -> r.getPro().getProId() + "-" + r.getReservationTime(), r -> r, (r1, r2) -> r1));
        Map<String, BlockedSlot> blockedSlotMap = blockedSlots.stream()
                .collect(Collectors.toMap(b -> b.getPro().getProId() + "-" + b.getBlockedTime(), b -> b, (b1, b2) -> b1));

        List<String> allTimes = TimeTableConstants.ALL_TIMES;
        List<TimeSlotDto> finalTimeSlots = new ArrayList<>();

        // 4. 각 프로별로 순회하며 전체 시간표(TimeSlot)를 생성합니다.
        for (Professional pro : professionals) {
            for (String time : allTimes) {
                String key = pro.getProId() + "-" + time;

                // 4-1. 예약이 있는 경우 (가장 높은 우선순위)
                if (reservationMap.containsKey(key)) {
                    Reservation r = reservationMap.get(key);
                    finalTimeSlots.add(new TimeSlotDto(time, r.getStatus().toLowerCase(), r.getUserName(), r.getReservationId(), r.getCoachingPart(), r.getPro().getName(), r.getChannel(), null, r.getPro().getProId()));
                }
                // 4-2. 관리자가 마감한 경우
                else if (blockedSlotMap.containsKey(key)) {
                    BlockedSlot b = blockedSlotMap.get(key);
                    finalTimeSlots.add(new TimeSlotDto(time, "blocked", null, null, null, pro.getName(), null, b.getBlockId(), pro.getProId()));
                }
                // 4-3. 그 외에는 모두 예약 가능한 경우
                else {
                    finalTimeSlots.add(new TimeSlotDto(time, "available", null, null, null, pro.getName(), null, null, pro.getProId()));
                }
            }
        }
        return finalTimeSlots;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    // [핵심 수정] 수동 시간 마감 로직
    @Transactional
    public void blockSlot(BlockedSlotDto dto) {
        // 1. DTO에서 받은 proId로 Professional 엔티티를 찾습니다.
        Professional pro = professionalRepository.findById(dto.getProId().longValue())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로를 찾을 수 없습니다: " + dto.getProId()));

        // 2. 새로운 BlockedSlot 엔티티를 생성하고, 찾은 Professional 엔티티를 설정합니다.
        BlockedSlot newBlockedSlot = new BlockedSlot();
        newBlockedSlot.setPro(pro); // <-- 문제 해결의 핵심! pro 정보를 명확히 연결합니다.
        newBlockedSlot.setBlockedDate(LocalDate.parse(dto.getBlockedDate()));
        newBlockedSlot.setBlockedTime(dto.getBlockedTime());
        newBlockedSlot.setReason(dto.getReason());

        // 3. 저장합니다.
        blockedSlotRepository.save(newBlockedSlot);
    }

    @Transactional
    public void unblockSlotById(Long blockedSlotId){
        if (!blockedSlotRepository.existsById(blockedSlotId)) {
            throw new IllegalArgumentException("ID: " + blockedSlotId + "에 해당하는 마감 슬롯을 찾을 수 없습니다.");
        }
        blockedSlotRepository.deleteById(blockedSlotId);
    }

    @Transactional
    public void confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + reservationId));
        reservation.setStatus("CONFIRMED");
        reservationRepository.save(reservation);
    }

    @Transactional
    public List<ReservationResponseDto> findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponseDto::new)
                .collect(Collectors.toList());
    }
}

