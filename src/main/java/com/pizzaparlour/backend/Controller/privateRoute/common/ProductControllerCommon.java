package com.pizzaparlour.backend.Controller.privateRoute.common;

import com.pizzaparlour.backend.Dto.Request.OrderRequestDTO;
import com.pizzaparlour.backend.Dto.Response.CommonResponse;
import com.pizzaparlour.backend.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pizza-parlour/common/private")
public class ProductControllerCommon {

    @Autowired
    private ProductService productService;

    @GetMapping("/getProduct/{type}")
    public CommonResponse<?> getProductByType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProductByCategory(type, page, size);
    }

    @GetMapping("/product/{productId}")
    public CommonResponse<?> getSingleProductById(@PathVariable String productId) {
        return productService.getSingleProduct(productId);
    }

    @GetMapping("/categories")
    public CommonResponse<?> getAllCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("/getBanner")
    public CommonResponse<?> getAllBanner( @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
        return productService.getAllBanner(page,size);
    }

    @GetMapping("/getAllDeals")
    public CommonResponse<?> getAllDeals(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size){
        return productService.getAllDeals(page,size);
    }

    @GetMapping("/deals/{id}")
    public CommonResponse<?> getDealsById(@PathVariable UUID id) {
        return productService.getDealDetails(id);
    }


}
