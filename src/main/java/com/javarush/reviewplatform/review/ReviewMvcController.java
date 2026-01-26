package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.auth.CustomUserDetails;
import com.javarush.reviewplatform.product.ProductService;
import com.javarush.reviewplatform.product.ProductTo;
import com.javarush.reviewplatform.user.UserService;
import com.javarush.reviewplatform.user.UserTo;
import com.javarush.reviewplatform.util.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(Constant.Path.REVIEWS)
@RequiredArgsConstructor
public class ReviewMvcController {
    private final ProductService productService;
    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/product/{id}")
    public String showReviewForm(@PathVariable Long id, Model model) {
        ProductTo product = productService.getById(id);
        ReviewTo review = new ReviewTo();
        review.setProductId(product.getId());

        model.addAttribute("product", product);
        model.addAttribute("review", review);
        model.addAttribute("templateName", "review");
        model.addAttribute("fragmentName", "review-form");
        return Constant.View.MAIN;
    }

    @PostMapping
    public String createReview(@Validated(ReviewTo.WebValidation.class) @ModelAttribute("review") ReviewTo review,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails,
                               Model model) {
        review.setUserId(customUserDetails.getId());
        ReviewTo saved = reviewService.save(review);
        return "redirect:" + Constant.Path.PRODUCTS;
    }

    @PostMapping("/update")
    public String updateReview(@ModelAttribute("review") ReviewTo review, Model model) {
        ReviewTo saved = reviewService.save(review);
        return "redirect:" + Constant.Path.PRODUCTS;
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id, BindingResult result, Model model) {
        boolean deleted = reviewService.deleteById(id);
        if (deleted) {
            return "redirect:" + Constant.Path.PRODUCTS;
        } else {
            result.addError(new FieldError("review", "review", "Review not deleted"));
        }
        if (result.hasErrors()) {
            return "redirect:" + Constant.Path.PRODUCTS;
        }
        return "redirect:" + Constant.Path.REVIEWS + "/products/" + id;
    }
}
