package com.javarush.reviewplatform.review;

import com.javarush.reviewplatform.product.ProductService;
import com.javarush.reviewplatform.product.ProductTo;
import com.javarush.reviewplatform.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(Constant.Path.REVIEWS)
@RequiredArgsConstructor
public class ReviewMvcController {
    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping("/products/{id}")
    public String showReviewForm(@PathVariable Long id, Model model) {
        ProductTo product = productService.getById(id);
        model.addAttribute("product", product);
        model.addAttribute("review", new ReviewTo());
        model.addAttribute("templateName", "review");
        model.addAttribute("fragmentName", "review-form");
        return Constant.View.MAIN;
    }

    @PostMapping
    public String createReview(@ModelAttribute("review") ReviewTo review, Model model) {
        ReviewTo saved = reviewService.save(review);
        return "redirect:" + Constant.Path.PRODUCTS;
    }
}
