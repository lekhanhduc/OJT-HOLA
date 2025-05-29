package vn.khanhduc.springbootexercise.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.khanhduc.springbootexercise.dto.request.BalanceUpdateRequest;
import vn.khanhduc.springbootexercise.dto.response.BalanceDetailResponse;
import vn.khanhduc.springbootexercise.dto.response.BalanceUpdateResponse;
import vn.khanhduc.springbootexercise.entity.Balance;
import vn.khanhduc.springbootexercise.mapper.BalanceMapper;
import vn.khanhduc.springbootexercise.repository.AccountRepository;
import vn.khanhduc.springbootexercise.repository.BalanceRepository;
import vn.khanhduc.springbootexercise.utils.SecurityUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BALANCE-SERVICE")
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;

    public BalanceDetailResponse getBalance() {
        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        var balance = balanceRepository.findByAccountId(userId)
                .orElseThrow(() -> new RuntimeException("Balance not found"));

        return BalanceDetailResponse.builder()
                .availableBalance(balance.getAvailableBalance())
                .holdBalance(balance.getHoldBalance())
                .totalBalance(balance.getTotalBalance())
                .build();
    }


    @Transactional
    public BalanceUpdateResponse deposit(BalanceUpdateRequest request) {

        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount added must be greater than 0.");
        }

        accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Balance balance = balanceRepository.findByAccountId(userId)
                .orElseThrow(() -> new RuntimeException("Balance not found"));

        balance.setAvailableBalance(balance.getAvailableBalance().add(request.getAmount()));
        balance.setLastUpdated(LocalDateTime.now());

        balance = balanceRepository.save(balance);

        return BalanceMapper.toBalanceUpdateResponse(balance);
    }

    @Transactional
    public BalanceUpdateResponse withdraw(BalanceUpdateRequest request) {

        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0.");
        }

        accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Balance balance = balanceRepository.findByAccountId(userId)
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
