package pl.oliwawyplywa.web.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;

public class UpdateUserDTO {

    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,30}$")
    private String username;

    @JsonProperty("personal_data")
    private PersonalDataDTO personalData;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String username, PersonalDataDTO personalData) {
        this.username = username;
        this.personalData = personalData;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PersonalDataDTO getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalDataDTO personalData) {
        this.personalData = personalData;
    }
}
