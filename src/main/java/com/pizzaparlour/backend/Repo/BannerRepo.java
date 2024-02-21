package com.pizzaparlour.backend.Repo;

import com.pizzaparlour.backend.Entity.Banner;
import com.pizzaparlour.backend.Entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BannerRepo extends JpaRepository<Banner, UUID> {
    Page<Banner> findAll(Pageable pageable);

}
