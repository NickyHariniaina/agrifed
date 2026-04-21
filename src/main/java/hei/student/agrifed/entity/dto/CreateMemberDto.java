package hei.student.agrifed.entity.dto;

import java.time.LocalDate;

import hei.student.agrifed.entity.Gender;
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
    private String[] referees;
    private Boolean registrationFeePaid;
    private Boolean membershipDuesPaid;
}
