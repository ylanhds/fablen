package com.fablen.product.controller;

import com.fablen.common.Result;
import com.fablen.product.entity.Product;
import com.fablen.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = (name == null || name.isEmpty()) ?
                productService.getAllProductsPageable(pageable) :
                productService.searchProducts(name, pageable);
        return Result.success(productPage);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_EDITOR') or hasAuthority('ROLE_PRODUCT_ADMIN')")
    public Result<Product> addProduct(@RequestBody Product product) {
        return Result.success(productService.addProduct(product));
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_EDITOR') or hasAuthority('ROLE_PRODUCT_ADMIN')")
    public Result<Product> editProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return Result.success(productService.editProduct(id, productDetails));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_ADMIN')")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }
}
