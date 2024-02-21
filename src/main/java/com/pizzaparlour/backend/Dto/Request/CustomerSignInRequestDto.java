package com.pizzaparlour.backend.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSignInRequestDto {

    private String email;

    private String password;
}
