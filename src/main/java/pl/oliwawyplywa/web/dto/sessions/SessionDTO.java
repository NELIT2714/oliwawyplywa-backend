package pl.oliwawyplywa.web.dto.sessions;

import java.time.Instant;

public class SessionDTO {

    private String token;
    private Instant expiresAt;

    public SessionDTO(String token, Instant expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
