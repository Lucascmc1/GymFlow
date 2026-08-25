package io.github.lucascmc1.gymflow.report;

import io.github.lucascmc1.gymflow.dashboard.ChartPoint;
import io.github.lucascmc1.gymflow.payment.PaymentRepository;
import io.github.lucascmc1.gymflow.payment.PaymentStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final PaymentRepository paymentRepository;

    public ReportService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public ReportSnapshot load() {
        YearMonth current = YearMonth.now();
        YearMonth previous = current.minusMonths(1);
        BigDecimal currentRevenue = sumMonth(current);
        BigDecimal previousRevenue = sumMonth(previous);
        BigDecimal change = previousRevenue.signum() == 0 ? BigDecimal.ZERO
                : currentRevenue.subtract(previousRevenue)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(previousRevenue, 1, RoundingMode.HALF_UP);
        long paidCount = paymentRepository.countByStatus(PaymentStatus.PAID);
        BigDecimal averageTicket = paidCount == 0 ? BigDecimal.ZERO
                : totalPaidForSeries(current).divide(BigDecimal.valueOf(paidCount), 2,
                        RoundingMode.HALF_UP);
        return new ReportSnapshot(currentRevenue, previousRevenue, change,
                paymentRepository.sumReceivable(), averageTicket, paidCount,
                paymentRepository.countByStatus(PaymentStatus.PENDING),
                paymentRepository.countByStatus(PaymentStatus.OVERDUE),
                revenueSeries(current));
    }

    private BigDecimal sumMonth(YearMonth month) {
        return paymentRepository.sumPaidBetween(month.atDay(1), month.atEndOfMonth());
    }

    private BigDecimal totalPaidForSeries(YearMonth current) {
        return paymentRepository.sumPaidBetween(current.minusMonths(5).atDay(1),
                current.atEndOfMonth());
    }

    private List<ChartPoint> revenueSeries(YearMonth current) {
        List<BigDecimal> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        BigDecimal maximum = BigDecimal.ONE;
        for (int offset = 5; offset >= 0; offset--) {
            YearMonth month = current.minusMonths(offset);
            BigDecimal value = sumMonth(month);
            values.add(value);
            labels.add(month.getMonth().getDisplayName(TextStyle.SHORT,
                    Locale.forLanguageTag("pt-BR")).replace(".", ""));
            maximum = maximum.max(value);
        }
        List<ChartPoint> points = new ArrayList<>();
        for (int index = 0; index < values.size(); index++) {
            int percentage = values.get(index).multiply(BigDecimal.valueOf(100))
                    .divide(maximum, 0, RoundingMode.HALF_UP).intValue();
            points.add(new ChartPoint(labels.get(index), values.get(index), percentage));
        }
        return points;
    }
}
