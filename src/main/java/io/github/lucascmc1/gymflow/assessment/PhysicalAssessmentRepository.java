package io.github.lucascmc1.gymflow.assessment;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalAssessmentRepository extends JpaRepository<PhysicalAssessment, Long> {

    @EntityGraph(attributePaths = "member")
    List<PhysicalAssessment> findAllByOrderByAssessmentDateDesc();

    @EntityGraph(attributePaths = "member")
    List<PhysicalAssessment> findByMemberIdOrderByAssessmentDateDesc(Long memberId);
}
