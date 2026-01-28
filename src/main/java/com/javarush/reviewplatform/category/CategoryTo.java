package com.javarush.reviewplatform.category;

import com.javarush.reviewplatform.common.BaseTo;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class CategoryTo extends BaseTo {
    @Size(min = 2, max = 50)
    String name;
    String description;

    public CategoryTo(Long id, String name, String description) {
        this.setId(id);
        this.name = name;
        this.description = description;
    }
}
