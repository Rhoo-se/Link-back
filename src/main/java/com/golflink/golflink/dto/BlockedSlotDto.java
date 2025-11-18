package com.golflink.golflink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockedSlotDto {
    private Integer proId;
    private String blockedDate;
    private String blockedTime;
    private String reason;
}