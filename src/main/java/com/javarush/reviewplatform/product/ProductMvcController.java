package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.category.CategoryService;
import com.javarush.reviewplatform.category.CategoryTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
@Slf4j
@RequiredArgsConstructor
public class ProductMvcController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String showProducts(Model model) {
        List<ProductTo> products = productService.getAll();
        List<CategoryTo> categories = categoryService.getAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductTo());
        return "products";
    }

    @PostMapping
    public String createProduct(@ModelAttribute ProductTo product) {
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        List<ProductTo> products = productService.getProductsByCategoryId(id);
        List<CategoryTo> categories = categoryService.getAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductTo());
        return "products";
    }
}
