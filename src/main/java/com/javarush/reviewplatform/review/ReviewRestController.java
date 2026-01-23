package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.util.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.Path.API_REVIEWS)
@RequiredArgsConstructor
public class ReviewRestController {
    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewTo> getAllReviews() {
        return reviewService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ReviewTo createReview(@Valid @RequestBody ReviewTo reviewTo) {
        return reviewService.save(reviewTo);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ReviewTo updateReview(@Valid @RequestBody ReviewTo reviewTo) {
        if (reviewTo.getId() == null) throw new IllegalArgumentException("Review ID is required for update");
        return reviewService.save(reviewTo);
    }

    @GetMapping("/{id}")
    public ReviewTo getReviewById(@PathVariable Long id) {
        return reviewService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteReviewById(@PathVariable Long id) {
        boolean deleted = reviewService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
