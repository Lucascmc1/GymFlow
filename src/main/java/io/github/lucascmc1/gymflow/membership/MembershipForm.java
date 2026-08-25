package io.github.lucascmc1.gymflow.membership;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MembershipForm {

    @NotNull(message = "Selecione um aluno")
    private Long memberId;

    @NotNull(message = "Selecione um plano")
    private Long planId;

    @NotNull(message = "Informe a data de início")
    private LocalDate startDate = LocalDate.now();

    @NotNull(message = "Informe o desconto")
    @DecimalMin(value = "0.00", message = "O desconto não pode ser negativo")
    @DecimalMax(value = "100.00", message = "O desconto não pode ultrapassar 100%")
    private BigDecimal discountPercent = BigDecimal.ZERO;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }
}
