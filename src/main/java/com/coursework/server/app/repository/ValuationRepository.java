package com.coursework.server.app.repository;

import com.coursework.server.app.model.Valuation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ValuationRepository extends JpaRepository<Valuation, Long> {
    List<Valuation> findAllByUserId(Integer userId);

    @Query("SELECT COUNT(DISTINCT v.user.id) FROM Valuation v WHERE v.createdAt BETWEEN :startDate AND :endDate")
    long countDistinctUsersByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(v) FROM Valuation v WHERE v.createdAt BETWEEN :startDate AND :endDate")
    long countByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT EXTRACT(DOW FROM created_at) as day_of_week, COUNT(*) as count " +
           "FROM valuation " +
           "WHERE created_at >= :startDate " +
           "GROUP BY EXTRACT(DOW FROM created_at) " +
           "ORDER BY day_of_week", nativeQuery = true)
    List<Object[]> getCalculationsByDayOfWeek(@Param("startDate") LocalDateTime startDate);

    long countByScenarioIsNull();
    
    long countByScenarioIsNotNull();
}

