package com.pizzaparlour.backend.helper;

import com.pizzaparlour.backend.Config.CustomerDetails;
import com.pizzaparlour.backend.Dto.Response.DealResponse;
import com.pizzaparlour.backend.Dto.Response.OrderResponse;
import com.pizzaparlour.backend.Dto.Response.ProductResponseDto;
import com.pizzaparlour.backend.Entity.Customer;
import com.pizzaparlour.backend.Entity.CustomerOrder;
import com.pizzaparlour.backend.Entity.OrderStatus;
import com.pizzaparlour.backend.Entity.Product;
import com.pizzaparlour.backend.Exception.CustomException;
import com.pizzaparlour.backend.Exception.CustomerNotFoundException;
import com.pizzaparlour.backend.Repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ServiceHelper {

    @Autowired
    private CustomerRepo customerRepo;

    public Customer getCurrentCustomer() throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new CustomException("User is not authorised");
        }
        CustomerDetails customerDetails = (CustomerDetails) authentication.getPrincipal();
        String email = customerDetails.getUsername();
        Customer customer = customerRepo.findByEmail(email).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }

        return customer;
    }

    public List<ProductResponseDto> generateProductResponseDto(List<Product> products){
        List<ProductResponseDto> productResponse = new ArrayList<>();
        for(Product product:products){
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setName(product.getName());
            productResponseDto.setPrice(product.getPrice());
            productResponseDto.setRating(product.getRating());
            productResponseDto.setImage(getProductImage(product.getId()));
            productResponseDto.setDesc(product.getDesc());
            productResponseDto.setId(product.getId());
            productResponse.add(productResponseDto);
        }
        return productResponse;
    }

    public Page<OrderResponse> getCustomerOrders(Page<CustomerOrder> customerOrders ){
        Page<OrderResponse> orderResponseList = customerOrders.map((order) -> {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.getId());
            orderResponse.setOrderStatus(order.getOrderStatus());
            orderResponse.setOrderedProducts(generateProductResponseDto(order.getOrderedProducts().stream().toList()));
            orderResponse.setProductsQuantity(order.getProductsQuantity());
            orderResponse.setCity(order.getCity());
            List<DealResponse> dealResponses = new ArrayList<>();
            order.getOrderedDealProducts().forEach((deal) -> {
                DealResponse dealResponse = new DealResponse();
                dealResponse.setId(deal.getId());
                dealResponse.setPrice(deal.getPrice());
                dealResponse.setDesc(deal.getProductDesc());
                dealResponse.setName(deal.getName());
                dealResponse.setImage(getProductImage(deal.getId()));
                dealResponse.setDiscount(deal.getDiscount());
                Set<Product> prd = deal.getProducts();
                List<ProductResponseDto> productResponse = generateProductResponseDto(prd.stream().toList());
                dealResponse.setProducts(productResponse);
                dealResponses.add(dealResponse);
            });
            orderResponse.setOrderDeals(dealResponses);
            orderResponse.setAddress(order.getAddress());
            orderResponse.setDistrict(order.getDistrict());
            orderResponse.setCustomerEmail(order.getCustomer().getEmail());
            orderResponse.setReceiverEmail(order.getReceiverEmail());
            orderResponse.setReceiverName(order.getReceiverName());
            orderResponse.setPhoneNumber(order.getPhoneNumber());
            orderResponse.setAltPhoneNumber(order.getAltPhoneNumber());
            orderResponse.setTotalPrice(order.getTotalPrice());
            orderResponse.setPinCode(order.getPinCode());
            orderResponse.setState(order.getState());
            orderResponse.setUpdatableStatus(Arrays.stream(OrderStatus.values()).filter((status) -> status != order.getOrderStatus()).toList());
            return orderResponse;
        });
        return orderResponseList;

    }

    public String getProductImage(UUID id){
        return "http://localhost:8080/pizza-parlour/public/img/"+id;
    }
}
