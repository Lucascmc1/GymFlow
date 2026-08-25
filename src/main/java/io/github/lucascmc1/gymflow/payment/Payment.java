package io.github.lucascmc1.gymflow.payment;

import io.github.lucascmc1.gymflow.membership.Membership;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;

    @Column(name = "reference_date", nullable = false)
    private LocalDate referenceDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "paid_at")
    private LocalDate paidAt;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private PaymentMethod method;

    @Column(length = 500)
    private String note;

    protected Payment() {
    }

    public Payment(Membership membership, LocalDate referenceDate, LocalDate dueDate,
                   BigDecimal amount, PaymentStatus status) {
        this.membership = membership;
        this.referenceDate = referenceDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.status = status;
    }

    public void markAsPaid(PaymentMethod paymentMethod, LocalDate paymentDate) {
        this.status = PaymentStatus.PAID;
        this.method = paymentMethod;
        this.paidAt = paymentDate;
    }

    public void markAsOverdue() {
        if (status == PaymentStatus.PENDING && dueDate.isBefore(LocalDate.now())) {
            status = PaymentStatus.OVERDUE;
        }
    }

    public Long getId() {
        return id;
    }

    public Membership getMembership() {
        return membership;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getPaidAt() {
        return paidAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
