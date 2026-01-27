package com.javarush.reviewplatform.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ReviewViewTo extends ReviewTo {
    String categoryName;
    String productName;
}
