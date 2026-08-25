package io.github.lucascmc1.gymflow.payment;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PaymentForm {

    @NotNull(message = "Selecione a forma de pagamento")
    private PaymentMethod method = PaymentMethod.PIX;

    @NotNull(message = "Informe a data do pagamento")
    private LocalDate paidAt = LocalDate.now();

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public LocalDate getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDate paidAt) {
        this.paidAt = paidAt;
    }
}
