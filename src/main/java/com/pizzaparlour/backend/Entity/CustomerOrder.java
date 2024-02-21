package com.pizzaparlour.backend.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToMany
     @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnore
    private Set<Product> orderedProducts = new HashSet<Product>();

    @ManyToMany
    @JoinTable(
            name = "order_deals",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "deal_id")
    )
    @JsonIgnore
    private Set<Deals> orderedDealProducts = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "order_products_quantity", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> productsQuantity = new HashMap<UUID,Integer>();
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String receiverName;

    private String receiverEmail;

    private String state;

    private String pinCode;

    private String city;

    private String district;

    private String address;

    private String phoneNumber;

    private String altPhoneNumber;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "order=" + orderStatus +
                '}';
    }

}
