package com.pizzaparlour.backend.Repo;

import com.pizzaparlour.backend.Entity.Cart;
import com.pizzaparlour.backend.Entity.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepo extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByName(String username);

    Optional<Customer> findByEmail(String email);

    Page<Customer> findAll(Pageable pageable);

    Optional<Cart> findCartByEmail(String email);


}
