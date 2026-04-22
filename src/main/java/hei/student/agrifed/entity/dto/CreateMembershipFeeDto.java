package hei.student.agrifed.entity.dto;

import java.time.LocalDate;

import hei.student.agrifed.entity.ActivityStatus;
import hei.student.agrifed.entity.Frequency;
import hei.student.agrifed.entity.MembershipFee;
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
    private Frequency frequency;


    public MembershipFee toMembershipFee() {
        return MembershipFee.builder()
                .eligibleFrom(eligibleFrom)
                .frequency(frequency)
                .amount(amount)
                .label(label)
                .status(ActivityStatus.ACTIVE)
                .build();
    }
}
