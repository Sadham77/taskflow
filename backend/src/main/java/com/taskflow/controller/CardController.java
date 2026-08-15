package com.taskflow.controller;

import com.taskflow.dto.CardRequest;
import com.taskflow.dto.MoveCardRequest;
import com.taskflow.model.Card;
import com.taskflow.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/lists/{listId}/cards")
    public ResponseEntity<Card> createCard(
            @PathVariable Long listId,
            @RequestBody CardRequest request,
            Authentication auth
    ) {
        return ResponseEntity.ok(cardService.createCard(listId, request, auth.getName()));
    }

    @PutMapping("/cards/{cardId}")
    public ResponseEntity<Card> updateCard(
            @PathVariable Long cardId,
            @RequestBody CardRequest request,
            Authentication auth
    ) {
        return ResponseEntity.ok(cardService.updateCard(cardId, request, auth.getName()));
    }

    // Called when a card is dragged to a new list/position in the UI
    @PutMapping("/cards/{cardId}/move")
    public ResponseEntity<Void> moveCard(
            @PathVariable Long cardId,
            @RequestBody MoveCardRequest request,
            Authentication auth
    ) {
        cardService.moveCard(cardId, request, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId, Authentication auth) {
        cardService.deleteCard(cardId, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
