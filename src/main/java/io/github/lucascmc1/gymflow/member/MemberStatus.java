package io.github.lucascmc1.gymflow.member;

public enum MemberStatus {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    SUSPENDED("Suspenso");

    private final String label;

    MemberStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
