package vn.khanhduc.springbootexercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.springbootexercise.dto.request.AccountCreationRequest;
import vn.khanhduc.springbootexercise.dto.request.AccountUpdateRequest;
import vn.khanhduc.springbootexercise.dto.response.AccountCreationResponse;
import vn.khanhduc.springbootexercise.dto.response.AccountDetailResponse;
import vn.khanhduc.springbootexercise.dto.response.AccountUpdateResponse;
import vn.khanhduc.springbootexercise.dto.response.ApiResponse;
import vn.khanhduc.springbootexercise.service.AccountService;

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

}
