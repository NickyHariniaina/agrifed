package hei.student.agrifed.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import hei.student.agrifed.entity.ActivityMemberAttendance;
import hei.student.agrifed.entity.CollectivityActivity;
import hei.student.agrifed.entity.dto.ActivityMemberAttendanceDto;
import hei.student.agrifed.entity.dto.MemberDescriptionDto;
import hei.student.agrifed.entity.enums.ActivityType;
import hei.student.agrifed.entity.enums.AttendanceStatus;
import hei.student.agrifed.entity.enums.DayOfWeek;
import hei.student.agrifed.entity.enums.MemberOccupation;

import org.springframework.stereotype.Repository;

@Repository
public class CollectivityActivityRepository {

    private final Connection connection;

    public CollectivityActivityRepository(Connection connection) {
        this.connection = connection;
    }

    public CollectivityActivity save(CollectivityActivity activity, String collectivityId) {
        String sql = """
                INSERT INTO collectivity_activity (
                    id_collectivity, label, activity_type,
                    executive_date, recurrence_week_ordinal, recurrence_day_of_week,
                    member_occupation_concerned
                ) VALUES (?, ?, ?::activity_type, ?, ?, ?::day_of_week, ?)
                RETURNING id, id_collectivity, label, activity_type, executive_date,
                          recurrence_week_ordinal, recurrence_day_of_week, member_occupation_concerned
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, collectivityId);
            ps.setString(2, activity.getLabel());
            ps.setString(3, activity.getActivityType().name());
            if (activity.getExecutiveDate() != null) {
                ps.setDate(4, Date.valueOf(activity.getExecutiveDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            if (activity.getRecurrenceWeekOrdinal() != null) {
                ps.setInt(5, activity.getRecurrenceWeekOrdinal());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            if (activity.getRecurrenceDayOfWeek() != null) {
                ps.setString(6, activity.getRecurrenceDayOfWeek().name());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }
            if (activity.getMemberOccupationConcerned() != null && !activity.getMemberOccupationConcerned().isEmpty()) {
                String[] occupations = activity.getMemberOccupationConcerned().stream()
                        .map(MemberOccupation::name)
                        .toArray(String[]::new);
                Array array = connection.createArrayOf("occupation", occupations);
                ps.setArray(7, array);
            } else {
                ps.setNull(7, Types.ARRAY);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapActivity(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<CollectivityActivity> findById(String id) {
        String sql = """
                SELECT id, id_collectivity, label, activity_type, executive_date,
                       recurrence_week_ordinal, recurrence_day_of_week, member_occupation_concerned
                FROM collectivity_activity
                WHERE id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapActivity(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CollectivityActivity> findAllByCollectivityId(String collectivityId) {
        String sql = """
                SELECT id, id_collectivity, label, activity_type, executive_date,
                       recurrence_week_ordinal, recurrence_day_of_week, member_occupation_concerned
                FROM collectivity_activity
                WHERE id_collectivity = ?
                """;
        List<CollectivityActivity> activities = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                activities.add(mapActivity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return activities;
    }

    public boolean existsById(String id) {
        String sql = "SELECT 1 FROM collectivity_activity WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByCollectivityAndActivity(String collectivityId, String activityId) {
        String sql = "SELECT 1 FROM collectivity_activity WHERE id = ? AND id_collectivity = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, activityId);
            ps.setString(2, collectivityId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ActivityMemberAttendance saveAttendance(ActivityMemberAttendance attendance, String activityId) {
        String sql = """
INSERT INTO activity_member_attendance (id_activity, id_member, attendance_status)
                 VALUES (?, ?, ?::attendance_status)
                 ON CONFLICT (id_activity, id_member) DO UPDATE SET attendance_status = EXCLUDED.attendance_status
                 RETURNING id, id_activity, id_member, attendance_status
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, activityId);
            ps.setString(2, attendance.getIdMember());
            ps.setString(3, attendance.getAttendanceStatus().name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapAttendance(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ActivityMemberAttendanceDto> findAllAttendanceByActivityId(String activityId) {
        String sql = """
                SELECT a.id, a.attendance_status,
                       m.id as member_id, m.firstname, m.lastname, m.email, m.occupation
                FROM activity_member_attendance a
                JOIN member m ON a.id_member = m.id
                WHERE a.id_activity = ?
                """;
        List<ActivityMemberAttendanceDto> results = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MemberDescriptionDto memberDesc = MemberDescriptionDto.builder()
                        .id(rs.getString("member_id"))
                        .firstName(rs.getString("firstname"))
                        .lastName(rs.getString("lastname"))
                        .email(rs.getString("email"))
                        .occupation(rs.getString("occupation") != null
                                ? MemberOccupation.valueOf(rs.getString("occupation")) : null)
                        .build();

                ActivityMemberAttendanceDto dto = ActivityMemberAttendanceDto.builder()
                        .id(rs.getString("id"))
                        .memberDescription(memberDesc)
                        .attendanceStatus(AttendanceStatus.valueOf(rs.getString("attendance_status")))
                        .build();
                results.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public Optional<AttendanceStatus> findAttendanceStatus(String activityId, String memberId) {
        String sql = """
                SELECT attendance_status FROM activity_member_attendance
                WHERE id_activity = ? AND id_member = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, activityId);
            ps.setString(2, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(AttendanceStatus.valueOf(rs.getString("attendance_status")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CollectivityActivity mapActivity(ResultSet rs) throws SQLException {
        List<MemberOccupation> occupations = new ArrayList<>();
        Array occupationArray = rs.getArray("member_occupation_concerned");
        if (occupationArray != null) {
            String[] occupationStrings = (String[]) occupationArray.getArray();
            occupations = Arrays.stream(occupationStrings)
                    .map(MemberOccupation::valueOf)
                    .collect(Collectors.toList());
        }

        return CollectivityActivity.builder()
                .id(rs.getString("id"))
                .idCollectivity(rs.getString("id_collectivity"))
                .label(rs.getString("label"))
                .activityType(ActivityType.valueOf(rs.getString("activity_type")))
                .executiveDate(rs.getDate("executive_date") != null
                        ? rs.getDate("executive_date").toLocalDate() : null)
                .recurrenceWeekOrdinal(rs.getObject("recurrence_week_ordinal", Integer.class))
                .recurrenceDayOfWeek(rs.getString("recurrence_day_of_week") != null
                        ? DayOfWeek.valueOf(rs.getString("recurrence_day_of_week")) : null)
                .memberOccupationConcerned(occupations)
                .build();
    }

    private ActivityMemberAttendance mapAttendance(ResultSet rs) throws SQLException {
        return ActivityMemberAttendance.builder()
                .id(rs.getString("id"))
                .idActivity(rs.getString("id_activity"))
                .idMember(rs.getString("id_member"))
                .attendanceStatus(AttendanceStatus.valueOf(rs.getString("attendance_status")))
                .build();
    }
}
