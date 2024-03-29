package com.example.inhamonchallenge.domain.record.repository;

import com.example.inhamonchallenge.domain.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {


    @Query("select r from Record r where ((r.user.id in " +
            "(select f.following.id from Follow f where f.follower.id = :userId) and r.privacy != 'PRIVATE') " +
            "or (r.privacy = 'PUBLIC')) " +
            "AND r.user.isDeleted = false " +
            "and r.id < :cursor order by r.id desc")
    Slice<Record> findFollowingTop4(@Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);

    @Query("select r from Record r where r.privacy = 'PUBLIC' " +
            "AND r.user.isDeleted = false " +
            "and r.id < :cursor order by r.id desc")
    Slice<Record> findPublicTop4(@Param("cursor") Long cursor, Pageable pageable);

    @Query("select r from Record r where r.user.id not in " +
            "(select f.following.id from Follow f where f.follower.id = :userId) " +
            "AND r.user.isDeleted = false " +
            "and r.user.id != :userId " +
            "and r.privacy = 'PUBLIC' " +
            "and r.id < :cursor order by r.id desc")
    Slice<Record> findNonFollowingTop(@Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);

    @Query("select r from Record r where r.privacy = 'PUBLIC' " +
            "AND r.user.isDeleted = false " +
            "and r.id < :cursor order by r.id desc")
    Slice<Record> findPublicTop10(@Param("cursor") Long cursor, Pageable pageable);

    List<Record> findByHabitId(Long habitId);

    void deleteByUserId(Long id);
}
