package hei.student.agrifed.entity.dto;

import hei.student.agrifed.entity.ActivityMemberAttendance;
import hei.student.agrifed.entity.enums.AttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateActivityMemberAttendanceDto {
    private String memberIdentifier;
    private AttendanceStatus attendanceStatus;

    public ActivityMemberAttendance toActivityMemberAttendance() {
        return ActivityMemberAttendance.builder()
                .idMember(memberIdentifier)
                .attendanceStatus(attendanceStatus)
                .build();
    }
}
