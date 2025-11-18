package com.golflink.golflink.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "blocked_slots")
public class BlockedSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long blockId;

    // Professional 엔티티와 다대일 관계를 맺습니다.
    // 이 관계를 통해 어떤 프로의 시간이 마감되었는지 알 수 있습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id")
    private Professional pro;

    @Column(name = "blocked_date")
    private LocalDate blockedDate;

    @Column(name = "blocked_time")
    private String blockedTime;

    private String reason;
}
