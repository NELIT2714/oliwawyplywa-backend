package pl.oliwawyplywa.web.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateOrder {

    @JsonProperty("full_name")
    private String fullName;

    private String email;
    private String address;
    private List<OrderItemDTO> products;

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderItemDTO> getProducts() {
        return products;
    }

    public String getFullName() {
        return fullName;
    }
}
