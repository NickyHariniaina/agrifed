package hei.student.agrifed.entity.dto;

import java.time.LocalDate;
import java.util.List;

import hei.student.agrifed.entity.CollectivityActivity;
import hei.student.agrifed.entity.MonthlyRecurrenceRule;
import hei.student.agrifed.entity.enums.ActivityType;
import hei.student.agrifed.entity.enums.MemberOccupation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCollectivityActivityDto {
    private String label;
    private ActivityType activityType;
    private LocalDate executiveDate;
    private MonthlyRecurrenceRule recurrenceRule;
    private List<MemberOccupation> memberOccupationConcerned;

    public CollectivityActivity toCollectivityActivity() {
        return CollectivityActivity.builder()
                .label(label)
                .activityType(activityType)
                .executiveDate(executiveDate)
                .recurrenceRule(recurrenceRule)
                .memberOccupationConcerned(memberOccupationConcerned)
                .build();
    }
}
