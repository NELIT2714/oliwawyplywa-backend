package pl.oliwawyplywa.web.dto.admins;

import java.time.Instant;
import java.util.Date;

public class TokenResponse {

    private String token;
    private Instant expiration;

    public TokenResponse(String token, Instant expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiration() {
        return expiration;
    }
}
