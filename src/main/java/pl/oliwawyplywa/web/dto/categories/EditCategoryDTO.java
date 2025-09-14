package pl.oliwawyplywa.web.dto.categories;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EditCategoryDTO {

    @NotBlank
    @Pattern(regexp = "^(?=.{3,100}$)[\\p{L}\\p{N}\\p{P}]+(?: [\\p{L}\\p{N}\\p{P}]+)*$")
    @JsonProperty("category_name")
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }
}
