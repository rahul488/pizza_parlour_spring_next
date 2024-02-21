package com.pizzaparlour.backend.Repo;

import com.pizzaparlour.backend.Entity.Customer;
import com.pizzaparlour.backend.Entity.Deals;
import com.pizzaparlour.backend.Entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface DealsRepo extends JpaRepository<Deals, UUID> {

    @Query("SELECT d.products FROM Deals d WHERE d.id = :dealsId")
    List<Product> findDealProductsByDealId(@Param("dealsId") UUID dealsId);

     Page<Deals> findAll(Pageable pageable);

}
