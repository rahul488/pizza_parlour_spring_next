package com.pizzaparlour.backend.Repo;

import com.pizzaparlour.backend.Entity.Cart;
import com.pizzaparlour.backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CartRepo extends JpaRepository<Cart, UUID> {

//    @Query(value = "SELECT p.* FROM cart c JOIN cart_products cp ON c.cart_id = cp.cart_id JOIN product p ON cp.product_id = p.product_id WHERE c.cart_id = :cartId", nativeQuery = true)
//    List<Product> findProductsByCartId(@Param("cartId") UUID cartId);
}
