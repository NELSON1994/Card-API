package com.cardapi.card.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "cards")
public class Card {
    @Id
    private int id;

    @Column(unique = true)
    private String name;
    private String description;
    private String color;
    private String status;
    @Column(name="date_of_creation")
    private Date dateOfCreation;

    @Column(name="user_id")
    private int userId;

}
