package io.github.lucascmc1.gymflow.workout;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    @EntityGraph(attributePaths = "member")
    List<WorkoutPlan> findAllByOrderByUpdatedAtDesc();

    @EntityGraph(attributePaths = "member")
    List<WorkoutPlan> findByMemberIdOrderByUpdatedAtDesc(Long memberId);

    @EntityGraph(attributePaths = {"member", "exercises"})
    Optional<WorkoutPlan> findDetailedById(Long id);
}
