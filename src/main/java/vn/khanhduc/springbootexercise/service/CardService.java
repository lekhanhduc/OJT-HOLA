package vn.khanhduc.springbootexercise.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.khanhduc.springbootexercise.dto.request.CardCreationRequest;
import vn.khanhduc.springbootexercise.dto.response.CardCreationResponse;
import vn.khanhduc.springbootexercise.repository.CardRepository;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CARD-SERVICE")
public class CardService {

    private final CardRepository cardRepository;

    public CardCreationResponse createCard(CardCreationRequest request) {
        return null;
    }
}
