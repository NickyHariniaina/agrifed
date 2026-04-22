package hei.student.agrifed.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCollectivityStructureDto {
    private String president;      // MemberIdentifier
    private String vicePresident;  // MemberIdentifier
    private String treasurer;      // MemberIdentifier
    private String secretary;      // MemberIdentifier
}
