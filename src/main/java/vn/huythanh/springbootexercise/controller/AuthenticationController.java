package vn.huythanh.springbootexercise.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.huythanh.springbootexercise.dto.request.RefreshTokenRequest;
import vn.huythanh.springbootexercise.dto.request.SignInRequest;
import vn.huythanh.springbootexercise.dto.response.ApiResponse;
import vn.huythanh.springbootexercise.dto.response.RefreshTokenResponse;
import vn.huythanh.springbootexercise.dto.response.SignInResponse;
import vn.huythanh.springbootexercise.service.AuthenticationService;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<SignInResponse> login(@RequestBody SignInRequest request) {
        return ApiResponse.<SignInResponse>builder()
                .code(HttpStatus.OK.value())
                .result(authenticationService.login(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestHeader("Authorization") String token) {
        try {
            authenticationService.logout(token.replace("Bearer ", ""));
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("logged out successfully")
                    .build();
        } catch (ParseException e) {
            log.error("Logout error");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException {
        return ApiResponse.<RefreshTokenResponse>builder()
                .code(HttpStatus.OK.value())
                .result(authenticationService.refreshToken(request))
                .build();
    }
    
}
