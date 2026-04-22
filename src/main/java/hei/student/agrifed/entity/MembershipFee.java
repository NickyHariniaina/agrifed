package hei.student.agrifed.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MembershipFee {
    private Integer id;
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private Double amount;
    private String label;
    private ActivityStatus status;

}
