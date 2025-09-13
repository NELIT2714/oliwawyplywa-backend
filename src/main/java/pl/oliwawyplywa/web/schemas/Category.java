package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("tbl_categories")
public class Category {

    @Id
    @JsonProperty("category_id")
    private Integer idCategory;

    @NotBlank
    @Pattern(regexp = "^(?=.{3,100}$)[\\p{L}\\p{N}\\p{P}]+(?: [\\p{L}\\p{N}\\p{P}]+)*$")
    @JsonProperty("category_name")
    private String categoryName;

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
