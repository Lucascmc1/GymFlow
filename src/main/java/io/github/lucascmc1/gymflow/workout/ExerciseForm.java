package io.github.lucascmc1.gymflow.workout;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExerciseForm {

    @NotBlank(message = "Informe o exercício")
    @Size(max = 120)
    private String name;

    @NotBlank(message = "Informe o grupo muscular")
    @Size(max = 80)
    private String muscleGroup;

    @NotNull(message = "Informe a quantidade de séries")
    @Min(1)
    @Max(20)
    private Integer sets = 3;

    @NotBlank(message = "Informe as repetições")
    @Size(max = 30)
    private String repetitions = "10-12";

    @NotNull(message = "Informe o descanso")
    @Min(0)
    @Max(600)
    private Integer restSeconds = 60;

    @Size(max = 400)
    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public String getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(String repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(Integer restSeconds) {
        this.restSeconds = restSeconds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
