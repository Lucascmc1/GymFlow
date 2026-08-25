package io.github.lucascmc1.gymflow.workout;

import io.github.lucascmc1.gymflow.member.Member;
import io.github.lucascmc1.gymflow.member.MemberService;
import io.github.lucascmc1.gymflow.shared.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkoutService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;
    private final MemberService memberService;

    public WorkoutService(WorkoutPlanRepository workoutPlanRepository,
                          ExerciseRepository exerciseRepository,
                          MemberService memberService) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public List<WorkoutPlan> findAll() {
        return workoutPlanRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Transactional(readOnly = true)
    public WorkoutPlan findDetailed(Long id) {
        return workoutPlanRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado"));
    }

    @Transactional
    public WorkoutPlan create(WorkoutForm form) {
        Member member = memberService.findById(form.getMemberId());
        WorkoutPlan plan = new WorkoutPlan(member, form.getTitle().trim(), form.getGoal().trim(),
                form.getCoach().trim(), form.isActive());
        return workoutPlanRepository.save(plan);
    }

    @Transactional
    public void addExercise(Long workoutId, ExerciseForm form) {
        WorkoutPlan workoutPlan = findDetailed(workoutId);
        Exercise exercise = new Exercise(form.getName().trim(), form.getMuscleGroup().trim(),
                form.getSets(), form.getRepetitions().trim(), form.getRestSeconds(),
                form.getNotes(), workoutPlan.getExercises().size() + 1);
        workoutPlan.addExercise(exercise);
    }

    @Transactional
    public Long deleteExercise(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercício não encontrado"));
        Long workoutId = exercise.getWorkoutPlan().getId();
        exerciseRepository.delete(exercise);
        return workoutId;
    }
}
