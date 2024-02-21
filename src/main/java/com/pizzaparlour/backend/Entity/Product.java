package com.pizzaparlour.backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Column(name = "product_id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_price")
    private double price;

    @Column(name = "product_rating")
    private double rating;

    @Column(name = "product_type")
    private String type;

    @Lob
    @Column(name = "product_image",columnDefinition = "MEDIUMBLOB")
    @Transient
    private byte[] image;

    private String imagePath;

    @Column(name = "product_description",length = 400)
    private String desc;

    @ManyToMany(mappedBy = "productSet")
    @JsonIgnore
    @JsonIgnoreProperties("productSet")
    private Set<Cart> carts = new HashSet<>();

    @ManyToMany(mappedBy = "products",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    @JsonIgnore
    private Set<CustomerTop10Products> customerTop10Products=new HashSet<>();

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    @ToString.Exclude
    private Set<Deals> deals = new HashSet<>();

    @ManyToMany(mappedBy = "orderedProducts")
    @JsonIgnore
    private Set<CustomerOrder> customerOrders = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
