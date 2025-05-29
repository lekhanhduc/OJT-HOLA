package vn.huythanh.springbootexercise.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.huythanh.springbootexercise.dto.request.AccountCreationRequest;
import vn.huythanh.springbootexercise.dto.request.AccountUpdateRequest;
import vn.huythanh.springbootexercise.dto.response.AccountCreationResponse;
import vn.huythanh.springbootexercise.dto.response.AccountDetailResponse;
import vn.huythanh.springbootexercise.dto.response.AccountUpdateResponse;
import vn.huythanh.springbootexercise.entity.Account;
import vn.huythanh.springbootexercise.entity.Balance;
import vn.huythanh.springbootexercise.mapper.AccountMapper;
import vn.huythanh.springbootexercise.repository.AccountRepository;
import vn.huythanh.springbootexercise.repository.BalanceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ACCOUNT-SERVICE")
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final BalanceRepository balanceRepository;

    public AccountCreationResponse createUser(AccountCreationRequest request) {
        Optional<Account> byEmail = accountRepository.findByEmail(request.getEmail());
        if(byEmail.isPresent()) {
            throw new RuntimeException("Email existed");
        }
        Account account = AccountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        accountRepository.save(account);
        log.info("Created account {}", account.getId());

        Balance balance = Balance.builder()
                .account(account)
                .availableBalance(BigDecimal.ZERO)
                .holdBalance(BigDecimal.ZERO)
                .lastUpdated(LocalDateTime.now())
                .build();

        balanceRepository.save(balance);

        log.info("Create balance record for ID account: {}", account.getId());

        return AccountMapper.toAccountCreationResponse(account);
    }

    @CacheEvict(
            value = "accountCache",
            key = "T(vn.huythanh.springbootexercise.utils.SecurityUtils).getUserLogin()")
    public AccountUpdateResponse updateAccount(AccountUpdateRequest request) {
        log.info("Updating account");

        Optional<String> principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty()) throw new RuntimeException("Unauthenticated");

        Long userId = Long.parseLong(principal.get());

        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if(request.getEmail() != null && !request.getEmail().isEmpty()) {
            if(accountRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
                throw new RuntimeException("Email existed");
            }
            account.setEmail(request.getEmail());
        }

       if(request.getPhone() != null && !request.getPhone().isEmpty()) {
           if(accountRepository.existsByPhoneAndIdNot(request.getPhone(), userId)) {
               throw new RuntimeException("Phone existed");
           }
           account.setPhone(request.getPhone());
       }
       accountRepository.save(account);

       return AccountUpdateResponse.builder()
                .email(account.getEmail())
                .phone(account.getPhone())
                .customerName(account.getCustomerName())
                .build();
    }

    @Cacheable(value = "accountCache", key = "T(vn.huythanh.springbootexercise.utils.SecurityUtils).getUserLogin()")
    public AccountDetailResponse info() {
        log.info("Get account details");

        Optional<String> principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty()) throw new RuntimeException("Unauthenticated");

        Long userId = Long.parseLong(principal.get());

        return accountRepository.findById(userId)
                .map(account -> AccountDetailResponse.builder()
                        .id(account.getId())
                        .email(account.getEmail())
                        .phone(account.getPhone())
                        .customerName(account.getCustomerName())
                        .createdAt(account.getCreatedAt())
                        .build())
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

}
