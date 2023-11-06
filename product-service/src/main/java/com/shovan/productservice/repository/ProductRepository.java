package com.shovan.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shovan.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
