package hei.student.agrifed.entity.dto;

import hei.student.agrifed.entity.PaymentMode;

public class CreateMemberPaymentDto {
    private Double      amount;
    private String      membershipFeeIdentifier;
    private String      accountCreditedIdentifier;
    private PaymentMode paymentMode;
}
