package io.github.lucascmc1.gymflow.assessment;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AssessmentForm {

    @NotNull(message = "Selecione um aluno")
    private Long memberId;

    @NotNull(message = "Informe a data da avaliação")
    private LocalDate assessmentDate = LocalDate.now();

    @NotNull(message = "Informe o peso")
    @DecimalMin("20.00")
    @DecimalMax("400.00")
    private BigDecimal weight;

    @NotNull(message = "Informe a altura")
    @DecimalMin("0.80")
    @DecimalMax("2.60")
    private BigDecimal height;

    @DecimalMin("1.00")
    @DecimalMax("70.00")
    private BigDecimal bodyFatPercentage;

    @DecimalMin("1.00")
    @DecimalMax("250.00")
    private BigDecimal muscleMass;

    private BigDecimal waist;
    private BigDecimal chest;
    private BigDecimal arm;
    private BigDecimal thigh;

    @Size(max = 600)
    private String notes;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
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
