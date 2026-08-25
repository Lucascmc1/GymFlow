package io.github.lucascmc1.gymflow.dashboard;

import io.github.lucascmc1.gymflow.checkin.CheckInRepository;
import io.github.lucascmc1.gymflow.member.MemberRepository;
import io.github.lucascmc1.gymflow.member.MemberStatus;
import io.github.lucascmc1.gymflow.payment.PaymentRepository;
import io.github.lucascmc1.gymflow.payment.PaymentStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final CheckInRepository checkInRepository;

    public DashboardService(MemberRepository memberRepository, PaymentRepository paymentRepository,
                            CheckInRepository checkInRepository) {
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
        this.checkInRepository = checkInRepository;
    }

    @Transactional
    public DashboardSnapshot load() {
        LocalDate today = LocalDate.now();
        paymentRepository.updateOverdue(today);
        YearMonth currentMonth = YearMonth.from(today);
        BigDecimal monthlyRevenue = paymentRepository.sumPaidBetween(
                currentMonth.atDay(1), currentMonth.atEndOfMonth());
        long checkInsToday = checkInRepository.countByCheckedInAtBetween(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay().minusNanos(1));

        return new DashboardSnapshot(
                memberRepository.countByStatus(MemberStatus.ACTIVE),
                monthlyRevenue,
                paymentRepository.countByStatus(PaymentStatus.OVERDUE),
                checkInsToday,
                paymentRepository.sumReceivable(),
                checkInRepository.findTop12ByOrderByCheckedInAtDesc(),
                paymentRepository.findTop5ByStatusOrderByDueDateAsc(PaymentStatus.OVERDUE),
                revenueSeries(currentMonth),
                hourActivity(today));
    }

    private List<ChartPoint> revenueSeries(YearMonth currentMonth) {
        List<RawPoint> rawPoints = new ArrayList<>();
        BigDecimal maximum = BigDecimal.ONE;
        for (int offset = 5; offset >= 0; offset--) {
            YearMonth month = currentMonth.minusMonths(offset);
            BigDecimal value = paymentRepository.sumPaidBetween(month.atDay(1), month.atEndOfMonth());
            String label = month.getMonth().getDisplayName(TextStyle.SHORT, PT_BR)
                    .replace(".", "");
            rawPoints.add(new RawPoint(label, value));
            maximum = maximum.max(value);
        }
        BigDecimal chartMaximum = maximum;
        return rawPoints.stream()
                .map(point -> new ChartPoint(point.label(), point.value(),
                        point.value().multiply(BigDecimal.valueOf(100))
                                .divide(chartMaximum, 0, RoundingMode.HALF_UP).intValue()))
                .toList();
    }

    private List<HourActivity> hourActivity(LocalDate today) {
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay().minusNanos(1);
        int[] hours = {6, 8, 10, 12, 14, 16, 18, 20, 22};
        List<Long> counts = new ArrayList<>();
        long maximum = 1;
        for (int hour : hours) {
            long count = checkInRepository.countForHour(start, end, hour);
            counts.add(count);
            maximum = Math.max(maximum, count);
        }
        List<HourActivity> result = new ArrayList<>();
        for (int index = 0; index < hours.length; index++) {
            long count = counts.get(index);
            result.add(new HourActivity(hours[index] + "h", count,
                    (int) Math.round(count * 100.0 / maximum)));
        }
        return result;
    }

    private record RawPoint(String label, BigDecimal value) {
    }
}
