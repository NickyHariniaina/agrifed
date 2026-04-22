package hei.student.agrifed.service;

import hei.student.agrifed.entity.MemberPayment;
import hei.student.agrifed.entity.dto.CreateMemberPaymentDto;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.repository.MemberPaymentRepository;
import hei.student.agrifed.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberPaymentService {
    private final MemberPaymentRepository memberPaymentRepository;
    private final MemberRepository memberRepository;

    public MemberPaymentService(MemberPaymentRepository memberPaymentRepository,
                                MemberRepository memberRepository) {
        this.memberPaymentRepository = memberPaymentRepository;
        this.memberRepository        = memberRepository;
    }


    private MemberPayment createOne(Integer memberId, CreateMemberPaymentDto dto) {
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than 0.");
        }
        if (dto.getPaymentMode() == null) {
            throw new BadRequestException("paymentMode is required.");
        }

        Integer membershipFeeId = Integer.parseInt(dto.getMembershipFeeIdentifier());
        Integer accountId       = Integer.parseInt(dto.getAccountCreditedIdentifier());

        Integer collectivityId = memberPaymentRepository
                .findCollectivityIdByMembershipFee(membershipFeeId)
                .orElseThrow(() -> new NotFoundException(
                        "MembershipFee not found with id : " + membershipFeeId));

        memberPaymentRepository.findFinancialAccount(accountId)
                .orElseThrow(() -> new NotFoundException(
                        "FinancialAccount not found with id : " + accountId));

        return memberPaymentRepository.save(
                memberId, membershipFeeId, accountId, collectivityId,
                dto.getAmount(), dto.getPaymentMode());
    }
}
