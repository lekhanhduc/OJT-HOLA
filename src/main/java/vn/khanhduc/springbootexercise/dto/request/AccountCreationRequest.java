package vn.khanhduc.springbootexercise.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountCreationRequest {
    private String email;
    private String phone;
    private String password;
    private String customerName;
}
