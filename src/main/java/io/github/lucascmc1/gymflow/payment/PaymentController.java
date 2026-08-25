package io.github.lucascmc1.gymflow.payment;

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
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    String list(@RequestParam(required = false) PaymentStatus status, Model model) {
        model.addAttribute("payments", paymentService.findAll(status));
        model.addAttribute("statuses", PaymentStatus.values());
        model.addAttribute("methods", PaymentMethod.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("paymentForm", new PaymentForm());
        return "payments/list";
    }

    @PostMapping("/{id}/pay")
    String markAsPaid(@PathVariable Long id, @Valid @ModelAttribute PaymentForm paymentForm,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Selecione a forma e a data de pagamento.");
            return "redirect:/payments";
        }
        try {
            paymentService.markAsPaid(id, paymentForm);
            redirectAttributes.addFlashAttribute("successMessage", "Pagamento confirmado.");
        } catch (BusinessRuleException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/payments";
    }
}
