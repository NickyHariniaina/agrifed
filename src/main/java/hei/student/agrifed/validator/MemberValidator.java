package hei.student.agrifed.validator;

import hei.student.agrifed.entity.Member;
import org.springframework.stereotype.Component;

import hei.student.agrifed.entity.dto.CreateMemberDto;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.repository.CollectivityRepository;
import hei.student.agrifed.repository.MemberRepository;

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

    public void checkReferees(CreateMemberDto createMemberDto, MemberRepository memberRepository) {
        boolean containsOneRefereeComingFromSameCollectivity = false;
        for (String referee : createMemberDto.getReferees()) {
            if (!memberRepository.existsById(referee)) {
                throw new NotFoundException("Member not found");
            } else {
                Member referees = memberRepository.findById(referee).get();
                if (referees.getCollectivityIdentifier().equals(createMemberDto.getCollectivityIdentifier())) {
                    containsOneRefereeComingFromSameCollectivity = true;
                }
            }
        }
        if (!containsOneRefereeComingFromSameCollectivity) {
            throw new BadRequestException("At least one Referees must come from the same collectivity");
        }
    }

    public void checkCollectivity(CreateMemberDto createMemberDto, CollectivityRepository collectivityRepository) {
        if (!collectivityRepository.existsById(createMemberDto.getCollectivityIdentifier())) {
            throw new NotFoundException("Collectivity not found");
        }
    }
}
