package io.github.lucascmc1.gymflow.dashboard;

import io.github.lucascmc1.gymflow.checkin.CheckIn;
import io.github.lucascmc1.gymflow.payment.Payment;
import java.math.BigDecimal;
import java.util.List;

public record DashboardSnapshot(
        long activeMembers,
        BigDecimal monthlyRevenue,
        long overduePayments,
        long checkInsToday,
        BigDecimal receivable,
        List<CheckIn> recentCheckIns,
        List<Payment> overdueList,
        List<ChartPoint> revenueSeries,
        List<HourActivity> hourActivity) {
}
