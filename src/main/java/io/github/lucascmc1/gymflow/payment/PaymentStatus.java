package io.github.lucascmc1.gymflow.payment;

public enum PaymentStatus {
    PENDING("Pendente"),
    PAID("Pago"),
    OVERDUE("Atrasado"),
    CANCELLED("Cancelado");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
