package io.github.lucascmc1.gymflow.report;

import io.github.lucascmc1.gymflow.dashboard.ChartPoint;
import java.math.BigDecimal;
import java.util.List;

public record ReportSnapshot(
        BigDecimal currentRevenue,
        BigDecimal previousRevenue,
        BigDecimal revenueChange,
        BigDecimal receivable,
        BigDecimal averageTicket,
        long paidCount,
        long pendingCount,
        long overdueCount,
        List<ChartPoint> revenueSeries) {
}
