package com.pizzaparlour.backend.Dto.Request;

import com.pizzaparlour.backend.Entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateRequestDTO {

    private OrderStatus orderStatus;
}
