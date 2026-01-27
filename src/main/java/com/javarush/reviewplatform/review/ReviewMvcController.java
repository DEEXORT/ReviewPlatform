package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.auth.service.CustomUserDetails;
import com.javarush.reviewplatform.product.ProductService;
import com.javarush.reviewplatform.product.ProductTo;
import com.javarush.reviewplatform.user.UserService;
import com.javarush.reviewplatform.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(Constant.Path.REVIEWS)
@RequiredArgsConstructor
public class ReviewMvcController {
    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping
    public String showReviews(Model model) {
        List<ReviewViewTo> reviews = reviewService.getAllReviewViews();
        model.addAttribute("reviews", reviews);
        model.addAttribute("templateName", "review");
        model.addAttribute("fragmentName", "reviews");
        return Constant.View.MAIN;
    }

    @GetMapping(Constant.Path.EDIT + "/{reviewId}")
    public String showEditReviewForm(@PathVariable Long reviewId, Model model) {
        ReviewTo reviewTo = reviewService.getById(reviewId);
        ProductTo product = productService.getById(reviewTo.getProductId());
        model.addAttribute("review", reviewTo);
        model.addAttribute("product", product);
        model.addAttribute("templateName", "review");
        model.addAttribute("fragmentName", "review-form");
        return Constant.View.MAIN;
    }

    @GetMapping("/product/{id}")
    public String showCreateReviewForm(@PathVariable Long id, Model model) {
        ProductTo product = productService.getById(id);
        ReviewTo review = new ReviewTo();
        review.setProductId(product.getId());

        model.addAttribute("product", product);
        model.addAttribute("review", review);
        model.addAttribute("templateName", "review");
        model.addAttribute("fragmentName", "review-form");
        return Constant.View.MAIN;
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER') and " +
            "authentication.name == #username")
    public String showUserReviews(@PathVariable String username, Model model) {
        List<ReviewViewTo> reviews = reviewService.getReviewViewsByUsername(username);
        model.addAttribute("reviews", reviews);
        model.addAttribute("templateName", "review/user-reviews");
        model.addAttribute("fragmentName", "reviews");
        return Constant.View.MAIN;
    }

    @PostMapping
    public String createReview(@Validated(ReviewTo.WebValidation.class) @ModelAttribute("review") ReviewTo review,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        review.setUserId(customUserDetails.getId());
        ReviewTo saved = reviewService.save(review);
        return "redirect:" + Constant.Path.PRODUCTS;
    }

    @PostMapping(Constant.Path.UPDATE)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER') and " +
            "(@securityService.isReviewOwner(#review.id, authentication.name) or hasRole('ADMIN'))")
    public String updateReview(@ModelAttribute("review") ReviewTo review) {
        ReviewTo saved = reviewService.save(review);
        return "redirect:" + Constant.Path.PRODUCTS;
    }

    @PostMapping(Constant.Path.DELETE + "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER') and " +
            "(@securityService.isReviewOwner(#id, authentication.name) or hasRole('ADMIN'))")
    public String deleteReview(@PathVariable Long id,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication) {
        boolean deleted = reviewService.deleteById(id);
        if (!deleted) {
            redirectAttributes.addAttribute("errorMessage", "Review not found");
        }
        return "redirect:" + Constant.Path.REVIEWS + "/" + authentication.getName();
    }
}
