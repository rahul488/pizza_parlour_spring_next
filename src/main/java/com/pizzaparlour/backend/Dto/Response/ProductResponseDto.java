package com.pizzaparlour.backend.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private UUID id;

    private String name;

    private double price;

    private String image;

    private String desc;

    private double rating;

    private String type;

    private String backgroundImage;
}
