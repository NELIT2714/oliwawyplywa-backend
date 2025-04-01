package pl.oliwawyplywa.web.dto.users;

import pl.oliwawyplywa.web.dto.sessions.SessionDTO;
import pl.oliwawyplywa.web.schemas.ShippingAddress;

import java.util.List;

public class UserResponse {

    private String username;
    private PersonalDataDTO personalData;
    private List<ShippingAddress> shippingAddresses;
    private List<SessionDTO> sessions;

    public UserResponse(String username, PersonalDataDTO personalData, List<ShippingAddress> shippingAddresses, List<SessionDTO> sessions) {
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

    public List<ShippingAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public void setShippingAddresses(List<ShippingAddress> shippingAddresses) {
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
}
