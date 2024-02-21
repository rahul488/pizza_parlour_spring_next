package com.pizzaparlour.backend.Repo;

import com.pizzaparlour.backend.Entity.Customer;
import com.pizzaparlour.backend.Entity.CustomerTop10Products;
import com.pizzaparlour.backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Top10ProductRepo extends JpaRepository<CustomerTop10Products, UUID> {
    @Query("SELECT c FROM CustomerTop10Products c WHERE c.customer.id = :customerId AND c.lastAccessTime > :thresholdTime")
    Optional<CustomerTop10Products> findByLastAccessTime(@Param("customerId") UUID customerId, @Param("thresholdTime") LocalDateTime thresholdTime);

    @Query("SELECT c.products FROM CustomerTop10Products c WHERE c.customer.id = :customerId")
    List<Product> findProductsByCustomerId(@Param("customerId") UUID customerId);

}
