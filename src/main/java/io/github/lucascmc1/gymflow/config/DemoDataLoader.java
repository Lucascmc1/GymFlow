package io.github.lucascmc1.gymflow.config;

import io.github.lucascmc1.gymflow.assessment.PhysicalAssessment;
import io.github.lucascmc1.gymflow.assessment.PhysicalAssessmentRepository;
import io.github.lucascmc1.gymflow.checkin.CheckIn;
import io.github.lucascmc1.gymflow.checkin.CheckInRepository;
import io.github.lucascmc1.gymflow.member.Member;
import io.github.lucascmc1.gymflow.member.MemberRepository;
import io.github.lucascmc1.gymflow.member.MemberStatus;
import io.github.lucascmc1.gymflow.membership.Membership;
import io.github.lucascmc1.gymflow.membership.MembershipRepository;
import io.github.lucascmc1.gymflow.membership.MembershipStatus;
import io.github.lucascmc1.gymflow.payment.Payment;
import io.github.lucascmc1.gymflow.payment.PaymentMethod;
import io.github.lucascmc1.gymflow.payment.PaymentRepository;
import io.github.lucascmc1.gymflow.payment.PaymentStatus;
import io.github.lucascmc1.gymflow.plan.Plan;
import io.github.lucascmc1.gymflow.plan.PlanRepository;
import io.github.lucascmc1.gymflow.workout.Exercise;
import io.github.lucascmc1.gymflow.workout.WorkoutPlan;
import io.github.lucascmc1.gymflow.workout.WorkoutPlanRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "app.demo-data", havingValue = "true", matchIfMissing = true)
public class DemoDataLoader implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;
    private final MembershipRepository membershipRepository;
    private final PaymentRepository paymentRepository;
    private final CheckInRepository checkInRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final PhysicalAssessmentRepository assessmentRepository;

    public DemoDataLoader(MemberRepository memberRepository, PlanRepository planRepository,
                          MembershipRepository membershipRepository,
                          PaymentRepository paymentRepository,
                          CheckInRepository checkInRepository,
                          WorkoutPlanRepository workoutPlanRepository,
                          PhysicalAssessmentRepository assessmentRepository) {
        this.memberRepository = memberRepository;
        this.planRepository = planRepository;
        this.membershipRepository = membershipRepository;
        this.paymentRepository = paymentRepository;
        this.checkInRepository = checkInRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (memberRepository.count() > 0) {
            return;
        }

        List<Plan> plans = createPlans();
        List<Member> members = createMembers();
        List<Membership> memberships = createMemberships(members, plans);
        createPayments(memberships);
        createCheckIns(members);
        createWorkouts(members);
        createAssessments(members);
    }

    private List<Plan> createPlans() {
        return planRepository.saveAll(List.of(
                new Plan("Start", "Para quem busca uma rotina de treino simples e flexível.",
                        new BigDecimal("99.90"), 1,
                        "Acesso à musculação\nAvaliação inicial\nAplicativo de treinos", true),
                new Plan("Evolve", "Acompanhamento completo para evolução consistente.",
                        new BigDecimal("149.90"), 6,
                        "Musculação e aulas coletivas\nAvaliação trimestral\nTreino personalizado",
                        true),
                new Plan("Performance", "Estrutura premium para metas de alta performance.",
                        new BigDecimal("219.90"), 12,
                        "Acesso total\nAvaliação mensal\nConsultoria esportiva\nÁrea de recuperação",
                        true)));
    }

    private List<Member> createMembers() {
        String[][] data = {
                {"Marina Alves", "000.000.001-91", "marina.alves@example.com", "1997-03-12"},
                {"Rafael Moura", "000.000.002-72", "rafael.moura@example.com", "1992-08-21"},
                {"Camila Ribeiro", "000.000.003-53", "camila.ribeiro@example.com", "2000-11-05"},
                {"Bruno Ferreira", "000.000.004-34", "bruno.ferreira@example.com", "1989-01-28"},
                {"Ana Beatriz Lima", "000.000.005-15", "ana.lima@example.com", "1995-06-17"},
                {"Diego Nascimento", "000.000.006-04", "diego.nascimento@example.com", "1998-04-09"},
                {"Juliana Santos", "000.000.007-87", "juliana.santos@example.com", "1994-12-03"},
                {"Pedro Henrique", "000.000.008-68", "pedro.henrique@example.com", "2001-07-30"},
                {"Larissa Freitas", "000.000.009-49", "larissa.freitas@example.com", "1999-10-14"},
                {"Gustavo Rocha", "000.000.010-82", "gustavo.rocha@example.com", "1991-02-25"},
                {"Renata Melo", "000.000.011-63", "renata.melo@example.com", "1987-09-18"},
                {"Thiago Costa", "000.000.012-44", "thiago.costa@example.com", "1996-05-11"}
        };
        List<Member> members = new ArrayList<>();
        for (int index = 0; index < data.length; index++) {
            MemberStatus status = index == 10 ? MemberStatus.INACTIVE
                    : index == 11 ? MemberStatus.SUSPENDED : MemberStatus.ACTIVE;
            Member member = new Member(data[index][0], data[index][1], data[index][2],
                    "(84) 9" + String.format("%04d-%04d", 8100 + index, 1200 + index),
                    LocalDate.parse(data[index][3]), LocalDate.now().minusMonths(8 - index % 7),
                    status);
            member.setEmergencyContact("Contato de emergência · (84) 99999-00"
                    + String.format("%02d", index));
            member.setGender(index % 2 == 0 ? "Feminino" : "Masculino");
            members.add(member);
        }
        return memberRepository.saveAll(members);
    }

    private List<Membership> createMemberships(List<Member> members, List<Plan> plans) {
        List<Membership> memberships = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            Plan plan = index == 8 ? plans.get(0) : plans.get(index % 2 + 1);
            LocalDate start = index == 8 ? LocalDate.now().withDayOfMonth(5)
                    : LocalDate.now().minusMonths(3).withDayOfMonth(5 + index);
            BigDecimal discount = index % 4 == 0 ? new BigDecimal("10.00") : BigDecimal.ZERO;
            BigDecimal monthlyPrice = plan.getMonthlyPrice()
                    .multiply(BigDecimal.ONE.subtract(discount.movePointLeft(2)))
                    .setScale(2);
            memberships.add(new Membership(members.get(index), plan, start,
                    start.plusMonths(plan.getDurationMonths()), MembershipStatus.ACTIVE,
                    monthlyPrice, discount));
        }
        return membershipRepository.saveAll(memberships);
    }

    private void createPayments(List<Membership> memberships) {
        YearMonth current = YearMonth.now();
        for (int memberIndex = 0; memberIndex < memberships.size(); memberIndex++) {
            Membership membership = memberships.get(memberIndex);
            int historyMonths = membership.getPlan().getDurationMonths() == 1 ? 1 : 4;
            for (int offset = historyMonths - 1; offset >= 0; offset--) {
                YearMonth reference = current.minusMonths(offset);
                int dueDay = Math.min(5 + memberIndex, reference.lengthOfMonth());
                Payment payment = new Payment(membership, reference.atDay(1),
                        reference.atDay(dueDay), membership.getMonthlyPrice(), PaymentStatus.PENDING);
                boolean leaveOpen = offset == 0 && memberIndex >= 7;
                if (!leaveOpen) {
                    PaymentMethod method = PaymentMethod.values()[memberIndex
                            % PaymentMethod.values().length];
                    payment.markAsPaid(method, reference.atDay(Math.min(dueDay,
                            reference.lengthOfMonth())));
                }
                paymentRepository.save(payment);
            }
        }
        paymentRepository.updateOverdue(LocalDate.now());
    }

    private void createCheckIns(List<Member> members) {
        LocalDate today = LocalDate.now();
        int[] hours = {6, 6, 8, 10, 12, 16, 18, 18, 18, 20};
        for (int index = 0; index < 10; index++) {
            checkInRepository.save(new CheckIn(members.get(index),
                    today.atTime(hours[index], 7 + index * 3)));
        }
        for (int day = 1; day <= 4; day++) {
            for (int index = 0; index < 7; index++) {
                checkInRepository.save(new CheckIn(members.get(index),
                        today.minusDays(day).atTime(7 + index * 2, 10 + day)));
            }
        }
    }

    private void createWorkouts(List<Member> members) {
        WorkoutPlan strength = new WorkoutPlan(members.get(0), "Treino A · Força",
                "Ganho de força e massa muscular", "Carlos Mendes", true);
        strength.addExercise(new Exercise("Agachamento livre", "Quadríceps", 4, "6-8", 120,
                "Aumentar carga apenas com execução estável", 1));
        strength.addExercise(new Exercise("Supino reto", "Peitoral", 4, "8-10", 90,
                "Escápulas retraídas", 2));
        strength.addExercise(new Exercise("Remada curvada", "Costas", 3, "10-12", 90,
                null, 3));
        strength.addExercise(new Exercise("Desenvolvimento", "Ombros", 3, "10", 60,
                null, 4));

        WorkoutPlan conditioning = new WorkoutPlan(members.get(1), "Condicionamento",
                "Resistência cardiovascular", "Fernanda Lopes", true);
        conditioning.addExercise(new Exercise("Bike intervalada", "Cardiorrespiratório", 8,
                "30s forte / 60s leve", 0, "Aquecimento de 5 minutos", 1));
        conditioning.addExercise(new Exercise("Kettlebell swing", "Corpo inteiro", 4, "15",
                45, null, 2));
        conditioning.addExercise(new Exercise("Remo ergométrico", "Cardiorrespiratório", 5,
                "250 m", 60, null, 3));

        WorkoutPlan mobility = new WorkoutPlan(members.get(2), "Mobilidade e core",
                "Estabilidade e amplitude de movimento", "Carlos Mendes", true);
        mobility.addExercise(new Exercise("Prancha frontal", "Core", 4, "40s", 30,
                null, 1));
        mobility.addExercise(new Exercise("Dead bug", "Core", 3, "12 por lado", 30,
                null, 2));
        mobility.addExercise(new Exercise("Mobilidade de quadril", "Mobilidade", 3, "45s",
                20, null, 3));
        workoutPlanRepository.saveAll(List.of(strength, conditioning, mobility));
    }

    private void createAssessments(List<Member> members) {
        PhysicalAssessment first = new PhysicalAssessment(members.get(0),
                LocalDate.now().minusMonths(3), new BigDecimal("68.40"), new BigDecimal("1.68"));
        first.setBodyFatPercentage(new BigDecimal("24.10"));
        first.setMuscleMass(new BigDecimal("46.20"));
        first.setWaist(new BigDecimal("76.50"));

        PhysicalAssessment second = new PhysicalAssessment(members.get(0), LocalDate.now(),
                new BigDecimal("66.80"), new BigDecimal("1.68"));
        second.setBodyFatPercentage(new BigDecimal("21.70"));
        second.setMuscleMass(new BigDecimal("47.10"));
        second.setWaist(new BigDecimal("73.80"));

        PhysicalAssessment third = new PhysicalAssessment(members.get(1),
                LocalDate.now().minusDays(12), new BigDecimal("84.20"), new BigDecimal("1.81"));
        third.setBodyFatPercentage(new BigDecimal("17.30"));
        third.setMuscleMass(new BigDecimal("64.50"));

        PhysicalAssessment fourth = new PhysicalAssessment(members.get(2),
                LocalDate.now().minusDays(5), new BigDecimal("59.60"), new BigDecimal("1.63"));
        fourth.setBodyFatPercentage(new BigDecimal("22.80"));
        fourth.setMuscleMass(new BigDecimal("41.30"));
        assessmentRepository.saveAll(List.of(first, second, third, fourth));
    }
}
