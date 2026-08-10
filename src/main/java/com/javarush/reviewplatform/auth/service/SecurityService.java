package com.javarush.reviewplatform.auth.service;

import com.javarush.reviewplatform.review.model.Review;
import com.javarush.reviewplatform.review.repository.ReviewRepository;
import com.javarush.reviewplatform.user.model.User;
import com.javarush.reviewplatform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public boolean isReviewOwner(Long reviewId, String username) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()) return false;

        Optional<User> user = userRepository.findById(review.get().getId());
        return user.isPresent() && user.get().getUsername().equals(username);
    }
}
