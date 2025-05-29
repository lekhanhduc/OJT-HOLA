package vn.huythanh.springbootexercise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.huythanh.springbootexercise.dto.request.BalanceUpdateRequest;
import vn.huythanh.springbootexercise.dto.response.BalanceDetailResponse;
import vn.huythanh.springbootexercise.dto.response.BalanceUpdateResponse;
import vn.huythanh.springbootexercise.entity.Account;
import vn.huythanh.springbootexercise.entity.Balance;
import vn.huythanh.springbootexercise.repository.AccountRepository;
import vn.huythanh.springbootexercise.repository.BalanceRepository;
import vn.huythanh.springbootexercise.utils.SecurityUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BalanceService balanceService;

    private Account account;
    private Balance balance;

    @BeforeEach
    public void setUp() {
        account = Account.builder()
                .id(1L)
                .email("test@example.com")
                .phone("0123456789")
                .customerName("Test User")
                .build();

        balance = Balance.builder()
                .id(1L)
                .account(account)
                .availableBalance(new BigDecimal("1000000"))
                .holdBalance(new BigDecimal("200000"))
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    // Test cho phương thức getBalance
    @Test
    public void testGetBalance_Success() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(1L);

            when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(balance));

            BalanceDetailResponse response = balanceService.getBalance();

            assertNotNull(response);
            assertEquals(new BigDecimal("1000000"), response.getAvailableBalance());
            assertEquals(new BigDecimal("200000"), response.getHoldBalance());
            assertEquals(new BigDecimal("1200000"), response.getTotalBalance());
        }
    }

    @Test
    public void testGetBalance_Unauthenticated_ThrowsException() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(null);

            // Kiểm tra ngoại lệ
            RuntimeException exception = assertThrows(RuntimeException.class, () -> balanceService.getBalance());
            assertEquals("Unauthenticated", exception.getMessage());
        }
    }

    @Test
    public void testGetBalance_BalanceNotFound_ThrowsException() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(1L);

            // Giả lập balanceRepository trả về empty
            when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.empty());

            // Kiểm tra ngoại lệ
            RuntimeException exception = assertThrows(RuntimeException.class, () -> balanceService.getBalance());
            assertEquals("Balance not found", exception.getMessage());
        }
    }

    // Test cho phương thức deposit
    @Test
    public void testDeposit_Success() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(1L);

            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
            when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(balance));
            when(balanceRepository.save(any(Balance.class))).thenReturn(balance);

            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(new BigDecimal("500000"));

            BalanceUpdateResponse response = balanceService.deposit(request);

            assertNotNull(response);
            assertEquals(new BigDecimal("1500000"), balance.getAvailableBalance());
            assertEquals(new BigDecimal("200000"), balance.getHoldBalance());
            assertEquals(1L, response.getAccountId());
        }
    }

    @Test
    public void testDeposit_Unauthenticated_ThrowsException() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(null);

            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(new BigDecimal("500000"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> balanceService.deposit(request));
            assertEquals("Unauthenticated", exception.getMessage());
        }
    }

    @Test
    public void testDeposit_InvalidAmount_ThrowsException() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(1L);

            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(new BigDecimal("-500"));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> balanceService.deposit(request));
            assertEquals("The amount added must be greater than 0.", exception.getMessage());
        }
    }

    @Test
    public void testDeposit_AccountNotFound_ThrowsException() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(1L);

            when(accountRepository.findById(1L)).thenReturn(Optional.empty());

            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(new BigDecimal("500000"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> balanceService.deposit(request));
            assertEquals("Account not found", exception.getMessage());
        }
    }

    @Test
    public void testWithdraw_Success() {
        // Mock phương thức tĩnh SecurityUtils.getUserLogin()
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(1L);

            // Giả lập accountRepository và balanceRepository
            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
            when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(balance));
            when(balanceRepository.save(any(Balance.class))).thenReturn(balance);

            // Tạo request
            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(new BigDecimal("300000"));

            // Gọi phương thức
            BalanceUpdateResponse response = balanceService.withdraw(request);

            // Kiểm tra kết quả
            assertNotNull(response);
            assertEquals(new BigDecimal("700000"), balance.getAvailableBalance());
            assertEquals(new BigDecimal("200000"), balance.getHoldBalance());
            assertEquals(1L, response.getAccountId());
        }
    }

    @Test
    public void testWithdraw_Unauthenticated_ThrowsException() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(null);

            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(new BigDecimal("300000"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> balanceService.withdraw(request));
            assertEquals("Unauthenticated", exception.getMessage());
        }
    }

    @Test
    public void testWithdraw_InvalidAmount_ThrowsException() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(1L);

            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(new BigDecimal("0"));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> balanceService.withdraw(request));
            assertEquals("Withdrawal amount must be greater than 0.", exception.getMessage());
        }
    }

    @Test
    public void testWithdraw_InsufficientBalance_ThrowsException() {
        try (var mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserLogin).thenReturn(1L);

            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
            when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(balance));

            BalanceUpdateRequest request = new BalanceUpdateRequest();
            request.setAmount(new BigDecimal("1500000"));

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> balanceService.withdraw(request));
            assertEquals("Available balance is not enough to make the transaction.", exception.getMessage());
        }
    }
}