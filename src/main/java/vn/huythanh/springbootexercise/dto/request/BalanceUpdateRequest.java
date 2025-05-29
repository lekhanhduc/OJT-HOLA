package vn.huythanh.springbootexercise.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BalanceUpdateRequest {
    private BigDecimal amount;
}
