package vn.huythanh.springbootexercise.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationRequest {
    private String email;
    private String phone;
    private String password;
    private String customerName;
}
