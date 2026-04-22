package hei.student.agrifed.entity.dto;

import java.time.LocalDate;
import java.util.List;

import hei.student.agrifed.entity.Gender;
import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.MemberOccupation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMemberDto {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private Integer phoneNumber;
    private String email;
    private MemberOccupation occupation;
    private String collectivityIdentifier;
    private List<String> referees;
    private Boolean registrationFeePaid;
    private Boolean membershipDuesPaid;

    public Member toMember() {
        Member member = new Member();
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setBirthDate(birthDate);
        member.setGender(gender);
        member.setAddress(address);
        member.setPhoneNumber(phoneNumber);
        member.setProfession(profession);
        member.setEmail(email);
        member.setOccupation(occupation);
        member.setReferees(referees);
        member.setJoinedAt(LocalDate.now());
        return member;
    }
}
