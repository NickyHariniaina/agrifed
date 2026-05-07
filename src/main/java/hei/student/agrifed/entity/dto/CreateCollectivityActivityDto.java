package hei.student.agrifed.entity.dto;

import java.time.LocalDate;
import java.util.List;

import hei.student.agrifed.entity.CollectivityActivity;
import hei.student.agrifed.entity.enums.ActivityType;
import hei.student.agrifed.entity.enums.DayOfWeek;
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
    private Integer recurrenceWeekOrdinal;
    private DayOfWeek recurrenceDayOfWeek;
    private List<MemberOccupation> memberOccupationConcerned;

    public CollectivityActivity toCollectivityActivity() {
        return CollectivityActivity.builder()
                .label(label)
                .activityType(activityType)
                .executiveDate(executiveDate)
                .recurrenceWeekOrdinal(recurrenceWeekOrdinal)
                .recurrenceDayOfWeek(recurrenceDayOfWeek)
                .memberOccupationConcerned(memberOccupationConcerned)
                .build();
    }
}
