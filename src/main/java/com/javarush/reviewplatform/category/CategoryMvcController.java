package com.javarush.reviewplatform.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryMvcController {
    private final CategoryService categoryService;

    @GetMapping
    public String showCategories(Model model) {
        List<CategoryTo> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new CategoryTo());
        return "categories";
    }

    @PostMapping
    public String createCategory(@ModelAttribute CategoryTo category) {
        categoryService.save(category);
        return "redirect:/categories";
    }
}
