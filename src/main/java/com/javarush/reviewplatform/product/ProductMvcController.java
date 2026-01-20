package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.category.CategoryService;
import com.javarush.reviewplatform.category.CategoryTo;
import com.javarush.reviewplatform.util.Constant;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Constant.Path.PRODUCTS)
@Slf4j
@RequiredArgsConstructor
public class ProductMvcController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String showProducts(Model model) {
        List<ProductTo> products = productService.getAll();
        setModelAttrs(model, products);
        return Constant.View.MAIN;
    }

    @PostMapping
    public String createProduct(@ModelAttribute ProductTo product) {
        productService.save(product);
        return "redirect:" + Constant.Path.PRODUCTS;
    }

    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        List<ProductTo> products = productService.getProductsByCategoryId(id);
        setModelAttrs(model, products);
        return Constant.View.MAIN;
    }

    @GetMapping("/edit/{id}")
    public String showEditProduct(@PathVariable Long id, Model model) {
        ProductTo product = productService.getById(id);
        List<ProductTo> products = productService.getAll();
        setModelAttrs(model, products);
        model.addAttribute("product", product);
        return Constant.View.MAIN;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteById(id);
        if (deleted) {
            return "redirect:" + Constant.Path.PRODUCTS;
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
