package hei.student.agrifed.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import hei.student.agrifed.entity.enums.Bank;
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
public class BankAccount implements FinancialAccount {
    private String id;
    private String holderName;
    private Bank bankName;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Integer bankAccountNumber;
    private Integer bankAccountKey;
    private Double amount;
}