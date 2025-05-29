package vn.huythanh.springbootexercise.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.huythanh.springbootexercise.common.CardStatus;
import vn.huythanh.springbootexercise.common.TransactionStatus;
import vn.huythanh.springbootexercise.dto.request.CardCreationRequest;
import vn.huythanh.springbootexercise.dto.request.CardDeleteRequest;
import vn.huythanh.springbootexercise.dto.response.CardCreationResponse;
import vn.huythanh.springbootexercise.dto.response.CardDetailResponse;
import vn.huythanh.springbootexercise.entity.Account;
import vn.huythanh.springbootexercise.entity.Card;
import vn.huythanh.springbootexercise.entity.Transaction;
import vn.huythanh.springbootexercise.repository.AccountRepository;
import vn.huythanh.springbootexercise.repository.CardRepository;
import vn.huythanh.springbootexercise.repository.TransactionRepository;
import vn.huythanh.springbootexercise.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CARD-SERVICE")
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
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

    public List<CardDetailResponse> getCartByUserLogin() {
        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        List<Card> myCart = cardRepository.findByAccountId(userId);

        return myCart.stream()
                .map(card -> CardDetailResponse.builder()
                        .id(card.getId())
                        .cardId(card.getCardId())
                        .cardNumber(card.getCardNumber())
                        .cvv(card.getCvv())
                        .cardType(card.getCardType())
                        .expiryDate(card.getExpiryDate())
                        .status(card.getStatus())
                        .customerName(card.getAccount().getCustomerName())
                        .build()).toList();
    }

    public void deleteCard(CardDeleteRequest request) {
        var userId = SecurityUtils.getUserLogin();
        if(userId == null) {
            throw new RuntimeException("Unauthenticated");
        }
        Card card = cardRepository.findByAccountIdAndCardId(userId, request.getId())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        List<Transaction> transactions = transactionRepository.findByCardAndStatus(card, TransactionStatus.PENDING);
        if (!transactions.isEmpty()) {
            throw new IllegalStateException("The card cannot be removed because there is a pending transaction.");
        }
        cardRepository.delete(card);
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
