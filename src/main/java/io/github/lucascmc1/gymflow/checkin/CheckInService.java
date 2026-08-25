package io.github.lucascmc1.gymflow.checkin;

import io.github.lucascmc1.gymflow.member.Member;
import io.github.lucascmc1.gymflow.member.MemberService;
import io.github.lucascmc1.gymflow.member.MemberStatus;
import io.github.lucascmc1.gymflow.membership.MembershipRepository;
import io.github.lucascmc1.gymflow.membership.MembershipStatus;
import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final MemberService memberService;
    private final MembershipRepository membershipRepository;

    public CheckInService(CheckInRepository checkInRepository, MemberService memberService,
                          MembershipRepository membershipRepository) {
        this.checkInRepository = checkInRepository;
        this.memberService = memberService;
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public List<CheckIn> findRecent() {
        return checkInRepository.findTop100ByOrderByCheckedInAtDesc();
    }

    @Transactional
    public CheckIn register(Long memberId) {
        Member member = memberService.findById(memberId);
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new BusinessRuleException("O cadastro deste aluno não está ativo");
        }
        if (!membershipRepository.existsByMemberIdAndStatus(memberId, MembershipStatus.ACTIVE)) {
            throw new BusinessRuleException("O aluno não possui uma matrícula ativa");
        }
        LocalDate today = LocalDate.now();
        if (checkInRepository.existsByMemberIdAndCheckedInAtBetween(memberId,
                today.atStartOfDay(), today.plusDays(1).atStartOfDay().minusNanos(1))) {
            throw new BusinessRuleException("Este aluno já realizou check-in hoje");
        }
        return checkInRepository.save(new CheckIn(member, LocalDateTime.now()));
    }
}
