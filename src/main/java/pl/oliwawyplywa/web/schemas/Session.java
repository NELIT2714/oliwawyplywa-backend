package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tbl_users_sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id_session")
    @Column(name = "id_session")
    private int idSession;

    @Column(name = "token", length = 300, unique = true, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "expires_at", nullable = false)
    @JsonProperty("expires_at")
    private Instant expiresAt;

    public Session(String token, User user, Instant expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public Session() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
