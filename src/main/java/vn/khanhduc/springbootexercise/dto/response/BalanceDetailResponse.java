package vn.khanhduc.springbootexercise.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class BalanceDetailResponse {
    private BigDecimal availableBalance;
    private BigDecimal holdBalance;
    private BigDecimal totalBalance;
}
