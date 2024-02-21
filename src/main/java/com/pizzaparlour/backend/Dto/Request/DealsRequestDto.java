package com.pizzaparlour.backend.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealsRequestDto {

    private String name;

    private double price;

    private double discount;

    private MultipartFile image;

    private List<UUID> productIds;

    private String desc;
}
