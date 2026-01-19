package com.javarush.reviewplatform.product;

import com.javarush.reviewplatform.common.AbstractBaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService extends AbstractBaseService<Product, ProductTo, ProductRepository, ProductMapper> {
    public ProductService(ProductRepository repository, ProductMapper mapper) {
        super(repository, mapper);
    }

    public List<ProductTo> getProductsByCategoryId(Long categoryId) {
        return repository
                .findByCategoryId(categoryId)
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }
}
