package com.fablen.product.controller;

import com.fablen.common.Result;
import com.fablen.product.entity.Product;
import com.fablen.product.service.DubboProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private DubboProductService dubboProductService;

    @GetMapping
    public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String name) {

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Product> productPage =
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Product> resultPage =
            (name == null || name.isEmpty()) ?
                dubboProductService.getAllProductsPageable(productPage) :
                dubboProductService.searchProducts(name, productPage);

        return Result.success(resultPage);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_EDITOR') or hasAuthority('ROLE_PRODUCT_ADMIN')")
    public Result<Product> addProduct(@RequestBody Product product) {
        return Result.success(dubboProductService.addProduct(product));
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ROLE_EDITOR') or hasAuthority('ROLE_PRODUCT_ADMIN')")
    public Result<Product> editProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return Result.success(dubboProductService.editProduct(id, productDetails));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_ADMIN')")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        dubboProductService.deleteProduct(id);
        return Result.success();
    }

    @GetMapping("/all")
    public Result<List<Product>> getAllProducts() {
        return Result.success(dubboProductService.getAllProducts());
    }
}
