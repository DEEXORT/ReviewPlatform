package com.javarush.reviewplatform.review.model;

import com.javarush.reviewplatform.common.BaseTo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ReviewTo extends BaseTo {
    @Size(min = 1, max = 50)
    String title;
    @Size(min = 1, max = 50)
    String content;
    @Min(1)
    @Max(10)
    Integer rating;
    @NotNull(groups = RestValidation.class)
    Long userId;
    @NotNull
    Long productId;

    public interface RestValidation {}
    public interface WebValidation {}
}
