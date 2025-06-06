package vn.huythanh.springbootexercise.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.huythanh.springbootexercise.common.TransactionStatus;
import vn.huythanh.springbootexercise.common.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionHistoryResponse {
    private String transactionId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String currency;
    private TransactionStatus status;
    private String description;
    private LocalDateTime transactionDate;
    private LocalDateTime completedAt;
}
