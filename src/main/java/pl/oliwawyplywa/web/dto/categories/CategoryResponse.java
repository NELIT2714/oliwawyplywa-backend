package pl.oliwawyplywa.web.dto.categories;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.oliwawyplywa.web.dto.products.ResponseProductDTO;

import java.util.List;

public class CategoryResponse {

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("products")
    private List<ResponseProductDTO> products;

    public CategoryResponse(int categoryId, String categoryName, List<ResponseProductDTO> products) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.products = products;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ResponseProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ResponseProductDTO> products) {
        this.products = products;
    }
}
