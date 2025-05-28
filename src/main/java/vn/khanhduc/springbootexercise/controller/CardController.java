package vn.khanhduc.springbootexercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.springbootexercise.dto.request.CardCreationRequest;
import vn.khanhduc.springbootexercise.dto.response.ApiResponse;
import vn.khanhduc.springbootexercise.dto.response.CardCreationResponse;
import vn.khanhduc.springbootexercise.service.CardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    ApiResponse<CardCreationResponse> createCard(@RequestBody CardCreationRequest request) {
        return ApiResponse.<CardCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Card created successfully")
                .result(cardService.createCard(request))
                .build();
    }


}
