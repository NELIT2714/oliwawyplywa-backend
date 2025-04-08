package pl.oliwawyplywa.web.dto.shippingAddresses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressResponseDTO {

    @JsonProperty("shipping_address_id")
    private int idShippingAddress;
//
//    @JsonProperty("user_id")
//    private int userId;

    private String country;
    private String voivodeship;
    private String city;
    private String postcode;
    private String street;
    private String buildingAddress;

    public AddressResponseDTO(int idShippingAddress, String country, String voivodeship, String city, String postcode, String street, String buildingAddress) {
        this.idShippingAddress = idShippingAddress;
//        this.userId = userId;
        this.country = country;
        this.voivodeship = voivodeship;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.buildingAddress = buildingAddress;
    }

    public int getIdShippingAddress() {
        return idShippingAddress;
    }

    public void setIdShippingAddress(int idShippingAddress) {
        this.idShippingAddress = idShippingAddress;
    }
//
//    public int getUserId() {
//        return userId;
//    }

//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

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
