package vn.huythanh.springbootexercise.dto.response;

import lombok.*;
import vn.huythanh.springbootexercise.common.CardStatus;
import vn.huythanh.springbootexercise.common.CardType;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDetailResponse {
    private Long id;
    private String cardId;
    private String cardNumber;
    private String cvv;
    private String customerName;
    private CardType cardType;
    private LocalDate expiryDate;
    private CardStatus status;
}
