package vn.khanhduc.springbootexercise.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountUpdateResponse {
    private String email;
    private String phone;
    private String customerName;
}
