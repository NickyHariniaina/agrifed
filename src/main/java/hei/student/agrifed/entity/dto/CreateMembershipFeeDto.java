package hei.student.agrifed.entity.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMembershipFeeDto {

    private LocalDate eligibleFrom;
    private Double amount;
    private String label;

}
