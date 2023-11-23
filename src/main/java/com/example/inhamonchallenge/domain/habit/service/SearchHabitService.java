package com.example.inhamonchallenge.domain.habit.service;

import com.example.inhamonchallenge.domain.common.dto.Result;
import com.example.inhamonchallenge.domain.habit.domain.Habit;
import com.example.inhamonchallenge.domain.habit.dto.HabitResponse;
import com.example.inhamonchallenge.domain.habit.dto.SearchHabitResponse;
import com.example.inhamonchallenge.domain.habit.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchHabitService {

    private final HabitRepository habitRepository;

    public Result<List<SearchHabitResponse>> searchHabit(String keyword, Long cursor) {
        if(cursor == null) {
            cursor = Long.MAX_VALUE;
        }
        List<Habit> habits = habitRepository.searchHabitsByKeyword(keyword, cursor, PageRequest.of(0, 10)).getContent();
        return new Result<>(habits.stream()
                .map(SearchHabitResponse::from)
                .collect(Collectors.toList()));
    }
}
