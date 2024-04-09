package com.cardapi.card.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("card.role")
@Data
public class ApplicationConfigs {

    private String admin;

    private String member;

}
