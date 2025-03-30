package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_shipping_addresses")
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_shipping_address")
    @JsonProperty("id_shipping_address")
    private int idShippingAddress;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "voivodeship", nullable = false)
    private String voivodeship;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "street")
    private String street;

    public ShippingAddress() {
    }

    public ShippingAddress(int idShippingAddress, User user, String country, String voivodeship, String city, String postcode, String street) {
        this.idShippingAddress = idShippingAddress;
        this.user = user;
        this.country = country;
        this.voivodeship = voivodeship;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
    }

    public int getIdShippingAddress() {
        return idShippingAddress;
    }

    public void setIdShippingAddress(int idShippingAddress) {
        this.idShippingAddress = idShippingAddress;
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
}
