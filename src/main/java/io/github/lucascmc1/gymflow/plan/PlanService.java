package io.github.lucascmc1.gymflow.plan;

import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import io.github.lucascmc1.gymflow.shared.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public List<Plan> findAll() {
        return planRepository.findAllByOrderByMonthlyPriceAsc();
    }

    @Transactional(readOnly = true)
    public List<Plan> findActive() {
        return planRepository.findByActiveTrueOrderByMonthlyPriceAsc();
    }

    @Transactional(readOnly = true)
    public Plan findById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado"));
    }

    @Transactional
    public Plan create(PlanForm form) {
        validateName(form.getName(), null);
        Plan plan = new Plan(form.getName().trim(), form.getDescription().trim(),
                form.getMonthlyPrice(), form.getDurationMonths(), form.getFeatures().trim(),
                form.isActive());
        return planRepository.save(plan);
    }

    @Transactional
    public Plan update(Long id, PlanForm form) {
        Plan plan = findById(id);
        validateName(form.getName(), id);
        plan.setName(form.getName().trim());
        plan.setDescription(form.getDescription().trim());
        plan.setMonthlyPrice(form.getMonthlyPrice());
        plan.setDurationMonths(form.getDurationMonths());
        plan.setFeatures(form.getFeatures().trim());
        plan.setActive(form.isActive());
        return plan;
    }

    private void validateName(String name, Long currentId) {
        boolean duplicate = currentId == null
                ? planRepository.existsByNameIgnoreCase(name)
                : planRepository.existsByNameIgnoreCaseAndIdNot(name, currentId);
        if (duplicate) {
            throw new BusinessRuleException("Já existe um plano com este nome");
        }
    }
}
