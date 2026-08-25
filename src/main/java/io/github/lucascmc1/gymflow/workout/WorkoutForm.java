package io.github.lucascmc1.gymflow.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorkoutForm {

    @NotNull(message = "Selecione um aluno")
    private Long memberId;

    @NotBlank(message = "Informe o nome do treino")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Informe o objetivo")
    @Size(max = 120)
    private String goal;

    @NotBlank(message = "Informe o responsável")
    @Size(max = 100)
    private String coach;

    private boolean active = true;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
