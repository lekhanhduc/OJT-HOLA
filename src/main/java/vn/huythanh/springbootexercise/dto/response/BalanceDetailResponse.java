package vn.huythanh.springbootexercise.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDetailResponse {
    private BigDecimal availableBalance;
    private BigDecimal holdBalance;
    private BigDecimal totalBalance;
}
