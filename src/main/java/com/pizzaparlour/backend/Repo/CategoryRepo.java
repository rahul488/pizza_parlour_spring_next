package com.pizzaparlour.backend.Repo;

import com.pizzaparlour.backend.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category, UUID> {

    public Category findByType(String type);
}
