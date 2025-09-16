package pl.oliwawyplywa.web.dto.orders;

import java.util.List;

public class CreateOrder {

    private String email;
    private String address;
    private List<ProductItem> products;

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public List<ProductItem> getProducts() {
        return products;
    }
}
