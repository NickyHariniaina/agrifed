package hei.student.agrifed.entity.dto;

import hei.student.agrifed.entity.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberPaymentDto {
    private Integer    amount;
    private String      membershipFeeIdentifier;
    private String      accountCreditedIdentifier;
    private PaymentMode paymentMode;
}
