package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.util.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.Path.API_PRODUCTS)
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;

    @GetMapping
    public List<ProductTo> getAllProducts() {
        return productService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ProductTo createProduct(@Valid @RequestBody ProductTo productTo) {
        return productService.save(productTo);
    }

    @GetMapping("/{id}")
    public ProductTo getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ProductTo updateProduct(@Valid @RequestBody ProductTo productTo) {
        if (productTo.getId() == null) throw new IllegalArgumentException("Product ID is required for update");
        return productService.save(productTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        boolean deleted = productService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
