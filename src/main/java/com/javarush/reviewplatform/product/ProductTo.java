package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.common.BaseTo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProductTo extends BaseTo {
    @Size(min = 2, max = 20)
    String title;
    String description;
    @NotNull
    Long categoryId;
    @Builder.Default
    Double rating = 0.0;
    @Builder.Default
    Integer reviewCount = 0;
}
