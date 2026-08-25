package io.github.lucascmc1.gymflow.member;

import io.github.lucascmc1.gymflow.shared.BusinessRuleException;
import io.github.lucascmc1.gymflow.shared.CpfUtils;
import io.github.lucascmc1.gymflow.shared.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<Member> search(String query, MemberStatus status) {
        String normalizedQuery = StringUtils.hasText(query) ? query.trim() : null;
        return memberRepository.search(normalizedQuery, status);
    }

    @Transactional(readOnly = true)
    public List<Member> findActive() {
        return memberRepository.findByStatusOrderByNameAsc(MemberStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));
    }

    @Transactional
    public Member create(MemberForm form) {
        validateUniqueFields(form, null);
        Member member = new Member();
        copy(form, member);
        return memberRepository.save(member);
    }

    @Transactional
    public Member update(Long id, MemberForm form) {
        Member member = findById(id);
        validateUniqueFields(form, id);
        copy(form, member);
        return member;
    }

    @Transactional
    public void toggleStatus(Long id) {
        Member member = findById(id);
        member.setStatus(member.getStatus() == MemberStatus.ACTIVE
                ? MemberStatus.INACTIVE : MemberStatus.ACTIVE);
    }

    private void validateUniqueFields(MemberForm form, Long currentId) {
        if (!CpfUtils.isValid(form.getCpf())) {
            throw new BusinessRuleException("O CPF informado não é válido");
        }
        String cpf = CpfUtils.format(form.getCpf());
        boolean duplicateCpf = currentId == null
                ? memberRepository.existsByCpf(cpf)
                : memberRepository.existsByCpfAndIdNot(cpf, currentId);
        if (duplicateCpf) {
            throw new BusinessRuleException("Já existe um aluno cadastrado com este CPF");
        }
        boolean duplicateEmail = currentId == null
                ? memberRepository.existsByEmailIgnoreCase(form.getEmail())
                : memberRepository.existsByEmailIgnoreCaseAndIdNot(form.getEmail(), currentId);
        if (duplicateEmail) {
            throw new BusinessRuleException("Já existe um aluno cadastrado com este e-mail");
        }
    }

    private void copy(MemberForm form, Member member) {
        member.setName(form.getName().trim());
        member.setCpf(CpfUtils.format(form.getCpf()));
        member.setEmail(form.getEmail().trim().toLowerCase());
        member.setPhone(form.getPhone().trim());
        member.setBirthDate(form.getBirthDate());
        member.setJoinDate(form.getJoinDate());
        member.setStatus(form.getStatus());
        member.setGender(form.getGender());
        member.setEmergencyContact(form.getEmergencyContact());
        member.setNotes(form.getNotes());
    }
}
