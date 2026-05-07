package hei.student.agrifed.service;

import java.util.ArrayList;
import java.util.List;

import hei.student.agrifed.entity.ActivityMemberAttendance;
import hei.student.agrifed.entity.CollectivityActivity;
import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.dto.ActivityMemberAttendanceDto;
import hei.student.agrifed.entity.dto.CreateActivityMemberAttendanceDto;
import hei.student.agrifed.entity.dto.CreateCollectivityActivityDto;
import hei.student.agrifed.entity.enums.AttendanceStatus;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.repository.CollectivityActivityRepository;
import hei.student.agrifed.repository.CollectivityRepository;
import hei.student.agrifed.repository.MemberRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CollectivityActivityService {

    private final CollectivityActivityRepository collectivityActivityRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public List<CollectivityActivity> createActivities(String collectivityId, List<CreateCollectivityActivityDto> dtos) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found with ID : " + collectivityId);
        }

        List<CollectivityActivity> created = new ArrayList<>();
        for (CreateCollectivityActivityDto dto : dtos) {
            validateActivityDto(dto);
            CollectivityActivity activity = dto.toCollectivityActivity();
            created.add(collectivityActivityRepository.save(activity, collectivityId));
        }
        return created;
    }

    public List<CollectivityActivity> getActivities(String collectivityId) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found with ID : " + collectivityId);
        }
        return collectivityActivityRepository.findAllByCollectivityId(collectivityId);
    }

    public List<ActivityMemberAttendanceDto> recordAttendance(
            String collectivityId,
            String activityId,
            List<CreateActivityMemberAttendanceDto> dtos) {

        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found with ID : " + collectivityId);
        }

        if (!collectivityActivityRepository.existsByCollectivityAndActivity(collectivityId, activityId)) {
            throw new NotFoundException("Activity not found with ID : " + activityId);
        }

        List<ActivityMemberAttendanceDto> results = new ArrayList<>();
        for (CreateActivityMemberAttendanceDto dto : dtos) {
            Member member = memberRepository.findById(dto.getMemberIdentifier())
                    .orElseThrow(() -> new NotFoundException("Member not found with ID : " + dto.getMemberIdentifier()));

            AttendanceStatus currentStatus = collectivityActivityRepository
                    .findAttendanceStatus(activityId, dto.getMemberIdentifier())
                    .orElse(null);

            if (currentStatus == AttendanceStatus.ATTENDED || currentStatus == AttendanceStatus.MISSING) {
                throw new BadRequestException(
                        "Attendance for member " + member.getFirstName() + " " + member.getLastName()
                                + " is already confirmed and cannot be changed.");
            }

            ActivityMemberAttendance attendance = dto.toActivityMemberAttendance();
            attendance.setIdActivity(activityId);
            collectivityActivityRepository.saveAttendance(attendance, activityId);

            results.add(ActivityMemberAttendanceDto.builder()
                    .memberDescription(hei.student.agrifed.entity.dto.MemberDescriptionDto.builder()
                            .id(member.getId())
                            .firstName(member.getFirstName())
                            .lastName(member.getLastName())
                            .email(member.getEmail())
                            .occupation(member.getOccupation())
                            .build())
                    .attendanceStatus(dto.getAttendanceStatus())
                    .build());
        }
        return results;
    }

    public List<ActivityMemberAttendanceDto> getAttendance(String collectivityId, String activityId) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException("Collectivity not found with ID : " + collectivityId);
        }

        if (!collectivityActivityRepository.existsByCollectivityAndActivity(collectivityId, activityId)) {
            throw new NotFoundException("Activity not found with ID : " + activityId);
        }

        return collectivityActivityRepository.findAllAttendanceByActivityId(activityId);
    }

    private void validateActivityDto(CreateCollectivityActivityDto dto) {
        boolean hasExecutiveDate = dto.getExecutiveDate() != null;
        boolean hasRecurrence = dto.getRecurrenceRule() != null
                && (dto.getRecurrenceRule().getWeekOrdinal() != null || dto.getRecurrenceRule().getDayOfWeek() != null);

        if (hasExecutiveDate && hasRecurrence) {
            throw new BadRequestException(
                    "Both executive date and recurrence rule cannot be provided at the same time.");
        }
        if (!hasExecutiveDate && !hasRecurrence) {
            throw new BadRequestException(
                    "Either executive date or recurrence rule must be provided.");
        }
        if (hasRecurrence) {
            if (dto.getRecurrenceRule().getWeekOrdinal() == null || dto.getRecurrenceRule().getDayOfWeek() == null) {
                throw new BadRequestException(
                        "Both recurrence week ordinal and day of week must be provided together.");
            }
            if (dto.getRecurrenceRule().getWeekOrdinal() < 1 || dto.getRecurrenceRule().getWeekOrdinal() > 5) {
                throw new BadRequestException(
                        "Recurrence week ordinal must be between 1 and 5.");
            }
        }
    }
}
