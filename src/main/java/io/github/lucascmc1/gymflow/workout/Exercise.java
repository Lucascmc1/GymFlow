package io.github.lucascmc1.gymflow.workout;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "muscle_group", nullable = false, length = 80)
    private String muscleGroup;

    @Column(name = "set_count", nullable = false)
    private Integer sets;

    @Column(nullable = false, length = 30)
    private String repetitions;

    @Column(name = "rest_seconds", nullable = false)
    private Integer restSeconds;

    @Column(length = 400)
    private String notes;

    @Column(nullable = false)
    private Integer position;

    protected Exercise() {
    }

    public Exercise(String name, String muscleGroup, Integer sets, String repetitions,
                    Integer restSeconds, String notes, Integer position) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
        this.repetitions = repetitions;
        this.restSeconds = restSeconds;
        this.notes = notes;
        this.position = position;
    }

    void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public Long getId() {
        return id;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public String getName() {
        return name;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public Integer getSets() {
        return sets;
    }

    public String getRepetitions() {
        return repetitions;
    }

    public Integer getRestSeconds() {
        return restSeconds;
    }

    public String getNotes() {
        return notes;
    }

    public Integer getPosition() {
        return position;
    }
}
