package com.pizzaparlour.backend.Controller.PublicRoute;

import com.pizzaparlour.backend.Dto.Request.CustomerSignInRequestDto;
import com.pizzaparlour.backend.Dto.Request.CustomerSignupRequestDto;
import com.pizzaparlour.backend.Dto.Request.ForgotPasswordRequestDto;
import com.pizzaparlour.backend.Dto.Response.CommonResponse;
import com.pizzaparlour.backend.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pizza-parlour/public")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/createAccount")
    public ResponseEntity<CommonResponse> saveUser(@RequestBody CustomerSignupRequestDto customerRequestDto){
        return new ResponseEntity<>(customerService.signup(customerRequestDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> loginUser(@RequestBody CustomerSignInRequestDto customerSignInRequestDto){
        return  new ResponseEntity<>(customerService.createToken(customerSignInRequestDto),HttpStatus.OK);
    }

    @PostMapping("/update-password")
    public CommonResponse updatePassword(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto){
    return customerService.updateCustomerPassword(forgotPasswordRequestDto);
    }


}
