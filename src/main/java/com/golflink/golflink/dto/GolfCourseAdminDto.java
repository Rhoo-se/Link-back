package com.golflink.golflink.dto;

import com.golflink.golflink.domain.GolfCourse;
import lombok.Getter;

@Getter
public class GolfCourseAdminDto {
    private final Long courseId;
    private final String name;

    public GolfCourseAdminDto(GolfCourse golfCourse) {
        this.courseId = golfCourse.getCourseId();
        this.name = golfCourse.getName();
    }
}