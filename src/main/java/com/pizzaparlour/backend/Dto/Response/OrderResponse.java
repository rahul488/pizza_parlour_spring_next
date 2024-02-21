package com.pizzaparlour.backend.Dto.Response;
import com.pizzaparlour.backend.Entity.Deals;
import com.pizzaparlour.backend.Entity.OrderStatus;
import com.pizzaparlour.backend.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private UUID id;

    private List<ProductResponseDto> orderedProducts = new ArrayList<>();

    private List<DealResponse> orderDeals = new ArrayList<>();

    private Map<UUID, Integer> productsQuantity = new HashMap<UUID,Integer>();

    private String customerEmail;

    private double totalPrice;

    private OrderStatus orderStatus;

    private String receiverName;

    private String receiverEmail;

    private String state;

    private String pinCode;

    private String city;

    private String district;

    private String address;

    private String phoneNumber;

    private String altPhoneNumber;

    private String backgroundImage;

    private List<OrderStatus> updatableStatus = new ArrayList<>();
}
