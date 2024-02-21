package com.pizzaparlour.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTop10Products {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "top10_products",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();

    private LocalDateTime lastAccessTime;

    @OneToOne()
    @JoinColumn(name="customer_id")
    @JsonManagedReference
    private Customer customer;

    @Override
    public int hashCode() {
        // Exclude the bidirectional relationship from hashCode , as i have used collections for data storing
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CustomerTop10Products{" +
                "lastAccessTime=" + lastAccessTime +
                '}';
    }
}
