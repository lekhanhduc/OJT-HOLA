package vn.khanhduc.springbootexercise.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.khanhduc.springbootexercise.common.CardType;

@Getter
@Setter
@Builder
public class CardCreationRequest {
    @NotNull(message = "Card type is required")
    private CardType cardType;
}
