package io.github.lucascmc1.gymflow.checkin;

import io.github.lucascmc1.gymflow.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_ins")
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "checked_in_at", nullable = false)
    private LocalDateTime checkedInAt;

    protected CheckIn() {
    }

    public CheckIn(Member member, LocalDateTime checkedInAt) {
        this.member = member;
        this.checkedInAt = checkedInAt;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }
}
