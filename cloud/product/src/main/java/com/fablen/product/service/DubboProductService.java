package com.fablen.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fablen.api.product.ProductService;
import com.fablen.product.entity.Product;
import com.fablen.product.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@DubboService(version = "1.0.0", group = "product-service")
@Service
public class DubboProductService implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    public Page<Product> getAllProductsPageable(Page<Product> page) {
        return productMapper.selectPage(page, null);
    }

    public Page<Product> searchProducts(String name, Page<Product> page) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        return productMapper.selectPage(page, queryWrapper);
    }

    public List<Product> getAllProducts() {
        return productMapper.selectList(null);
    }

    public Product addProduct(Product product) {
        productMapper.insert(product);
        return product;
    }

    public Product editProduct(Long id, Product productDetails) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        product.setName(productDetails.getName());
        productMapper.updateById(product);
        return product;
    }

    public void deleteProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        productMapper.deleteById(id);
    }

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
