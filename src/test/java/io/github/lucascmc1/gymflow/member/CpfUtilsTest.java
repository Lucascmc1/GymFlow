package io.github.lucascmc1.gymflow.member;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.lucascmc1.gymflow.shared.CpfUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CpfUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"000.000.001-91", "52998224725", "111.444.777-35"})
    void acceptsValidCpf(String cpf) {
        assertThat(CpfUtils.isValid(cpf)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "111.111.111-11", "123", "529.982.247-24"})
    void rejectsInvalidCpf(String cpf) {
        assertThat(CpfUtils.isValid(cpf)).isFalse();
    }
}
