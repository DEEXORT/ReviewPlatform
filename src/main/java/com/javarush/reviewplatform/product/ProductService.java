package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.common.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService extends AbstractBaseService<Product, ProductTo, ProductRepository, ProductMapper> {
    public ProductService(ProductRepository repository, ProductMapper mapper) {
        super(repository, mapper);
    }

    @Transactional
    public List<ProductViewTo> getProductsByCategoryId(Long categoryId) {
        return repository
                .findByCategoryId(categoryId)
                .map(mapper::mapToViewDto)
                .collect(Collectors.toList());
    }

    public List<ProductViewTo> getProductViews() {
        return repository.findAll().stream()
                .map(mapper::mapToViewDto)
                .toList();
    }
}
