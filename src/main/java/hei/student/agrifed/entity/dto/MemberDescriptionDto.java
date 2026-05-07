package hei.student.agrifed.entity.dto;

import hei.student.agrifed.entity.enums.MemberOccupation;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDescriptionDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private MemberOccupation occupation;
}