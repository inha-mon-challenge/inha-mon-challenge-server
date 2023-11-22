package com.example.inhamonchallenge.domain.record.repository;

import com.example.inhamonchallenge.domain.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("select r from Record r join fetch Follow f on r.user.id = f.following.id " +
            "where f.follower.id = :userId and r.id < :cursor order by r.id desc")
    Slice<Record> findFollowingTop4(@Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);
}
