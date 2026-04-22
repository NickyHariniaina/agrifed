package hei.student.agrifed.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssignIdentityDto {
    private Integer federationNumber;
    private String name;

    public AssignIdentityDto() {}

}
