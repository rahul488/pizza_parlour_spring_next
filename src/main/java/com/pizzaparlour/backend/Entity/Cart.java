package com.pizzaparlour.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@EqualsAndHashCode(exclude="dependent_list")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Column(name = "cart_id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "cart")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnore
    private Set<Product> productSet = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "cart_deals",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "deal_is")
    )
    @JsonIgnore
    private Set<Deals> dealsSet = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "cart_products_quantity", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> productsQuantity = new HashMap<>();

}
