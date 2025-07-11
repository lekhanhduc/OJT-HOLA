package vn.huythanh.springbootexercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.huythanh.springbootexercise.dto.request.CardCreationRequest;
import vn.huythanh.springbootexercise.dto.request.CardDeleteRequest;
import vn.huythanh.springbootexercise.dto.response.ApiResponse;
import vn.huythanh.springbootexercise.dto.response.CardCreationResponse;
import vn.huythanh.springbootexercise.dto.response.CardDetailResponse;
import vn.huythanh.springbootexercise.service.CardService;
import java.util.List;

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

    @GetMapping
    ApiResponse<List<CardDetailResponse>> getAllCardsByUserLogin() {
        return ApiResponse.<List<CardDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(cardService.getCartByUserLogin())
                .build();
    }

    @DeleteMapping
    ApiResponse<Void> deleteCard(@RequestBody CardDeleteRequest request) {
        cardService.deleteCard(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Card deleted successfully")
                .build();
    }

}
