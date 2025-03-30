package pl.oliwawyplywa.web.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateUser {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,30}$")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[\\w!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{6,32}$")
    private String password;

    @NotBlank
    @JsonProperty("password_repeat")
    private String passwordRepeat;

    public CreateUser() {
    }

    public CreateUser(String username, String email, String password, String passwordRepeat) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
    }

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordsMatch() {
        return password != null && password.equals(passwordRepeat);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }
}
