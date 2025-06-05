package vn.huythanh.springbootexercise.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDeleteRequest {
    private String cardId;
}
