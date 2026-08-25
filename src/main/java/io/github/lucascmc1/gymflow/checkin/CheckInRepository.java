package io.github.lucascmc1.gymflow.checkin;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    @EntityGraph(attributePaths = "member")
    List<CheckIn> findTop12ByOrderByCheckedInAtDesc();

    @EntityGraph(attributePaths = "member")
    List<CheckIn> findTop100ByOrderByCheckedInAtDesc();

    long countByCheckedInAtBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByMemberIdAndCheckedInAtBetween(Long memberId, LocalDateTime start,
                                                   LocalDateTime end);

    @Query("""
            SELECT COUNT(c) FROM CheckIn c
            WHERE c.checkedInAt BETWEEN :start AND :end
              AND HOUR(c.checkedInAt) = :hour
            """)
    long countForHour(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                      @Param("hour") int hour);
}
