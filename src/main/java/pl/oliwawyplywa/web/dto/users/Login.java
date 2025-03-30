package pl.oliwawyplywa.web.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {

    @JsonProperty("username_or_email")
    private String usernameOrEmail;
    private String password;

    public Login(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public Login() {
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
