package com.cardapi.card.service.serviceImpl;

import com.cardapi.card.wrappers.CardDto;
import com.cardapi.card.wrappers.CardSearchDto;
import com.cardapi.card.wrappers.GeneralResponseWrapper;

public interface CardService {

    GeneralResponseWrapper createCard(int userId, CardDto cardDto, String userPassword);

    GeneralResponseWrapper delete(int userId, int cardId, String userPassword);

    GeneralResponseWrapper update(int userId, int cardId, CardDto cardDto, String userPassword);

    GeneralResponseWrapper viewCards(int userId, String userPassword);

    GeneralResponseWrapper viewCard(int userId, int cardId, String userPassword);

    GeneralResponseWrapper searchCard(int userId, String userPassword, CardSearchDto cardSearchDto);
}
