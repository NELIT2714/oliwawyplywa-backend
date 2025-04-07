package pl.oliwawyplywa.web.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.oliwawyplywa.web.dto.sessions.SessionDTO;
import pl.oliwawyplywa.web.dto.shippingAddresses.AddressResponseDTO;
import pl.oliwawyplywa.web.schemas.ShippingAddress;

import java.util.List;

public class UserResponse {

    @JsonProperty("user_id")
    private int userId;

    private String username;

    @JsonProperty("personal_data")
    private PersonalDataDTO personalData;

    @JsonProperty("shipping_addresses")
    private List<AddressResponseDTO> shippingAddresses;

    @JsonProperty("sessions")
    private List<SessionDTO> sessions;

    public UserResponse(int userId, String username, PersonalDataDTO personalData, List<AddressResponseDTO> shippingAddresses, List<SessionDTO> sessions) {
        this.userId = userId;
        this.username = username;
        this.personalData = personalData;
        this.shippingAddresses = shippingAddresses;
        this.sessions = sessions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<AddressResponseDTO> getShippingAddresses() {
        return shippingAddresses;
    }

    public void setShippingAddresses(List<AddressResponseDTO> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
    }

    public List<SessionDTO> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionDTO> sessions) {
        this.sessions = sessions;
    }

    public PersonalDataDTO getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalDataDTO personalData) {
        this.personalData = personalData;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
