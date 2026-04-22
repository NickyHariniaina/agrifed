package hei.student.agrifed.entity;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private Integer phoneNumber;
    private String email;
    private MemberOccupation occupation;
    private List<String> referees;
    private LocalDate joinedAt;
    private String collectivityIdentifier;
}
