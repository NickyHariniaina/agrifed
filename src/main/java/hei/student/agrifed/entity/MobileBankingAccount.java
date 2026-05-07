package hei.student.agrifed.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import hei.student.agrifed.entity.enums.MobileBankingService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
public class MobileBankingAccount implements FinancialAccount {
    private String id;
    private String holderName;
    private MobileBankingService mobileBankingService;
    private Integer mobileNumber;
    private Double amount;
}