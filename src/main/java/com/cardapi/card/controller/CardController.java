package com.cardapi.card.controller;

import com.cardapi.card.service.serviceImpl.CardService;
import com.cardapi.card.wrappers.CardDto;
import com.cardapi.card.wrappers.CardSearchDto;
import com.cardapi.card.wrappers.GeneralResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/{userId}")
    private ResponseEntity<GeneralResponseWrapper> create( @PathVariable int userId,@Validated @RequestBody CardDto cardDto, @RequestParam String userPassword){
        var generalResponseWrapper=cardService.createCard(userId,cardDto,userPassword);
        return ResponseEntity.ok(generalResponseWrapper);
    }

    @GetMapping("/{userId}")
    private ResponseEntity<GeneralResponseWrapper> viewCards(@PathVariable int userId, @RequestParam String userPassword){
        var generalResponseWrapper=cardService.viewCards(userId,userPassword);
        return ResponseEntity.ok(generalResponseWrapper);
    }

    @GetMapping("/{userId}/{cardId}")
    private ResponseEntity<GeneralResponseWrapper> viewCard(@PathVariable int userId, @PathVariable int cardId,@RequestParam String userPassword){
        var generalResponseWrapper=cardService.viewCard(userId,cardId,userPassword);
        return ResponseEntity.ok(generalResponseWrapper);
    }

    @DeleteMapping("/{userId}/{cardId}")
    private ResponseEntity<GeneralResponseWrapper> delete(@PathVariable int userId, @PathVariable int cardId,@RequestParam String userPassword){
        var generalResponseWrapper=cardService.delete(userId,cardId,userPassword);
        return ResponseEntity.ok(generalResponseWrapper);
    }

    @PutMapping("/{userId}/{cardId}")
    private ResponseEntity<GeneralResponseWrapper> update(@PathVariable int userId, @Validated @RequestBody CardDto cardDto,@PathVariable int cardId,@RequestParam String userPassword){
        var generalResponseWrapper=cardService.update(userId,cardId,cardDto,userPassword);
        return ResponseEntity.ok(generalResponseWrapper);
    }

    @GetMapping("/{userId}/search")
    private ResponseEntity<GeneralResponseWrapper> search(@PathVariable int userId, @Validated @RequestBody CardSearchDto cardSearchDto, @RequestParam String userPassword){
        var generalResponseWrapper=cardService.searchCard(userId,userPassword,cardSearchDto);
        return ResponseEntity.ok(generalResponseWrapper);
    }

}
