package com.pizzaparlour.backend.Dto.Request;

import com.pizzaparlour.backend.Entity.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    private Map<UUID,Integer> orderedProducts = new HashMap<>();

    private double price;

    private PaymentMode paymentMode;

    private String transactionId;

    private String receiverName;

    private String receiverEmail;

    private String state;

    private String pinCode;

    private String city;

    private String district;

    private String address;

    private String phoneNumber;

    private String altPhoneNumber;

}
