package com.javarush.reviewplatform.category;

import com.javarush.reviewplatform.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Constant.Path.CATEGORIES)
@Slf4j
@RequiredArgsConstructor
public class CategoryMvcController {
    private final CategoryService categoryService;

    @GetMapping
    public String showCategories(Model model) {
        List<CategoryTo> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new CategoryTo());
        model.addAttribute("templateName", "categories");
        model.addAttribute("fragmentName", "categoriesContent");
        return Constant.View.MAIN;
    }

    @PostMapping
    public String createCategory(@ModelAttribute CategoryTo category) {
        categoryService.save(category);
        return "redirect:" + Constant.Path.CATEGORIES;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteById(id);
        if (deleted) {
            return "redirect:/categories";
        } else {
            throw new CategoryNotFoundException("Category with id = " + id + " not found");
        }
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute CategoryTo category) {
        categoryService.save(category);
        return "redirect:/categories";
    }
}
