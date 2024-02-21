package com.pizzaparlour.backend.Dto.Response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTableResponse {

    private UUID id;

    private String name;

    private String email;

    private String role;
}
