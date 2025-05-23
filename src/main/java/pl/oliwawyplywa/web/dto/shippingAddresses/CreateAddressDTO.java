package pl.oliwawyplywa.web.dto.shippingAddresses;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.oliwawyplywa.web.schemas.User;

public class CreateAddressDTO {

    private User user;
    private String country;
    private String voivodeship;
    private String city;
    private String postcode;
    private String street;

    @JsonProperty("building_number")
    private String buildingAddress;

    public CreateAddressDTO() {
    }

    public CreateAddressDTO(User user, String country, String voivodeship, String city, String postcode, String street, String buildingAddress) {
        this.user = user;
        this.country = country;
        this.voivodeship = voivodeship;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.buildingAddress = buildingAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVoivodeship() {
        return voivodeship;
    }

    public void setVoivodeship(String voivodeship) {
        this.voivodeship = voivodeship;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }
}
