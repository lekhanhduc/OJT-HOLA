package vn.huythanh.springbootexercise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.huythanh.springbootexercise.dto.request.AccountCreationRequest;
import vn.huythanh.springbootexercise.dto.response.AccountCreationResponse;
import vn.huythanh.springbootexercise.entity.Account;
import vn.huythanh.springbootexercise.repository.AccountRepository;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    private AccountCreationRequest request;

    @BeforeEach
    void setUp() {
        request = AccountCreationRequest.builder()
                .email("test@example.com")
                .customerName("Test User")
                .password("123456")
                .phone("123456789")
                .build();
    }

    @Test
    void createUser_Success() {
        when(accountRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty()); // Email chưa tồn tại
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword"); // Mã hóa mật khẩu
        Account savedAccount = Account.builder()
                .id(1L)
                .email(request.getEmail())
                .customerName(request.getCustomerName())
                .phone(request.getPhone())
                .password("encodedPassword")
                .build();
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount); // Giả lập lưu tài khoản

        // Act: Gọi phương thức cần test
        AccountCreationResponse response = accountService.createUser(request);

        // Assert: Kiểm tra kết quả
        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getCustomerName(), response.getCustomerName());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(savedAccount.getCreatedAt(), response.getCreatedAt());

        verify(accountRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createUser_EmailExisted_ThrowsException() {
        // Arrange: Giả lập email đã tồn tại
        Account existingAccount = Account.builder()
                .id(1L)
                .email(request.getEmail())
                .customerName("Existing User")
                .password("encodedPassword")
                .build();
        when(accountRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingAccount));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.createUser(request));
        assertEquals("Email existed", exception.getMessage());

        verify(accountRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(accountRepository, never()).save(any(Account.class));
    }
}
