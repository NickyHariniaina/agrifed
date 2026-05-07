package edu.hei.school.agricultural.api.model;

import java.time.LocalDate;
import java.util.List;

public class CollectivityActivity {
    public String id;
    public String label;
    public ActivityType activityType;
    public LocalDate executiveDate;
    public MonthlyRecurrenceRule recurrenceRule;
    public List<MemberOccupation> memberOccupationConcerned;

    @Override
    public String toString() {
        return "CollectivityActivity{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", activityType=" + activityType +
                ", executiveDate=" + executiveDate +
                ", recurrenceRule=" + recurrenceRule +
                ", memberOccupationConcerned=" + memberOccupationConcerned +
                '}';
    }
}
