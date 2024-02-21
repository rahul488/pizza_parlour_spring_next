package com.pizzaparlour.backend.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {

    private UUID id;

    private String name;

    private String email;

    private String accessToken;

    private List<SimpleGrantedAuthority> role;
}
