package vn.khanhduc.springbootexercise.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.khanhduc.springbootexercise.common.CardStatus;
import vn.khanhduc.springbootexercise.dto.request.CardCreationRequest;
import vn.khanhduc.springbootexercise.dto.response.CardCreationResponse;
import vn.khanhduc.springbootexercise.entity.Account;
import vn.khanhduc.springbootexercise.entity.Card;
import vn.khanhduc.springbootexercise.repository.AccountRepository;
import vn.khanhduc.springbootexercise.repository.CardRepository;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CARD-SERVICE")
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final Random random = new Random();

    public CardCreationResponse createCard(CardCreationRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthenticated");
        }

        Long userId = Long.parseLong(authentication.getName());

        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String cartId = UUID.randomUUID().toString();

        String cartNumber = generateCardNumber();
        String cvv = generateCvv();

        Card card = Card.builder()
                .cardId(cartId)
                .cardNumber(cartNumber)
                .cardType(request.getCardType())
                .cvv(cvv)
                .expiryDate(LocalDate.now().plusYears(5))
                .status(CardStatus.ACTIVE)
                .account(account)
                .build();

        card = cardRepository.save(card);

        return CardCreationResponse.builder()
                .id(card.getId())
                .cardId(card.getCardId())
                .cardNumber(card.getCardNumber())
                .cvv(card.getCvv())
                .cardType(card.getCardType())
                .expiryDate(card.getExpiryDate())
                .status(card.getStatus())
                .customerName(account.getCustomerName())
                .build();
    }

    private String generateCardNumber() {
        StringBuilder cartNumber = new StringBuilder();
        for(int i = 0; i <= 15; i++) {
            cartNumber.append(random.nextInt(10));
        }
        return cartNumber.toString();
    }

    private String generateCvv() {
        StringBuilder cvv = new StringBuilder();
        for(int i = 0; i <= 2; i++) {
            cvv.append(random.nextInt(10));
        }
        return cvv.toString();
    }
}
