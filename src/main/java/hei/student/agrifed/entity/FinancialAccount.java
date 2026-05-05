package hei.student.agrifed.entity;

import hei.student.agrifed.entity.enums.Bank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialAccount {
    private String id;
    private String  accountType;
    private Double  amount;

    private String             holderName;
    private MobileBankingService mobileBankingService;
    private Long               mobileNumber;

    private Bank bankName;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Long    bankAccountNumber;
    private Integer bankAccountKey;
}
