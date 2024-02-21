package com.pizzaparlour.backend.Repo;

import com.pizzaparlour.backend.Entity.CustomerOrder;
import com.pizzaparlour.backend.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepo extends JpaRepository<CustomerOrder, UUID> {

    Page<CustomerOrder> findAll(Pageable pageable);

    @Query("SELECT c FROM CustomerOrder c WHERE c.customer.id = :customerId")
    Page<CustomerOrder> findOrderByCustomerId(@Param("customerId") UUID customerId,Pageable pageable);
}
