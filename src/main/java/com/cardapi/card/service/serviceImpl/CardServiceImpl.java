package com.cardapi.card.service.serviceImpl;

import com.cardapi.card.config.ApplicationConfigs;
import com.cardapi.card.model.Card;
import com.cardapi.card.repository.CardRepository;
import com.cardapi.card.repository.UserRepository;
import com.cardapi.card.wrappers.CardDto;
import com.cardapi.card.wrappers.CardSearchDto;
import com.cardapi.card.wrappers.GeneralResponseWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final ApplicationConfigs applicationConfigs;

    public CardServiceImpl(UserRepository userRepository, CardRepository cardRepository, ApplicationConfigs applicationConfigs) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.applicationConfigs = applicationConfigs;
    }

    @Override
    public GeneralResponseWrapper createCard(int userId, CardDto cardDto, String userPassword) {
        var response = new GeneralResponseWrapper();
        var user = userRepository.findById(userId);
        if (user == null) {
            response.setResponseCode(404);
            response.setMessage("User not found");
            return response;
        }
        if(!user.get().getPassword().equals(userPassword)){
            response.setResponseCode(400);
            response.setMessage("Wrong password");
            return response;
        }
        var card = new Card();
        card.setColor("#".concat(cardDto.getColor()));
        card.setName(cardDto.getName());
        card.setDescription(cardDto.getDescription());
        card.setStatus("To Do");
        card.setDateOfCreation(new Date());
        card.setUserId(userId);

        cardRepository.save(card);
        response.setResponseCode(201);
        response.setMessage("Card created successfully");
        response.setData(card);
        return response;
    }

    @Override
    public GeneralResponseWrapper delete(int userId, int cardId, String userPassword) {
        var response = new GeneralResponseWrapper();
        var user = userRepository.findById(userId);
        if (user == null) {
            response.setResponseCode(404);
            response.setMessage("User not found");
            return response;
        }
        if(!user.get().getPassword().equals(userPassword)){
            response.setResponseCode(400);
            response.setMessage("Wrong password");
            return response;
        }
        var card = cardRepository.findById(cardId);
        if (card == null) {
            response.setResponseCode(404);
            response.setMessage("Card not found");
            return response;

        }
        var cardd = card.get();
        if (user.get().getRole().equals(applicationConfigs.getAdmin()) || user.get().getId() == cardd.getUserId()) {
            cardRepository.delete(cardd);
            response.setResponseCode(200);
            response.setMessage("Card Deleted successfully");
            response.setData(card);

        } else {
            response.setResponseCode(403);
            response.setMessage("User Not Permitted to Delete this card");
        }
        return response;
    }

    @Override
    public GeneralResponseWrapper update(int userId, int cardId, CardDto cardDto, String userPassword) {
        var response = new GeneralResponseWrapper();
        var user = userRepository.findById(userId);
        if (user == null) {
            response.setResponseCode(404);
            response.setMessage("User not found");
            return response;
        }
        if(!user.get().getPassword().equals(userPassword)){
            response.setResponseCode(400);
            response.setMessage("Wrong password");
            return response;
        }
        var card = cardRepository.findById(cardId);
        if (card == null) {
            response.setResponseCode(404);
            response.setMessage("Card not found");
            return response;

        }
        var cardd = card.get();
        if (user.get().getRole().equals(applicationConfigs.getAdmin()) || user.get().getId() == cardd.getUserId()) {
            cardd.setColor("#".concat(cardDto.getColor()));
            cardd.setName(cardDto.getName());
            cardd.setDescription(cardDto.getDescription());
            cardd.setStatus(cardDto.getStatus());
            cardRepository.save(cardd);

            response.setResponseCode(200);
            response.setMessage("Card Updated successfully");
            response.setData(card);

        } else {
            response.setResponseCode(403);
            response.setMessage("User Not Permitted to Update this card");
        }
        return response;
    }

    @Override
    public GeneralResponseWrapper viewCards(int userId, String userPassword) {
        var response = new GeneralResponseWrapper();
        var user = userRepository.findById(userId);
        if (user == null) {
            response.setResponseCode(404);
            response.setMessage("User not found");
            return response;
        }
        if(!user.get().getPassword().equals(userPassword)){
            response.setResponseCode(400);
            response.setMessage("Wrong password");
            return response;
        }
        var cards = new ArrayList<Card>();
        if (user.get().getRole().equals(applicationConfigs.getAdmin())) {
            cards = (ArrayList<Card>) cardRepository.findAll();
        } else {
            cards = (ArrayList<Card>) cardRepository.findByUserId(userId);
        }
        response.setResponseCode(200);
        response.setMessage("Cards fetched successfully");
        response.setData(cards);
        return response;

    }

    @Override
    public GeneralResponseWrapper viewCard(int userId, int cardId, String userPassword) {
        var response = new GeneralResponseWrapper();
        var user = userRepository.findById(userId);
        if (user == null) {
            response.setResponseCode(404);
            response.setMessage("User not found");
            return response;
        }
        if(!user.get().getPassword().equals(userPassword)){
            response.setResponseCode(400);
            response.setMessage("Wrong password");
            return response;
        }
        var card = cardRepository.findById(cardId);
        if (card == null) {
            response.setResponseCode(404);
            response.setMessage("Card not found");
            return response;
        }
        var cardd = card.get();
        if (user.get().getRole().equals(applicationConfigs.getAdmin()) || user.get().getId()==cardd.getId()) {
            response.setResponseCode(200);
            response.setMessage("Card fetched successfully");
        } else {
            response.setResponseCode(403);
            response.setMessage("User NOT Allowed to View this Card");
        }
        return response;

    }
    @Override
    public GeneralResponseWrapper searchCard(int userId, String userPassword, CardSearchDto cardSearchDto){
        var response = new GeneralResponseWrapper();
        var user = userRepository.findById(userId);
        if (user == null) {
            response.setResponseCode(404);
            response.setMessage("User not found");
            return response;
        }
        if(!user.get().getPassword().equals(userPassword)){
            response.setResponseCode(400);
            response.setMessage("Wrong password");
            return response;
        }
        var cards = new ArrayList<Card>();
        if(!cardSearchDto.getName().isEmpty() && !cardSearchDto.getColor().isEmpty() && !cardSearchDto.getDescription().isEmpty()){
            cards = (ArrayList<Card>) cardRepository.findByNameOrColorOrDescription(cardSearchDto.getName(), cardSearchDto.getColor(), cardSearchDto.getDescription());

        }
        else if(cardSearchDto.getName().isEmpty()&& (!cardSearchDto.getColor().isEmpty()&& !cardSearchDto.getDescription().isEmpty())){
            cards= (ArrayList<Card>) cardRepository.findByColorOrDescription(cardSearchDto.getColor(),cardSearchDto.getDescription());
        }
        else if(cardSearchDto.getColor().isEmpty()&& (!cardSearchDto.getName().isEmpty()&& !cardSearchDto.getDescription().isEmpty())){
            cards= (ArrayList<Card>) cardRepository.findByNameOrDescription(cardSearchDto.getName(), cardSearchDto.getDescription());
        }
        else if(cardSearchDto.getDescription().isEmpty()&& (!cardSearchDto.getColor().isEmpty()&& !cardSearchDto.getName().isEmpty())){
            cards= (ArrayList<Card>) cardRepository.findByNameOrColor(cardSearchDto.getName(),cardSearchDto.getColor());
        }

        else if(!cardSearchDto.getName().isEmpty()&& (cardSearchDto.getColor().isEmpty()&&cardSearchDto.getDescription().isEmpty())){
            cards = (ArrayList<Card>) cardRepository.findByName(cardSearchDto.getName());

        }
        else if(!cardSearchDto.getColor().isEmpty()&& (cardSearchDto.getName().isEmpty()&&cardSearchDto.getDescription().isEmpty())){
            cards = (ArrayList<Card>) cardRepository.findByColor(cardSearchDto.getColor());
        }
        else if(!cardSearchDto.getDescription().isEmpty()&& (cardSearchDto.getColor().isEmpty()&&cardSearchDto.getName().isEmpty())){
            cards = (ArrayList<Card>) cardRepository.findByDescription(cardSearchDto.getDescription());
        }
        else {
        cards=new ArrayList<>();
        }
        var cardsFiltered = new ArrayList<Card>();
        if(user.get().getRole().equals(applicationConfigs.getAdmin())){
            cardsFiltered.addAll(cards);
        }
        else {
            cardsFiltered.addAll(cards.stream().filter(card -> card.getUserId() == userId).collect(Collectors.toList()));
        }

        response.setResponseCode(200);
        response.setMessage("Cards fetched successfully");
        response.setData(cardsFiltered);
        return response;
    }


}
