package io.github.lucascmc1.gymflow.dashboard;

import java.math.BigDecimal;

public record ChartPoint(String label, BigDecimal value, int percentage) {
}
