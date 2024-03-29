package com.example.inhamonchallenge.domain.habit.dto;

import com.example.inhamonchallenge.domain.common.Category;
import com.example.inhamonchallenge.domain.habit.domain.Habit;
import com.example.inhamonchallenge.domain.record.domain.Record;
import com.example.inhamonchallenge.domain.record.dto.ImageRecordResponse;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitAndRecordResponse {

    private Long habitId;
    private Long userId;
    private String username;
    private Category category;
    private String title;
    private String content;
    private long dayCount;
    private LocalDateTime createdAt;
    private List<ImageRecordResponse> images;

    public static HabitAndRecordResponse from(Habit habit, List<Record> records) {
        return HabitAndRecordResponse.builder()
                .habitId(habit.getId())
                .userId(habit.getUser().getId())
                .username(habit.getUser().getName())
                .category(habit.getCategory())
                .title(habit.getTitle())
                .content(habit.getContent())
                .dayCount(Duration.between(habit.getCreatedAt(), LocalDateTime.now()).toDays() + 1)
                .createdAt(habit.getCreatedAt())
                .images(records.stream().map(record -> ImageRecordResponse.from(record)).collect(Collectors.toList()))
                .build();
    }

}
