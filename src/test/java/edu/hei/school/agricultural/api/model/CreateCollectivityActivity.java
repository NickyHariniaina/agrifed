package edu.hei.school.agricultural.api.model;

import java.time.LocalDate;
import java.util.List;

public class CreateCollectivityActivity {
    public String label;
    public ActivityType activityType;
    public LocalDate executiveDate;
    public MonthlyRecurrenceRule recurrenceRule;
    public List<MemberOccupation> memberOccupationConcerned;
}
