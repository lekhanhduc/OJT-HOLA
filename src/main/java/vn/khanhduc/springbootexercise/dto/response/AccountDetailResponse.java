package vn.khanhduc.springbootexercise.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AccountDetailResponse {
    private Long id;
    private String email;
    private String phone;
    private String customerName;

    @JsonFormat(pattern = "dd:MM:yyyy hh:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime createdAt;
}
