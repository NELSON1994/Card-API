package com.cardapi.card.repository;

import com.cardapi.card.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findByUserId(int userId);

    List<Card> findByName(String name);

    List<Card> findByColor(String color);

    List<Card> findByDescription(String description);

    List<Card> findByNameOrColor(String name, String color);

    List<Card> findByNameOrDescription(String name, String Description);

    List<Card> findByColorOrDescription(String color, String Description);

    List<Card> findByNameOrColorOrDescription(String name, String color, String description);

}
