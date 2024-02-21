package com.pizzaparlour.backend.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private double price;

    private String message;

    private String orderId;

    private String appName;

    private String customerName;

    private String customerEmail;

}
