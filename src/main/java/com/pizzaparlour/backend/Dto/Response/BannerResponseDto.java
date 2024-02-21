package com.pizzaparlour.backend.Dto.Response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerResponseDto {

    private UUID id;

    private String name;

    private String desc;

    private String image;
    
}
