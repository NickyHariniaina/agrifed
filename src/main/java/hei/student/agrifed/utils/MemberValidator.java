package hei.student.agrifed.utils;

import org.springframework.stereotype.Component;

import hei.student.agrifed.entity.dto.CreateMemberDto;
import hei.student.agrifed.exception.BadRequestException;

@Component
public class MemberValidator {

    public void checkCreateMemberDto(CreateMemberDto createMemberDto) {
        if (!createMemberDto.getMembershipDuesPaid()) {
            throw new BadRequestException("Membership dues paid must be greater than 200000.00");
        }
        if (!createMemberDto.getRegistrationFeePaid()) {
            throw new BadRequestException("Registration fee paid must be greater than 50000.00");
        }
        if (createMemberDto.getReferees().size() < 1) {
            throw new BadRequestException("Referees must be at least 2");
        }
    }
}
