package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_users_sessions")
public class Session {

    @Id
    @Column(name = "token", length = 300)
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    private User user;

    public Session(String token, User user) {
        this.token = token;
        this.user = user;
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
}
