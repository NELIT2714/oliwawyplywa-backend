package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    @JsonProperty("id_user")
    private int idUser;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_personal_data", referencedColumnName = "id_personal_data", unique = true, nullable = false)
    private PersonalData personalData;

    @OneToMany(mappedBy = "user")
    @JsonProperty("shipping_addresses")
    private List<ShippingAddress> shippingAddresses;

    @OneToMany(mappedBy = "user")
    @JsonProperty("sessions")
    private List<Session> sessions;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    public User() {
    }

    public User(String username, PersonalData personalData, String passwordHash) {
        this.username = username;
        this.personalData = personalData;
        this.passwordHash = passwordHash;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public List<ShippingAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
