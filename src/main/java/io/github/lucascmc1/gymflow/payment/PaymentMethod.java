package io.github.lucascmc1.gymflow.payment;

public enum PaymentMethod {
    PIX("Pix"),
    CREDIT_CARD("Cartão de crédito"),
    DEBIT_CARD("Cartão de débito"),
    CASH("Dinheiro"),
    BANK_SLIP("Boleto");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
