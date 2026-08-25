package io.github.lucascmc1.gymflow.plan;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findAllByOrderByMonthlyPriceAsc();

    List<Plan> findByActiveTrueOrderByMonthlyPriceAsc();

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
