package io.github.lucascmc1.gymflow.assessment;

import io.github.lucascmc1.gymflow.member.Member;
import io.github.lucascmc1.gymflow.member.MemberService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssessmentService {

    private final PhysicalAssessmentRepository assessmentRepository;
    private final MemberService memberService;

    public AssessmentService(PhysicalAssessmentRepository assessmentRepository,
                             MemberService memberService) {
        this.assessmentRepository = assessmentRepository;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public List<PhysicalAssessment> findAll() {
        return assessmentRepository.findAllByOrderByAssessmentDateDesc();
    }

    @Transactional
    public PhysicalAssessment create(AssessmentForm form) {
        Member member = memberService.findById(form.getMemberId());
        PhysicalAssessment assessment = new PhysicalAssessment(member, form.getAssessmentDate(),
                form.getWeight(), form.getHeight());
        assessment.setBodyFatPercentage(form.getBodyFatPercentage());
        assessment.setMuscleMass(form.getMuscleMass());
        assessment.setWaist(form.getWaist());
        assessment.setChest(form.getChest());
        assessment.setArm(form.getArm());
        assessment.setThigh(form.getThigh());
        assessment.setNotes(form.getNotes());
        return assessmentRepository.save(assessment);
    }
}
