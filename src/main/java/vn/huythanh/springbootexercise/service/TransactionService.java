package vn.huythanh.springbootexercise.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.huythanh.springbootexercise.dto.response.PageResponse;
import vn.huythanh.springbootexercise.dto.response.TransactionHistoryResponse;
import vn.huythanh.springbootexercise.entity.Transaction;
import vn.huythanh.springbootexercise.repository.AccountRepository;
import vn.huythanh.springbootexercise.repository.TransactionRepository;
import vn.huythanh.springbootexercise.utils.SecurityUtils;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public PageResponse<TransactionHistoryResponse> getTransactionHistory() {

        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Unauthenticated"));

        Page<Transaction> transactions = transactionRepository.findByAccount(account, PageRequest.of(0, 10));

        List<TransactionHistoryResponse> responses = transactions.getContent()
                .stream().map(transaction -> TransactionHistoryResponse.builder()
                        .transactionId(transaction.getTransactionId())
                        .transactionType(transaction.getTransactionType())
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .status(transaction.getStatus())
                        .description(transaction.getDescription())
                        .transactionDate(transaction.getCreatedAt())
                        .completedAt(transaction.getCompletedAt())
                        .build()).toList();

        return PageResponse.<TransactionHistoryResponse>builder()
                .currentPage(transactions.getNumber())
                .pageSize(transactions.getSize())
                .totalPages(transactions.getTotalPages())
                .totalElements(transactions.getTotalElements())
                .data(responses)
                .build();
    }
}
