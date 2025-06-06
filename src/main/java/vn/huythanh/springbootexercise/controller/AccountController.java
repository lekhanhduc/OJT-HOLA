package vn.huythanh.springbootexercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.huythanh.springbootexercise.dto.request.AccountCreationRequest;
import vn.huythanh.springbootexercise.dto.request.AccountUpdateRequest;
import vn.huythanh.springbootexercise.dto.response.AccountCreationResponse;
import vn.huythanh.springbootexercise.dto.response.AccountDetailResponse;
import vn.huythanh.springbootexercise.dto.response.AccountUpdateResponse;
import vn.huythanh.springbootexercise.dto.response.ApiResponse;
import vn.huythanh.springbootexercise.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    ApiResponse<AccountCreationResponse> registration(@RequestBody AccountCreationRequest request) {
        return ApiResponse.<AccountCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(accountService.createUser(request))
                .build();
    }

    @PutMapping
    ApiResponse<AccountUpdateResponse> update(@RequestBody AccountUpdateRequest request) {
        return ApiResponse.<AccountUpdateResponse>builder()
                .code(HttpStatus.OK.value())
                .result(accountService.updateAccount(request))
                .build();
    }

    @GetMapping
    ApiResponse<AccountDetailResponse> info() {
        return ApiResponse.<AccountDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .result(accountService.info())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Account deleted")
                .build();
    }

}
