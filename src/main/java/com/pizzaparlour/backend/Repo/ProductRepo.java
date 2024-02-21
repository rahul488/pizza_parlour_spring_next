package com.pizzaparlour.backend.Repo;

import com.pizzaparlour.backend.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, UUID> {

    Page<Product> findAll(Pageable pageable);


    Page<Product> findByType(String type,Pageable pageable);
}
