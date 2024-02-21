package com.pizzaparlour.backend.Dto.Response;

import com.pizzaparlour.backend.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealResponse {

    private UUID id;

    private String name;

    private double discount;

    private double price;

    private String desc;

    private String image;

    private List<ProductResponseDto> products;

    private String backgroundImage;
}
