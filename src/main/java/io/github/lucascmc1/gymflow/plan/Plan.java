package io.github.lucascmc1.gymflow.plan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @Column(nullable = false, length = 400)
    private String description;

    @Column(name = "monthly_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Column(nullable = false, length = 800)
    private String features;

    @Column(nullable = false)
    private boolean active;

    protected Plan() {
    }

    public Plan(String name, String description, BigDecimal monthlyPrice, Integer durationMonths,
                String features, boolean active) {
        this.name = name;
        this.description = description;
        this.monthlyPrice = monthlyPrice;
        this.durationMonths = durationMonths;
        this.features = features;
        this.active = active;
    }

    public Long getId() {
        return id;
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

    public String[] getFeatureList() {
        return features == null ? new String[0] : features.split("\\r?\\n");
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
