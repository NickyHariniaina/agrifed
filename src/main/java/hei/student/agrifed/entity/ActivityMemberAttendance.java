package hei.student.agrifed.entity;

import hei.student.agrifed.entity.enums.AttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActivityMemberAttendance {
    private String id;
    private String idActivity;
    private String idMember;
    private AttendanceStatus attendanceStatus;
}
