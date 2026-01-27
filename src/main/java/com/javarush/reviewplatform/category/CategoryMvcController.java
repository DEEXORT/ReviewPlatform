package com.javarush.reviewplatform.category;

import com.javarush.reviewplatform.util.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
        model.addAttribute("templateName", "category/categories");
        model.addAttribute("fragmentName", "categoriesContent");
        return Constant.View.MAIN;
    }

    @GetMapping(Constant.Path.EDIT + "/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        CategoryTo category = categoryService.getById(id);

        model.addAttribute("category", category);
        model.addAttribute("templateName", "category/category-form");
        model.addAttribute("fragmentName", "categoryEditForm");
        return Constant.View.MAIN;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createCategory(@Valid @ModelAttribute CategoryTo category) {
        categoryService.save(category);
        return "redirect:" + Constant.Path.CATEGORIES;
    }

    @PostMapping(Constant.Path.DELETE + "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteById(id);
        if (deleted) {
            return "redirect:" + Constant.Path.CATEGORIES;
        } else {
            throw new CategoryNotFoundException("Category with id = " + id + " not found");
        }
    }

    @PostMapping(Constant.Path.UPDATE)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateCategory(@Valid @ModelAttribute CategoryTo category) {
        categoryService.save(category);
        return "redirect:" + Constant.Path.CATEGORIES;
    }
}
