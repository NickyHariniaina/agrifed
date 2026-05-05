package hei.student.agrifed.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectivityTransaction {
    private String          id;
    private LocalDate creationDate;
    private Double           amount;
    private PaymentMode      paymentMode;
    private FinancialAccount accountCredited;
    private Member           memberDebited;
}
