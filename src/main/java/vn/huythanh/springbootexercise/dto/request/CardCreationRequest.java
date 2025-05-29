package vn.huythanh.springbootexercise.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.huythanh.springbootexercise.common.CardType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardCreationRequest {
    @NotNull(message = "Card type is required")
    private CardType cardType;
}
