package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.common.BaseTo;
import com.javarush.reviewplatform.product.Product;
import com.javarush.reviewplatform.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewTo extends BaseTo {
    @Size(min = 1, max = 50)
    String title;
    @Size(min = 1, max = 50)
    String content;
    @Size(min = 1, max = 10)
    Integer rating;
    @NotNull
    User user;
    @NotNull
    Product product;
}
