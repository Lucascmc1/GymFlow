package io.github.lucascmc1.gymflow.checkin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.github.lucascmc1.gymflow.member.Member;
import io.github.lucascmc1.gymflow.member.MemberService;
import io.github.lucascmc1.gymflow.member.MemberStatus;
import io.github.lucascmc1.gymflow.membership.MembershipRepository;
import io.github.lucascmc1.gymflow.membership.MembershipStatus;
import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckInServiceTest {

    @Mock
    private CheckInRepository checkInRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private MembershipRepository membershipRepository;

    private CheckInService checkInService;
    private Member activeMember;

    @BeforeEach
    void setUp() {
        checkInService = new CheckInService(checkInRepository, memberService, membershipRepository);
        activeMember = new Member("Marina Alves", "000.000.001-91", "marina@example.com",
                "(84) 99999-9999", LocalDate.of(1997, 3, 12), LocalDate.now(),
                MemberStatus.ACTIVE);
    }

    @Test
    void registersEntryForActiveMembership() {
        when(memberService.findById(1L)).thenReturn(activeMember);
        when(membershipRepository.existsByMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(true);
        when(checkInRepository.save(any(CheckIn.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CheckIn checkIn = checkInService.register(1L);

        assertThat(checkIn.getMember()).isEqualTo(activeMember);
        assertThat(checkIn.getCheckedInAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void rejectsSecondEntryOnSameDay() {
        when(memberService.findById(1L)).thenReturn(activeMember);
        when(membershipRepository.existsByMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(true);
        when(checkInRepository.existsByMemberIdAndCheckedInAtBetween(any(), any(), any()))
                .thenReturn(true);

        assertThatThrownBy(() -> checkInService.register(1L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("já realizou check-in");
    }
}
