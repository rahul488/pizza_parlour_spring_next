package com.pizzaparlour.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deals {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "deal_products",
            joinColumns = @JoinColumn(name = "deals_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    @ManyToMany(mappedBy = "dealsSet")
    @JsonIgnore
    private Set<Cart> carts = new HashSet<>();

    @ManyToMany(mappedBy = "orderedDealProducts")
    @JsonIgnore
    private Set<CustomerOrder> customerOrders = new HashSet<>();

    private double discount;

    private double price;

    private String productDesc;

    private String imagePath;
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
