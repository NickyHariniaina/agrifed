package hei.student.agrifed.validator;

import org.springframework.stereotype.Component;

import hei.student.agrifed.entity.Frequency;
import hei.student.agrifed.entity.dto.CreateMembershipFeeDto;
import hei.student.agrifed.exception.BadRequestException;

@Component
public class CollectivityValidator {

    public void validateMembershipFees(CreateMembershipFeeDto createMembershipFeeDto) {
        if (createMembershipFeeDto.getAmount() < 0) {
            throw new BadRequestException("Amount must be positive.");
        }
        if (createMembershipFeeDto.getFrequency() == null
                || (createMembershipFeeDto.getFrequency() != Frequency.PUNCTUALLY
                        && createMembershipFeeDto.getFrequency() != Frequency.WEEKLY
                        && createMembershipFeeDto.getFrequency() != Frequency.MONTHLY
                        && createMembershipFeeDto.getFrequency() != Frequency.ANNUALLY)) {
            throw new BadRequestException("Frequency must be one of : WEEKLY, MONTHLY, ANNUALLY, PUNCTUALLY.");
        }
    }
}
