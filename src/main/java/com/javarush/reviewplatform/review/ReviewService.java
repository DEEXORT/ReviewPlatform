package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.common.AbstractBaseService;
import org.springframework.stereotype.Service;

@Service
public class ReviewService extends AbstractBaseService<Review, ReviewTo, ReviewRepository, ReviewMapper> {
    public ReviewService(ReviewRepository repository, ReviewMapper mapper) {
        super(repository, mapper);
    }
}
