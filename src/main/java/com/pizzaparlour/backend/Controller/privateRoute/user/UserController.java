package com.pizzaparlour.backend.Controller.privateRoute.user;

import com.pizzaparlour.backend.Dto.Request.AddToCartRequestDto;
import com.pizzaparlour.backend.Dto.Request.ForgotPasswordRequestDto;
import com.pizzaparlour.backend.Dto.Request.OrderRequestDTO;
import com.pizzaparlour.backend.Dto.Response.CommonResponse;
import com.pizzaparlour.backend.Exception.CustomException;
import com.pizzaparlour.backend.Service.CustomerService;

import java.util.UUID;

import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pizza-parlour/customer/private")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/addToCart")
    public CommonResponse<?> addToCart(@RequestBody AddToCartRequestDto addToCartRequestDto) {
        return customerService.addToCart(addToCartRequestDto);
    }

    @PostMapping("/addDeals")
    public CommonResponse<?> addDealsToCart(@RequestBody AddToCartRequestDto addToCartRequestDto) {
        return customerService.addDealsToCart(addToCartRequestDto);
    }

    @GetMapping("/getCart")
    public CommonResponse<?> getCartDetails() throws CustomException {
        return customerService.getCartDetails();
    }

    @DeleteMapping("/removeFromCart/{id}")
    public CommonResponse<?> removeProduct(@PathVariable String id) throws CustomException {
        return customerService.deleteProductFromCart(UUID.fromString(id));
    }

    @GetMapping("/getAccountDetails")
    public CommonResponse<?> getProfileDetails() throws CustomException {
        return customerService.getCustomerDetails();
    }

    @PostMapping("/update-password")
    public CommonResponse<?> updatePassword(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
        return customerService.updateCustomerPassword(forgotPasswordRequestDto);
    }

    @GetMapping("/getTop10products")
    public CommonResponse<?> getTop10Products() throws CustomException {
        return customerService.getTop10Products();
    }

    @PostMapping("/create-order")
    public CommonResponse<?> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) throws CustomException, RazorpayException {
        return customerService.processPayment(orderRequestDTO);
    }

    @PostMapping("/complete-order")
    public CommonResponse<?> doPayment(@RequestBody OrderRequestDTO orderRequestDTO) throws CustomException {
        return customerService.saveOrder(orderRequestDTO);
    }

    @GetMapping("/get-order")
    public CommonResponse<?> getAllOrder(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) throws CustomException {
        return customerService.getAllOrders(page,size);
    }
}
