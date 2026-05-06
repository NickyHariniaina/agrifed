package hei.student.agrifed.entity.dto;

import hei.student.agrifed.entity.enums.AttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityMemberAttendanceDto {
    private String id;
    private MemberDescriptionDto memberDescription;
    private AttendanceStatus attendanceStatus;
}
