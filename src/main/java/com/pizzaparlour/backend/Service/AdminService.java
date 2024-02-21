package com.pizzaparlour.backend.Service;

import com.pizzaparlour.backend.Dto.Request.*;
import com.pizzaparlour.backend.Dto.Response.*;
import com.pizzaparlour.backend.Entity.*;
import com.pizzaparlour.backend.Exception.CustomException;
import com.pizzaparlour.backend.Repo.BannerRepo;
import com.pizzaparlour.backend.Repo.CustomerRepo;
import com.pizzaparlour.backend.Repo.OrderRepo;
import com.pizzaparlour.backend.helper.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.*;

@Service
public class AdminService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ServiceHelper serviceHelper;

    public CommonResponse<?> getAllCustomer(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepo.findAll(pageable);
        Page<CustomerTableResponse> resposne = customers.map((customer) -> {
            CustomerTableResponse customerResponseDto = new CustomerTableResponse();
            customerResponseDto.setId(customer.getId());
            customerResponseDto.setEmail(customer.getEmail());
            customerResponseDto.setName(customer.getName());
            customerResponseDto.setRole(customer.getRole());

            return customerResponseDto;
        });
        return new CommonResponse<>("Customer fetched successfully", true, resposne);
    }

    public CommonResponse<?> getAllProducts(int page, int size) {
        return productService.getProductByPage(page, size);
    }

    public CommonResponse<?> deleteProduct(UUID id) throws CustomException {
        return productService.deleteProduct(id);
    }

    public CommonResponse<?> updateProduct(UUID id, ProductRequestDto productRequestDto) {
        return productService.updateproduct(id, productRequestDto);
    }

    public CommonResponse<?> addDeals(DealsRequestDto dealsRequestDto) throws IOException {

        return productService.addDeals(dealsRequestDto);
    }

    public CommonResponse<?> addBanner(BannerRequestDto bannerRequestDto) throws IOException {
        return productService.addBanner(bannerRequestDto);
    }

    public CommonResponse<?> deleteBanner(UUID id) {
        return productService.deleteBanner(id);
    }

    public CommonResponse<?> updateBanner(BannerRequestDto bannerRequestDto, UUID id) throws IOException {

        return productService.updateBanner(bannerRequestDto,id);
    }

    public CommonResponse<?> updateDeal(DealsRequestDto dealsRequestDto, UUID id) throws IOException {
        return productService.updateDeal(dealsRequestDto,id);
    }

    public CommonResponse<?> deleteDeal(UUID id) {
        return productService.deleteDeal(id);
    }

//    Category operations start
    public CommonResponse<?> addCategory(CategoryRequestDto categoryRequestDto) throws CustomException, IOException {
        return productService.addProductCategory(categoryRequestDto);
    }

    public CommonResponse<?> updateCategory(CategoryRequestDto categoryRequestDto,UUID id) throws IOException {
        return productService.updateCategory(categoryRequestDto,id);
    }

    public CommonResponse<?> deleteCategory(UUID id){
        return  productService.deleteCategory(id);
    }


    public CommonResponse<?> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerOrder> customerOrders = orderRepo.findAll(pageable);
        Page<OrderResponse> orderResponseList = serviceHelper.getCustomerOrders(customerOrders);
        return new CommonResponse<>("Orders fetched successfully",true,orderResponseList);
    }

    public CommonResponse<?> updateOrder(UUID id, OrderUpdateRequestDTO dto) throws CustomException {
        CustomerOrder order = orderRepo.findById(id).orElse(null);

        if(order == null) throw new CustomException("Order Not Found");

        order.setOrderStatus(dto.getOrderStatus());

        orderRepo.save(order);

        return new CommonResponse<>("Order updated successfully",true,"");

    }

    public CommonResponse<?> addProduct(ProductRequestDto productRequestDto) throws CustomException, IOException {
        return productService.addProduct(productRequestDto);
    }
}
