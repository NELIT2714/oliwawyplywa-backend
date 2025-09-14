package pl.oliwawyplywa.web.dto.admins;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAdminDTO {

    private String username;
    private String password;

    @JsonProperty("password_repeat")
    private String passwordRepeat;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }
}
