package pl.oliwawyplywa.web.dto.tokens;

import java.time.Instant;

public class TokenResponse {

    private final String token;
    private final Instant expiresAt;

    public TokenResponse(String token, Instant expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
