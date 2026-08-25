package io.github.lucascmc1.gymflow.checkin;

import jakarta.validation.constraints.NotNull;

public class CheckInForm {

    @NotNull(message = "Selecione um aluno")
    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
