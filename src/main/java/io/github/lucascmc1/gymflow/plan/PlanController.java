package io.github.lucascmc1.gymflow.plan;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    String list(Model model) {
        model.addAttribute("plans", planService.findAll());
        return "plans/list";
    }

    @GetMapping("/new")
    String createForm(Model model) {
        model.addAttribute("planForm", new PlanForm());
        model.addAttribute("formTitle", "Novo plano");
        return "plans/form";
    }

    @PostMapping
    String create(@Valid @ModelAttribute PlanForm planForm, BindingResult bindingResult,
                  Model model, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                planService.create(planForm);
                redirectAttributes.addFlashAttribute("successMessage", "Plano criado com sucesso.");
                return "redirect:/plans";
            } catch (BusinessRuleException exception) {
                bindingResult.reject("business", exception.getMessage());
            }
        }
        model.addAttribute("formTitle", "Novo plano");
        return "plans/form";
    }

    @GetMapping("/{id}/edit")
    String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("planForm", PlanForm.from(planService.findById(id)));
        model.addAttribute("planId", id);
        model.addAttribute("formTitle", "Editar plano");
        return "plans/form";
    }

    @PostMapping("/{id}")
    String update(@PathVariable Long id, @Valid @ModelAttribute PlanForm planForm,
                  BindingResult bindingResult, Model model,
                  RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                planService.update(id, planForm);
                redirectAttributes.addFlashAttribute("successMessage", "Plano atualizado.");
                return "redirect:/plans";
            } catch (BusinessRuleException exception) {
                bindingResult.reject("business", exception.getMessage());
            }
        }
        model.addAttribute("planId", id);
        model.addAttribute("formTitle", "Editar plano");
        return "plans/form";
    }
}
