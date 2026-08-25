package io.github.lucascmc1.gymflow.payment;

import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import io.github.lucascmc1.gymflow.shared.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public List<Payment> findAll(PaymentStatus status) {
        paymentRepository.updateOverdue(LocalDate.now());
        return status == null
                ? paymentRepository.findAllByOrderByDueDateDesc()
                : paymentRepository.findByStatusOrderByDueDateDesc(status);
    }

    @Transactional(readOnly = true)
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança não encontrada"));
    }

    @Transactional
    public void markAsPaid(Long id, PaymentForm form) {
        Payment payment = findById(id);
        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new BusinessRuleException("Esta cobrança já foi paga");
        }
        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            throw new BusinessRuleException("Uma cobrança cancelada não pode ser recebida");
        }
        payment.markAsPaid(form.getMethod(), form.getPaidAt());
    }
}
