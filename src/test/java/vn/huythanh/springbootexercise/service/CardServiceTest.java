package vn.huythanh.springbootexercise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.huythanh.springbootexercise.common.CardStatus;
import vn.huythanh.springbootexercise.common.CardType;
import vn.huythanh.springbootexercise.dto.request.CardCreationRequest;
import vn.huythanh.springbootexercise.dto.response.CardCreationResponse;
import vn.huythanh.springbootexercise.entity.Account;
import vn.huythanh.springbootexercise.entity.Card;
import vn.huythanh.springbootexercise.repository.AccountRepository;
import vn.huythanh.springbootexercise.repository.CardRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CardService cardService;

    private Account account;
    private CardCreationRequest request;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .customerName("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        request = CardCreationRequest.builder()
                .cardType(CardType.VISA)
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createCard_success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Card savedCard = Card.builder()
                .id(1L)
                .cardId("uuid-123")
                .cardNumber("1234567890123456")
                .cvv("123")
                .cardType(CardType.VISA)
                .expiryDate(LocalDate.now().plusYears(5))
                .status(CardStatus.ACTIVE)
                .account(account)
                .build();
        when(cardRepository.save(any(Card.class))).thenReturn(savedCard);

        CardCreationResponse response = cardService.createCard(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("1234567890123456", response.getCardNumber());
        assertEquals("123", response.getCvv());
        assertEquals(CardType.VISA, response.getCardType());
        assertEquals(CardStatus.ACTIVE, response.getStatus());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals(LocalDate.now().plusYears(5), response.getExpiryDate());
        verify(accountRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_unauthenticated_throwsException() {
        when(securityContext.getAuthentication()).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardService.createCard(request));
        assertEquals("Unauthenticated", exception.getMessage());
        verify(accountRepository, never()).findById(any());
        verify(cardRepository, never()).save(any());
    }

    @Test
    void createCard_accountNotFound_throwsException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1");
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardService.createCard(request));
        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
        verify(cardRepository, never()).save(any());
    }

    @Test
    void createCard_generatesValidCardNumber() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Card savedCard = Card.builder()
                .id(1L)
                .cardId("uuid-123")
                .cardNumber("1234567890123456")
                .cvv("123")
                .cardType(CardType.VISA)
                .expiryDate(LocalDate.now().plusYears(5))
                .status(CardStatus.ACTIVE)
                .account(account)
                .build();
        when(cardRepository.save(any(Card.class))).thenReturn(savedCard);

        CardCreationResponse response = cardService.createCard(request);

        assertNotNull(response.getCardNumber());
        assertEquals(16, response.getCardNumber().length());
        assertTrue(response.getCardNumber().matches("\\d{16}"));
        verify(accountRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_generatesValidCvv() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Card savedCard = Card.builder()
                .id(1L)
                .cardId("uuid-123")
                .cardNumber("1234567890123456")
                .cvv("123")
                .cardType(CardType.VISA)
                .expiryDate(LocalDate.now().plusYears(5))
                .status(CardStatus.ACTIVE)
                .account(account)
                .build();
        when(cardRepository.save(any(Card.class))).thenReturn(savedCard);

        CardCreationResponse response = cardService.createCard(request);

        assertNotNull(response.getCvv());
        assertEquals(3, response.getCvv().length());
        assertTrue(response.getCvv().matches("\\d{3}"));
        verify(accountRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_setsCorrectExpiryDate() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Card savedCard = Card.builder()
                .id(1L)
                .cardId("uuid-123")
                .cardNumber("1234567890123456")
                .cvv("123")
                .cardType(CardType.VISA)
                .expiryDate(LocalDate.now().plusYears(5))
                .status(CardStatus.ACTIVE)
                .account(account)
                .build();
        when(cardRepository.save(any(Card.class))).thenReturn(savedCard);

        CardCreationResponse response = cardService.createCard(request);

        assertEquals(LocalDate.now().plusYears(5), response.getExpiryDate());
        verify(accountRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).save(any(Card.class));
    }
}