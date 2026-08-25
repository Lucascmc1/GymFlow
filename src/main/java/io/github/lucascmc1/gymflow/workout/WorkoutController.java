package io.github.lucascmc1.gymflow.workout;

import io.github.lucascmc1.gymflow.member.MemberService;
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
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final MemberService memberService;

    public WorkoutController(WorkoutService workoutService, MemberService memberService) {
        this.workoutService = workoutService;
        this.memberService = memberService;
    }

    @GetMapping
    String list(Model model) {
        model.addAttribute("workouts", workoutService.findAll());
        return "workouts/list";
    }

    @GetMapping("/new")
    String createForm(@RequestParam(required = false) Long memberId, Model model) {
        WorkoutForm form = new WorkoutForm();
        form.setMemberId(memberId);
        populateForm(model, form);
        return "workouts/form";
    }

    @PostMapping
    String create(@Valid @ModelAttribute WorkoutForm workoutForm, BindingResult bindingResult,
                  Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateForm(model, workoutForm);
            return "workouts/form";
        }
        WorkoutPlan workout = workoutService.create(workoutForm);
        redirectAttributes.addFlashAttribute("successMessage",
                "Treino criado. Adicione os exercícios da ficha.");
        return "redirect:/workouts/" + workout.getId();
    }

    @GetMapping("/{id}")
    String detail(@PathVariable Long id, Model model) {
        model.addAttribute("workout", workoutService.findDetailed(id));
        model.addAttribute("exerciseForm", new ExerciseForm());
        return "workouts/detail";
    }

    @PostMapping("/{id}/exercises")
    String addExercise(@PathVariable Long id,
                       @Valid @ModelAttribute ExerciseForm exerciseForm,
                       BindingResult bindingResult, Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("workout", workoutService.findDetailed(id));
            return "workouts/detail";
        }
        workoutService.addExercise(id, exerciseForm);
        redirectAttributes.addFlashAttribute("successMessage", "Exercício adicionado.");
        return "redirect:/workouts/" + id;
    }

    @PostMapping("/exercises/{exerciseId}/delete")
    String deleteExercise(@PathVariable Long exerciseId, RedirectAttributes redirectAttributes) {
        Long workoutId = workoutService.deleteExercise(exerciseId);
        redirectAttributes.addFlashAttribute("successMessage", "Exercício removido.");
        return "redirect:/workouts/" + workoutId;
    }

    private void populateForm(Model model, WorkoutForm workoutForm) {
        model.addAttribute("workoutForm", workoutForm);
        model.addAttribute("members", memberService.findActive());
    }
}
