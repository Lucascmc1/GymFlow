package io.github.lucascmc1.gymflow.checkin;

import io.github.lucascmc1.gymflow.member.MemberService;
import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/check-ins")
public class CheckInController {

    private final CheckInService checkInService;
    private final MemberService memberService;

    public CheckInController(CheckInService checkInService, MemberService memberService) {
        this.checkInService = checkInService;
        this.memberService = memberService;
    }

    @GetMapping
    String list(Model model) {
        model.addAttribute("checkIns", checkInService.findRecent());
        model.addAttribute("members", memberService.findActive());
        model.addAttribute("checkInForm", new CheckInForm());
        return "checkins/list";
    }

    @PostMapping
    String register(@Valid @ModelAttribute CheckInForm checkInForm, BindingResult bindingResult,
                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selecione um aluno.");
            return "redirect:/check-ins";
        }
        try {
            CheckIn checkIn = checkInService.register(checkInForm.getMemberId());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Entrada liberada para " + checkIn.getMember().getName() + ".");
        } catch (BusinessRuleException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/check-ins";
    }
}
