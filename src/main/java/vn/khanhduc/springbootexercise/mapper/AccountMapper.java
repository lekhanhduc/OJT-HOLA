package vn.khanhduc.springbootexercise.mapper;

import vn.khanhduc.springbootexercise.dto.request.AccountCreationRequest;
import vn.khanhduc.springbootexercise.dto.response.AccountCreationResponse;
import vn.khanhduc.springbootexercise.entity.Account;

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
