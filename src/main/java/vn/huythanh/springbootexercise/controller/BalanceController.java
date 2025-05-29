package vn.huythanh.springbootexercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.huythanh.springbootexercise.dto.request.BalanceUpdateRequest;
import vn.huythanh.springbootexercise.dto.response.ApiResponse;
import vn.huythanh.springbootexercise.dto.response.BalanceDetailResponse;
import vn.huythanh.springbootexercise.dto.response.BalanceUpdateResponse;
import vn.huythanh.springbootexercise.service.BalanceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/balances")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    ApiResponse<BalanceDetailResponse> getBalance() {
        return ApiResponse.<BalanceDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .result(balanceService.getBalance())
                .build();
    }

    @PostMapping("/deposit")
    ApiResponse<BalanceUpdateResponse> deposit(@RequestBody BalanceUpdateRequest request) {
        return ApiResponse.<BalanceUpdateResponse>builder()
                .code(HttpStatus.OK.value())
                .result(balanceService.deposit(request))
                .build();
    }

    @PostMapping("/withdraw")
    ApiResponse<BalanceUpdateResponse> withdraw(@RequestBody BalanceUpdateRequest request) {
        return ApiResponse.<BalanceUpdateResponse>builder()
                .code(HttpStatus.OK.value())
                .result(balanceService.withdraw(request))
                .build();
    }

}
