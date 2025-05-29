package vn.huythanh.springbootexercise.mapper;

import vn.huythanh.springbootexercise.dto.request.AccountCreationRequest;
import vn.huythanh.springbootexercise.dto.response.AccountCreationResponse;
import vn.huythanh.springbootexercise.entity.Account;

public class AccountMapper {

    private AccountMapper() {}

    public static Account toAccount(AccountCreationRequest request) {
        return Account.builder()
                .email(request.getEmail())
                .customerName(request.getCustomerName())
                .phone(request.getPhone())
                .build();
    }

    public static AccountCreationResponse toAccountCreationResponse(Account account) {
        return AccountCreationResponse.builder()
                .email(account.getEmail())
                .customerName(account.getCustomerName())
                .phone(account.getPhone())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
