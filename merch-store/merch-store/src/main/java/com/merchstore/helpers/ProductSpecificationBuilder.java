package com.merchstore.helpers;

import com.merchstore.models.Product;
import com.merchstore.models.filters.ProductFilterOptions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecificationBuilder {

    public static Specification<Product> buildFrom(ProductFilterOptions options) {

        return Specification
                .where(ProductSpecification.withMinPrice(options.getMinPrice()))
                .and(ProductSpecification.withMaxPrice(options.getMaxPrice()))
                .and(ProductSpecification.withCategory(options.getCategoryId()))
                .and(ProductSpecification.withName(options.getName()));
    }
}