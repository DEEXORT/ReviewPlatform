package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.category.Category;
import com.javarush.reviewplatform.category.CategoryService;
import com.javarush.reviewplatform.category.CategoryTo;
import jakarta.validation.constraints.NotNull;
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
        setModelAttrs(model, products);
        return "main";
    }

    @PostMapping
    public String createProduct(@ModelAttribute ProductTo product) {
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        List<ProductTo> products = productService.getProductsByCategoryId(id);
        setModelAttrs(model, products);
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String showEditProduct(@PathVariable Long id, Model model) {
        ProductTo product = productService.getById(id);
        List<ProductTo> products = productService.getAll();
        setModelAttrs(model, products);
        model.addAttribute("product", product);
        return "main";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteById(id);
        if (deleted) {
            return "redirect:/products";
        } else {
            throw new ProductNotFoundException("Product with id = " + id + " not found");
        }
    }

    @NotNull
    private void setModelAttrs(Model model, List<ProductTo> products) {
        List<CategoryTo> categories = categoryService.getAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductTo());
        model.addAttribute("templateName", "products");
        model.addAttribute("fragmentName", "productsContent");
    }
}
