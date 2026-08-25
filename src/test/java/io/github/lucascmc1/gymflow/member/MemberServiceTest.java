package io.github.lucascmc1.gymflow.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    private MemberService memberService;
    private MemberForm form;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
        form = new MemberForm();
        form.setName("  Marina Alves  ");
        form.setCpf("00000000191");
        form.setEmail("MARINA@EXAMPLE.COM");
        form.setPhone("(84) 99999-9999");
        form.setBirthDate(LocalDate.of(1997, 3, 12));
        form.setJoinDate(LocalDate.now());
        form.setStatus(MemberStatus.ACTIVE);
    }

    @Test
    void createsMemberWithNormalizedFields() {
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member member = memberService.create(form);

        assertThat(member.getName()).isEqualTo("Marina Alves");
        assertThat(member.getCpf()).isEqualTo("000.000.001-91");
        assertThat(member.getEmail()).isEqualTo("marina@example.com");
    }

    @Test
    void rejectsInvalidCpf() {
        form.setCpf("111.111.111-11");

        assertThatThrownBy(() -> memberService.create(form))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("CPF");
    }
}
