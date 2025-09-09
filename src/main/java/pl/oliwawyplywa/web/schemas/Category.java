package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "tbl_categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    @JsonProperty("category_id")
    private int idCategory;

    @NotBlank
    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    @JsonProperty("category_name")
    @Pattern(regexp = "^(?=.{3,100}$)[\\p{L}\\p{N}\\p{P}]+(?: [\\p{L}\\p{N}\\p{P}]+)*$")
    private String categoryName;

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
