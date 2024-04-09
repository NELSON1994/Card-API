package com.cardapi.card.wrappers;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CardDto {
    @NotNull(message = "Name is required")
    private String name;
    private String description;
    private String color;
    private String status;
}
