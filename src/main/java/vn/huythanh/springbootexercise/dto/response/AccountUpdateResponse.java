package vn.huythanh.springbootexercise.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateResponse {
    private String email;
    private String phone;
    private String customerName;
}
