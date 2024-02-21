package com.pizzaparlour.backend.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private UUID id;

    private String name;

    private String image;

    private String type;
}
