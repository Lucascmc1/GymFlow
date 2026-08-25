package io.github.lucascmc1.gymflow.member;

import io.github.lucascmc1.gymflow.assessment.PhysicalAssessmentRepository;
import io.github.lucascmc1.gymflow.membership.MembershipRepository;
import io.github.lucascmc1.gymflow.payment.PaymentRepository;
import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import io.github.lucascmc1.gymflow.workout.WorkoutPlanRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MembershipRepository membershipRepository;
    private final PaymentRepository paymentRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final PhysicalAssessmentRepository assessmentRepository;

    public MemberController(MemberService memberService,
                            MembershipRepository membershipRepository,
                            PaymentRepository paymentRepository,
                            WorkoutPlanRepository workoutPlanRepository,
                            PhysicalAssessmentRepository assessmentRepository) {
        this.memberService = memberService;
        this.membershipRepository = membershipRepository;
        this.paymentRepository = paymentRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @GetMapping
    String list(@RequestParam(required = false) String query,
                @RequestParam(required = false) MemberStatus status,
                Model model) {
        model.addAttribute("members", memberService.search(query, status));
        model.addAttribute("query", query);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", MemberStatus.values());
        return "members/list";
    }

    @GetMapping("/new")
    String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        model.addAttribute("statuses", MemberStatus.values());
        model.addAttribute("formTitle", "Novo aluno");
        return "members/form";
    }

    @PostMapping
    String create(@Valid @ModelAttribute MemberForm memberForm, BindingResult bindingResult,
                  Model model, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                Member member = memberService.create(memberForm);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Aluno cadastrado com sucesso.");
                return "redirect:/members/" + member.getId();
            } catch (BusinessRuleException exception) {
                bindingResult.reject("business", exception.getMessage());
            }
        }
        model.addAttribute("statuses", MemberStatus.values());
        model.addAttribute("formTitle", "Novo aluno");
        return "members/form";
    }

    @GetMapping("/{id}")
    String detail(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.findById(id));
        model.addAttribute("memberships",
                membershipRepository.findByMemberIdOrderByStartDateDesc(id));
        model.addAttribute("payments",
                paymentRepository.findByMembershipMemberIdOrderByDueDateDesc(id));
        model.addAttribute("workouts", workoutPlanRepository.findByMemberIdOrderByUpdatedAtDesc(id));
        model.addAttribute("assessments",
                assessmentRepository.findByMemberIdOrderByAssessmentDateDesc(id));
        return "members/detail";
    }

    @GetMapping("/{id}/edit")
    String editForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("memberForm", MemberForm.from(member));
        model.addAttribute("memberId", id);
        model.addAttribute("statuses", MemberStatus.values());
        model.addAttribute("formTitle", "Editar aluno");
        return "members/form";
    }

    @PostMapping("/{id}")
    String update(@PathVariable Long id, @Valid @ModelAttribute MemberForm memberForm,
                  BindingResult bindingResult, Model model,
                  RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                memberService.update(id, memberForm);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Dados do aluno atualizados.");
                return "redirect:/members/" + id;
            } catch (BusinessRuleException exception) {
                bindingResult.reject("business", exception.getMessage());
            }
        }
        model.addAttribute("memberId", id);
        model.addAttribute("statuses", MemberStatus.values());
        model.addAttribute("formTitle", "Editar aluno");
        return "members/form";
    }

    @PostMapping("/{id}/toggle-status")
    String toggleStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        memberService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "Status do aluno atualizado.");
        return "redirect:/members/" + id;
    }
}
