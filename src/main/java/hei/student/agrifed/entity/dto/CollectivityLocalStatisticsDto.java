package hei.student.agrifed.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectivityLocalStatisticsDto {
    private MemberDescriptionDto memberDescription;
    private Double earnedAmount;
    private Double unpaidAmount;
}