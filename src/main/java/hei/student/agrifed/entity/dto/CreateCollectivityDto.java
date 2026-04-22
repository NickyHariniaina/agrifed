package hei.student.agrifed.entity.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCollectivityDto {
    private String location;
    private List<String> members;
    private Boolean federationApproval;
    private CreateCollectivityStructureDto structure;
}
