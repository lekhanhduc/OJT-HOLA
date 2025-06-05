package vn.huythanh.springbootexercise.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.huythanh.springbootexercise.dto.request.BalanceUpdateRequest;
import vn.huythanh.springbootexercise.dto.response.BalanceDetailResponse;
import vn.huythanh.springbootexercise.dto.response.BalanceUpdateResponse;
import vn.huythanh.springbootexercise.entity.Account;
import vn.huythanh.springbootexercise.entity.Balance;
import vn.huythanh.springbootexercise.mapper.BalanceMapper;
import vn.huythanh.springbootexercise.repository.AccountRepository;
import vn.huythanh.springbootexercise.repository.BalanceRepository;
import vn.huythanh.springbootexercise.utils.SecurityUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BALANCE-SERVICE")
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;

    @CacheEvict(value = "accountCache", key = "T(vn.huythanh.springbootexercise.utils.SecurityUtils).getUserLogin()")
    public BalanceDetailResponse getBalance() {
        log.info("Get balance");
        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Balance balance = balanceRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Balance not found"));

        return BalanceDetailResponse.builder()
                .availableBalance(balance.getAvailableBalance())
                .holdBalance(balance.getHoldBalance())
                .totalBalance(balance.getTotalBalance())
                .build();
    }


    @Transactional
    @CacheEvict(value = "accountCache", key = "T(vn.huythanh.springbootexercise.utils.SecurityUtils).getUserLogin()")
    public BalanceUpdateResponse deposit(BalanceUpdateRequest request) {

        log.info("Deposit");

        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount added must be greater than 0.");
        }

        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Balance balance = balanceRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Balance not found"));

        balance.setAvailableBalance(balance.getAvailableBalance().add(request.getAmount()));
        balance.setLastUpdated(LocalDateTime.now());

        balance = balanceRepository.save(balance);

        return BalanceMapper.toBalanceUpdateResponse(balance);
    }

    @Transactional
    @CacheEvict(value = "accountCache", key = "T(vn.huythanh.springbootexercise.utils.SecurityUtils).getUserLogin()")
    public BalanceUpdateResponse withdraw(BalanceUpdateRequest request) {

        log.info("Withdraw");

        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0.");
        }

        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Balance balance = balanceRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Balance not found"));

        if (balance.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalStateException("Available balance is not enough to make the transaction.");
        }

        balance.setAvailableBalance(balance.getAvailableBalance().subtract(request.getAmount()));
        balance.setLastUpdated(LocalDateTime.now());

        balance = balanceRepository.save(balance);

        return BalanceMapper.toBalanceUpdateResponse(balance);
    }

}
