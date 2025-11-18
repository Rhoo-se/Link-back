package com.golflink.golflink.dto.adminDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProRequestDto {
    private String name;
    private Integer price;
    private String packageInfo;
    private String phrase;
    private String specialty;
    private Long courseId; // 신규 생성 시 어느 구장에 속할지 지정하기 위함
}
