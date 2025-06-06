package vn.huythanh.springbootexercise.mapper;

import vn.huythanh.springbootexercise.dto.response.TransactionHistoryResponse;
import vn.huythanh.springbootexercise.entity.Transaction;

public class TransactionMapper {

    private TransactionMapper() {}

    public static TransactionHistoryResponse toTransactionHistoryResponse(Transaction transaction) {
        return TransactionHistoryResponse.builder()
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .transactionDate(transaction.getCreatedAt())
                .completedAt(transaction.getCompletedAt())
                .build();
    }
}
