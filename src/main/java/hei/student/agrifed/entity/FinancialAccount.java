package hei.student.agrifed.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
public abstract class FinancialAccount {
}