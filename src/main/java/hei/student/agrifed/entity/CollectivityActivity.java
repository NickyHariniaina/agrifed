package hei.student.agrifed.entity;

import java.time.LocalDate;
import java.util.List;

import hei.student.agrifed.entity.enums.ActivityType;
import hei.student.agrifed.entity.enums.DayOfWeek;
import hei.student.agrifed.entity.enums.MemberOccupation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollectivityActivity {
    private String id;
    private String idCollectivity;
    private String label;
    private ActivityType activityType;
    private LocalDate executiveDate;
    private Integer recurrenceWeekOrdinal;
    private DayOfWeek recurrenceDayOfWeek;
    private List<MemberOccupation> memberOccupationConcerned;
}
