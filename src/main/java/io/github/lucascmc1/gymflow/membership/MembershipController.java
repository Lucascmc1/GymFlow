package io.github.lucascmc1.gymflow.membership;

import io.github.lucascmc1.gymflow.member.MemberService;
import io.github.lucascmc1.gymflow.plan.PlanService;
import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
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
@RequestMapping("/memberships")
public class MembershipController {

    private final MembershipService membershipService;
    private final MemberService memberService;
    private final PlanService planService;

    public MembershipController(MembershipService membershipService, MemberService memberService,
                                PlanService planService) {
        this.membershipService = membershipService;
        this.memberService = memberService;
        this.planService = planService;
    }

    @GetMapping
    String list(@RequestParam(required = false) MembershipStatus status, Model model) {
        model.addAttribute("memberships", membershipService.findAll(status));
        model.addAttribute("statuses", MembershipStatus.values());
        model.addAttribute("selectedStatus", status);
        return "memberships/list";
    }

    @GetMapping("/new")
    String createForm(@RequestParam(required = false) Long memberId, Model model) {
        MembershipForm form = new MembershipForm();
        form.setMemberId(memberId);
        populateForm(model, form);
        return "memberships/form";
    }

    @PostMapping
    String create(@Valid @ModelAttribute MembershipForm membershipForm,
                  BindingResult bindingResult, Model model,
                  RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                Membership membership = membershipService.create(membershipForm);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Matrícula criada e cobranças geradas.");
                return "redirect:/members/" + membership.getMember().getId();
            } catch (BusinessRuleException exception) {
                bindingResult.reject("business", exception.getMessage());
            }
        }
        populateForm(model, membershipForm);
        return "memberships/form";
    }

    @PostMapping("/{id}/cancel")
    String cancel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            membershipService.cancel(id);
            redirectAttributes.addFlashAttribute("successMessage", "Matrícula cancelada.");
        } catch (BusinessRuleException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/memberships";
    }

    private void populateForm(Model model, MembershipForm form) {
        model.addAttribute("membershipForm", form);
        model.addAttribute("members", memberService.findActive());
        model.addAttribute("plans", planService.findActive());
    }
}
