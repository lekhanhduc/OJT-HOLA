package vn.khanhduc.springbootexercise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import vn.khanhduc.springbootexercise.dto.request.SignInRequest;
import vn.khanhduc.springbootexercise.dto.response.SignInResponse;
import vn.khanhduc.springbootexercise.entity.Account;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private SignInRequest signInRequest;
    private Account account;

    @BeforeEach
    void setUp() {
        // Chỉ khởi tạo các đối tượng cơ bản
        signInRequest = SignInRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        account = Account.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .build();
    }

    // Test cho login()
    @Test
    void login_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(account);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())))
                .thenReturn(authentication);
        when(jwtService.generateAccessToken(account)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(account)).thenReturn("refreshToken");

        // Act
        SignInResponse response = authenticationService.login(signInRequest);

        // Assert
        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        // Verify
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        verify(jwtService, times(1)).generateAccessToken(account);
        verify(jwtService, times(1)).generateRefreshToken(account);
    }

    @Test
    void login_BadCredentials_ThrowsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authenticationService.login(signInRequest));
        assertEquals("Invalid credentials", exception.getMessage());

        // Verify
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateAccessToken(any());
        verify(jwtService, never()).generateRefreshToken(any());
    }

}