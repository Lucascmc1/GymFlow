package io.github.lucascmc1.gymflow.assessment;

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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "physical_assessments")
public class PhysicalAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal weight;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal height;

    @Column(name = "body_fat_percentage", precision = 5, scale = 2)
    private BigDecimal bodyFatPercentage;

    @Column(name = "muscle_mass", precision = 6, scale = 2)
    private BigDecimal muscleMass;

    @Column(precision = 6, scale = 2)
    private BigDecimal waist;

    @Column(precision = 6, scale = 2)
    private BigDecimal chest;

    @Column(precision = 6, scale = 2)
    private BigDecimal arm;

    @Column(precision = 6, scale = 2)
    private BigDecimal thigh;

    @Column(length = 600)
    private String notes;

    protected PhysicalAssessment() {
    }

    public PhysicalAssessment(Member member, LocalDate assessmentDate, BigDecimal weight,
                              BigDecimal height) {
        this.member = member;
        this.assessmentDate = assessmentDate;
        this.weight = weight;
        this.height = height;
    }

    public BigDecimal getBmi() {
        if (weight == null || height == null || height.signum() == 0) {
            return BigDecimal.ZERO;
        }
        return weight.divide(height.multiply(height), 2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public BigDecimal getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(BigDecimal bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public BigDecimal getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(BigDecimal muscleMass) {
        this.muscleMass = muscleMass;
    }

    public BigDecimal getWaist() {
        return waist;
    }

    public void setWaist(BigDecimal waist) {
        this.waist = waist;
    }

    public BigDecimal getChest() {
        return chest;
    }

    public void setChest(BigDecimal chest) {
        this.chest = chest;
    }

    public BigDecimal getArm() {
        return arm;
    }

    public void setArm(BigDecimal arm) {
        this.arm = arm;
    }

    public BigDecimal getThigh() {
        return thigh;
    }

    public void setThigh(BigDecimal thigh) {
        this.thigh = thigh;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
