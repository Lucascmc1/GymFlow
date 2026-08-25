package io.github.lucascmc1.gymflow.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @EntityGraph(attributePaths = {"membership", "membership.member", "membership.plan"})
    List<Payment> findAllByOrderByDueDateDesc();

    @EntityGraph(attributePaths = {"membership", "membership.member", "membership.plan"})
    List<Payment> findByStatusOrderByDueDateDesc(PaymentStatus status);

    @EntityGraph(attributePaths = {"membership", "membership.member", "membership.plan"})
    List<Payment> findByMembershipMemberIdOrderByDueDateDesc(Long memberId);

    @EntityGraph(attributePaths = {"membership", "membership.member", "membership.plan"})
    List<Payment> findTop5ByStatusOrderByDueDateAsc(PaymentStatus status);

    long countByStatus(PaymentStatus status);

    @Query("""
            SELECT COALESCE(SUM(p.amount), 0) FROM Payment p
            WHERE p.status = io.github.lucascmc1.gymflow.payment.PaymentStatus.PAID
              AND p.paidAt BETWEEN :startDate AND :endDate
            """)
    BigDecimal sumPaidBetween(@Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT COALESCE(SUM(p.amount), 0) FROM Payment p
            WHERE p.status IN (
                io.github.lucascmc1.gymflow.payment.PaymentStatus.PENDING,
                io.github.lucascmc1.gymflow.payment.PaymentStatus.OVERDUE)
            """)
    BigDecimal sumReceivable();

    @Modifying
    @Query("""
            UPDATE Payment p SET p.status = io.github.lucascmc1.gymflow.payment.PaymentStatus.OVERDUE
            WHERE p.status = io.github.lucascmc1.gymflow.payment.PaymentStatus.PENDING
              AND p.dueDate < :today
            """)
    int updateOverdue(@Param("today") LocalDate today);
}
