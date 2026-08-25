package io.github.lucascmc1.gymflow.membership;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.lucascmc1.gymflow.member.Member;
import io.github.lucascmc1.gymflow.member.MemberService;
import io.github.lucascmc1.gymflow.member.MemberStatus;
import io.github.lucascmc1.gymflow.payment.Payment;
import io.github.lucascmc1.gymflow.payment.PaymentRepository;
import io.github.lucascmc1.gymflow.plan.Plan;
import io.github.lucascmc1.gymflow.plan.PlanService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private PlanService planService;

    private MembershipService membershipService;

    @BeforeEach
    void setUp() {
        membershipService = new MembershipService(membershipRepository, paymentRepository,
                memberService, planService);
    }

    @Test
    void appliesDiscountAndGeneratesPaymentsForContractDuration() {
        Member member = new Member("Marina Alves", "000.000.001-91", "marina@example.com",
                "(84) 99999-9999", LocalDate.of(1997, 3, 12), LocalDate.now(),
                MemberStatus.ACTIVE);
        Plan plan = new Plan("Evolve", "Plano semestral", new BigDecimal("149.90"), 6,
                "Musculação", true);
        MembershipForm form = new MembershipForm();
        form.setMemberId(1L);
        form.setPlanId(2L);
        form.setStartDate(LocalDate.of(2026, 8, 10));
        form.setDiscountPercent(new BigDecimal("10.00"));

        when(memberService.findById(1L)).thenReturn(member);
        when(planService.findById(2L)).thenReturn(plan);
        when(membershipRepository.save(any(Membership.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Membership membership = membershipService.create(form);

        assertThat(membership.getStatus()).isEqualTo(MembershipStatus.ACTIVE);
        assertThat(membership.getMonthlyPrice()).isEqualByComparingTo("134.91");
        assertThat(membership.getEndDate()).isEqualTo(LocalDate.of(2027, 2, 10));
        verify(paymentRepository, times(6)).save(any(Payment.class));
    }
}
