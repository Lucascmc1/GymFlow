package io.github.lucascmc1.gymflow.membership;

public enum MembershipStatus {
    ACTIVE("Ativa"),
    EXPIRED("Expirada"),
    CANCELLED("Cancelada"),
    PAUSED("Pausada");

    private final String label;

    MembershipStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
