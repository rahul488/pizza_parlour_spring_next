package com.pizzaparlour.backend.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    private String name;

    private String type;

    private double price;

    private double rating;

    private MultipartFile image;

    private String desc;
}
