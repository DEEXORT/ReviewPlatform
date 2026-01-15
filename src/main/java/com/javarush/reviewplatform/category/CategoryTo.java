package com.javarush.reviewplatform.category;

import com.javarush.reviewplatform.common.BaseTo;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class CategoryTo extends BaseTo {
    @Size(min = 2, max = 50)
    String name;
    String description;
}
