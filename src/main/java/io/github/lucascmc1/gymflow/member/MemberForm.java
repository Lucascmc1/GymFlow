package io.github.lucascmc1.gymflow.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class MemberForm {

    @NotBlank(message = "Informe o nome completo")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
    private String name;

    @NotBlank(message = "Informe o CPF")
    @Pattern(regexp = "(?:\\d{3}\\.?){2}\\d{3}-?\\d{2}", message = "Use um CPF com 11 dígitos")
    private String cpf;

    @NotBlank(message = "Informe o e-mail")
    @Email(message = "Informe um e-mail válido")
    private String email;

    @NotBlank(message = "Informe o telefone")
    @Size(max = 20, message = "O telefone deve ter até 20 caracteres")
    private String phone;

    @NotNull(message = "Informe a data de nascimento")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate birthDate;

    @NotNull(message = "Informe a data de cadastro")
    private LocalDate joinDate = LocalDate.now();

    @NotNull(message = "Selecione o status")
    private MemberStatus status = MemberStatus.ACTIVE;

    @Size(max = 30)
    private String gender;

    @Size(max = 120)
    private String emergencyContact;

    @Size(max = 1000)
    private String notes;

    public static MemberForm from(Member member) {
        MemberForm form = new MemberForm();
        form.name = member.getName();
        form.cpf = member.getCpf();
        form.email = member.getEmail();
        form.phone = member.getPhone();
        form.birthDate = member.getBirthDate();
        form.joinDate = member.getJoinDate();
        form.status = member.getStatus();
        form.gender = member.getGender();
        form.emergencyContact = member.getEmergencyContact();
        form.notes = member.getNotes();
        return form;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
