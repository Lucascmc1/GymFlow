package io.github.lucascmc1.gymflow.membership;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findAllByOrderByStartDateDesc();

    @EntityGraph(attributePaths = {"member", "plan"})
    List<Membership> findByMemberIdOrderByStartDateDesc(Long memberId);

    Optional<Membership> findFirstByMemberIdAndStatusOrderByStartDateDesc(
            Long memberId, MembershipStatus status);

    boolean existsByMemberIdAndStatus(Long memberId, MembershipStatus status);

    long countByStatus(MembershipStatus status);

    @Query("""
            SELECT m FROM Membership m
            JOIN FETCH m.member
            JOIN FETCH m.plan
            WHERE (:status IS NULL OR m.status = :status)
            ORDER BY m.startDate DESC
            """)
    List<Membership> findForList(@Param("status") MembershipStatus status);
}
