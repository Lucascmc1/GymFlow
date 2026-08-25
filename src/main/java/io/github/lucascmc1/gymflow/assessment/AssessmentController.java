package io.github.lucascmc1.gymflow.assessment;

import io.github.lucascmc1.gymflow.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final MemberService memberService;

    public AssessmentController(AssessmentService assessmentService, MemberService memberService) {
        this.assessmentService = assessmentService;
        this.memberService = memberService;
    }

    @GetMapping
    String list(Model model) {
        model.addAttribute("assessments", assessmentService.findAll());
        return "assessments/list";
    }

    @GetMapping("/new")
    String createForm(@RequestParam(required = false) Long memberId, Model model) {
        AssessmentForm form = new AssessmentForm();
        form.setMemberId(memberId);
        populateForm(model, form);
        return "assessments/form";
    }

    @PostMapping
    String create(@Valid @ModelAttribute AssessmentForm assessmentForm,
                  BindingResult bindingResult, Model model,
                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateForm(model, assessmentForm);
            return "assessments/form";
        }
        assessmentService.create(assessmentForm);
        redirectAttributes.addFlashAttribute("successMessage", "Avaliação física registrada.");
        return "redirect:/assessments";
    }

    private void populateForm(Model model, AssessmentForm assessmentForm) {
        model.addAttribute("assessmentForm", assessmentForm);
        model.addAttribute("members", memberService.findActive());
    }
}
