package com.cardapi.card.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;
@RequiredArgsConstructor
public class AccessToken {
    public static final String BEARER = "Bearer ";

    private final String value;

    public String getValueAsString() {
        return value;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        DecodedJWT decodedJWT = decodeToken(value);
        JsonObject payloadAsJson = decodeTokenPayloadToJsonObject(decodedJWT);

        return StreamSupport.stream(
                        payloadAsJson.getAsJsonObject("realm_access").getAsJsonArray("roles").spliterator(), false)
                .map(JsonElement::getAsString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private DecodedJWT decodeToken(String value) {
        if (isNull(value)){
            System.out.println("Token has not been provided");
            return null;
        }
        return JWT.decode(value);
    }

    private JsonObject decodeTokenPayloadToJsonObject(DecodedJWT decodedJWT) {
        try {
            String payloadAsString = decodedJWT.getPayload();
            return new Gson().fromJson(
                    new String(Base64.getDecoder().decode(payloadAsString), StandardCharsets.UTF_8),
                    JsonObject.class);
        }   catch (RuntimeException exception){
            exception.printStackTrace();
            System.out.println("Invalid JWT or JSON format of each of the jwt parts");
            return null;
        }
    }
}
