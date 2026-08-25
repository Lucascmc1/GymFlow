package io.github.lucascmc1.gymflow.plan;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class PlanForm {

    @NotBlank(message = "Informe o nome do plano")
    @Size(max = 80)
    private String name;

    @NotBlank(message = "Informe uma descrição")
    @Size(max = 400)
    private String description;

    @NotNull(message = "Informe o valor mensal")
    @DecimalMin(value = "1.00", message = "O valor deve ser maior que zero")
    private BigDecimal monthlyPrice;

    @NotNull(message = "Informe a duração")
    @Min(value = 1, message = "A duração mínima é de 1 mês")
    @Max(value = 36, message = "A duração máxima é de 36 meses")
    private Integer durationMonths;

    @NotBlank(message = "Informe ao menos um benefício")
    @Size(max = 800)
    private String features;

    private boolean active = true;

    public static PlanForm from(Plan plan) {
        PlanForm form = new PlanForm();
        form.name = plan.getName();
        form.description = plan.getDescription();
        form.monthlyPrice = plan.getMonthlyPrice();
        form.durationMonths = plan.getDurationMonths();
        form.features = plan.getFeatures();
        form.active = plan.isActive();
        return form;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
