package io.github.lucascmc1.gymflow.membership;

import io.github.lucascmc1.gymflow.member.Member;
import io.github.lucascmc1.gymflow.member.MemberService;
import io.github.lucascmc1.gymflow.payment.Payment;
import io.github.lucascmc1.gymflow.payment.PaymentRepository;
import io.github.lucascmc1.gymflow.payment.PaymentStatus;
import io.github.lucascmc1.gymflow.plan.Plan;
import io.github.lucascmc1.gymflow.plan.PlanService;
import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import io.github.lucascmc1.gymflow.shared.ResourceNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final PaymentRepository paymentRepository;
    private final MemberService memberService;
    private final PlanService planService;

    public MembershipService(MembershipRepository membershipRepository,
                             PaymentRepository paymentRepository,
                             MemberService memberService,
                             PlanService planService) {
        this.membershipRepository = membershipRepository;
        this.paymentRepository = paymentRepository;
        this.memberService = memberService;
        this.planService = planService;
    }

    @Transactional(readOnly = true)
    public List<Membership> findAll(MembershipStatus status) {
        return membershipRepository.findForList(status);
    }

    @Transactional(readOnly = true)
    public Membership findById(Long id) {
        return membershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada"));
    }

    @Transactional
    public Membership create(MembershipForm form) {
        if (membershipRepository.existsByMemberIdAndStatus(form.getMemberId(),
                MembershipStatus.ACTIVE)) {
            throw new BusinessRuleException("Este aluno já possui uma matrícula ativa");
        }
        Member member = memberService.findById(form.getMemberId());
        Plan plan = planService.findById(form.getPlanId());
        if (!plan.isActive()) {
            throw new BusinessRuleException("O plano selecionado está inativo");
        }
        BigDecimal multiplier = BigDecimal.ONE.subtract(
                form.getDiscountPercent().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        BigDecimal finalPrice = plan.getMonthlyPrice().multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);
        LocalDate endDate = form.getStartDate().plusMonths(plan.getDurationMonths());
        Membership membership = new Membership(member, plan, form.getStartDate(), endDate,
                MembershipStatus.ACTIVE, finalPrice, form.getDiscountPercent());
        Membership saved = membershipRepository.save(membership);
        createPayments(saved);
        return saved;
    }

    @Transactional
    public void cancel(Long id) {
        Membership membership = findById(id);
        if (membership.getStatus() != MembershipStatus.ACTIVE) {
            throw new BusinessRuleException("Somente matrículas ativas podem ser canceladas");
        }
        membership.setStatus(MembershipStatus.CANCELLED);
    }

    private void createPayments(Membership membership) {
        LocalDate reference = membership.getStartDate().withDayOfMonth(1);
        for (int month = 0; month < membership.getPlan().getDurationMonths(); month++) {
            LocalDate currentReference = reference.plusMonths(month);
            int dueDay = Math.min(membership.getStartDate().getDayOfMonth(),
                    currentReference.lengthOfMonth());
            LocalDate dueDate = currentReference.withDayOfMonth(dueDay);
            Payment payment = new Payment(membership, currentReference, dueDate,
                    membership.getMonthlyPrice(), PaymentStatus.PENDING);
            paymentRepository.save(payment);
        }
    }
}
