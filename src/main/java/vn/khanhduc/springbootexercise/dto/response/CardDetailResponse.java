package vn.khanhduc.springbootexercise.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.khanhduc.springbootexercise.common.CardStatus;
import vn.khanhduc.springbootexercise.common.CardType;
import java.time.LocalDate;

@Getter
@Setter
@Builder
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
