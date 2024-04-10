package com.cardapi.card.security;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Base64;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenValidator {
    private final JwkProvider jwkProvider;

    public AccessToken validateAuthorizationHeader(String authorizationHeader) {
        String tokenValue = subStringBearer(authorizationHeader);
        validateToken(tokenValue);
        return new AccessToken(tokenValue);
    }

    private void validateToken(String value) {
        DecodedJWT decodedJWT = decodeToken(value);
        verifyTokenHeader(decodedJWT);
        verifySignature(decodedJWT);
        verifyPayload(decodedJWT);
    }

    private DecodedJWT decodeToken(String value) {
        if (isNull(value)){
            log.error("Token has not been provided");
            return null;
        }
        DecodedJWT decodedJWT = JWT.decode(value);
        log.debug("Token decoded successfully");
        return decodedJWT;
    }

    private void verifyTokenHeader(DecodedJWT decodedJWT) {
        try {
            Preconditions.checkArgument(decodedJWT.getType().equals("JWT"));
            log.debug("Token's header is correct");
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            log.error("Token is not JWT type");
        }
    }

    private void verifySignature(DecodedJWT decodedJWT) {
        try {
            Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(decodedJWT);
            log.debug("Token's signature is correct");
        } catch (JwkException | SignatureVerificationException ex) {
            ex.printStackTrace();
            log.error("Token has invalid signature");
        }
    }

    private void verifyPayload(DecodedJWT decodedJWT) {
        JsonObject payloadAsJson = decodeTokenPayloadToJsonObject(decodedJWT);
        if (hasTokenExpired(payloadAsJson)) {
            log.error("Token has expired");
        }
        log.debug("Token has not expired");

        if (!hasTokenRealmRolesClaim(payloadAsJson)) {
            log.error("Token doesn't contain claims with realm roles");
        }
        log.debug("Token's payload contain claims with realm roles");

        if (!hasTokenScopeInfo(payloadAsJson)) {
            System.out.println("Token doesn't contain scope information");
        }
        log.debug("Token's payload contain scope information");
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

    private boolean hasTokenExpired(JsonObject payloadAsJson) {
        Instant expirationDatetime = extractExpirationDate(payloadAsJson);
        return Instant.now().isAfter(expirationDatetime);
    }

    private Instant extractExpirationDate(JsonObject payloadAsJson) {
        try {
            return Instant.ofEpochSecond(payloadAsJson.get("exp").getAsLong());
        } catch (NullPointerException ex) {
            System.out.println("There is no 'exp' claim in the token payload");
            return null;
        }
    }

    private boolean hasTokenRealmRolesClaim(JsonObject payloadAsJson) {
        try {
            return payloadAsJson.getAsJsonObject("realm_access").getAsJsonArray("roles").size() > 0;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    private boolean hasTokenScopeInfo(JsonObject payloadAsJson) {
        return payloadAsJson.has("scope");
    }

    private String subStringBearer(String authorizationHeader) {
        try {
            return authorizationHeader.substring(AccessToken.BEARER.length());
        } catch (Exception ex) {
            System.out.println("There is no AccessToken in a request header");
            return  null;
        }
    }
}
