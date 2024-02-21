package com.pizzaparlour.backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Column(name="customer_id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_role")
    private String role="ROLE_USER";

    @Column(name = "customer_email")
    private String email;

    @Column(name = "customer_password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "customer")
    @JsonBackReference
    @JsonIgnore
    private CustomerTop10Products top10Products;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CustomerOrder> orders = new ArrayList<CustomerOrder>();

    @Override
    public int hashCode() {
        // Exclude the bidirectional relationship from hashCode
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
