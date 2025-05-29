package vn.huythanh.springbootexercise.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceUpdateResponse {
    private Long accountId;
    private BigDecimal availableBalance;
    private BigDecimal holdBalance;
    private BigDecimal totalBalance;
    private LocalDateTime lastUpdated;
}
