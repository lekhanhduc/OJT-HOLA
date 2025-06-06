package vn.huythanh.springbootexercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.huythanh.springbootexercise.dto.response.ApiResponse;
import vn.huythanh.springbootexercise.dto.response.PageResponse;
import vn.huythanh.springbootexercise.dto.response.TransactionHistoryResponse;
import vn.huythanh.springbootexercise.service.TransactionService;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/api/v1/transactions")
    ApiResponse<PageResponse<TransactionHistoryResponse>> getHistory() {
        return ApiResponse.<PageResponse<TransactionHistoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(transactionService.getTransactionHistory())
                .build();
    }

}
