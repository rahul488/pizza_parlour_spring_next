package com.pizzaparlour.backend.Controller.privateRoute.admin;

import com.pizzaparlour.backend.Dto.Request.*;
import com.pizzaparlour.backend.Dto.Response.CommonResponse;
import com.pizzaparlour.backend.Entity.OrderStatus;
import com.pizzaparlour.backend.Exception.CustomException;
import com.pizzaparlour.backend.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/pizza-parlour/admin/private/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //TODO:-handle delete for product which is in deal.

    @GetMapping("/customer")
    public CommonResponse<?> getAllCustomer( @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size){
        return adminService.getAllCustomer(page,size);
    }

    @GetMapping("/getAllProducts")
    public CommonResponse<?> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.getAllProducts(page,size);
    }

    @PostMapping("/product/add")
    public CommonResponse<?> addProduct(@ModelAttribute ProductRequestDto productRequestDto) throws CustomException, IOException {
        return adminService.addProduct(productRequestDto);
    }

    @DeleteMapping("/product/delete/{id}")
    public CommonResponse<?> deleteProduct(@PathVariable UUID id) throws CustomException {
        return adminService.deleteProduct(id);
    }

    @PutMapping("/product/update/{id}")
    public CommonResponse<?> updateProduct(@PathVariable UUID id, @ModelAttribute ProductRequestDto productRequestDto){
        return adminService.updateProduct(id,productRequestDto);
    }

    @PostMapping("/addDeals")
    public CommonResponse<?> addDeals(@ModelAttribute DealsRequestDto dealsRequestDto) throws IOException{
        return adminService.addDeals(dealsRequestDto);
    }
    @PutMapping("/deals/{id}")
    public CommonResponse<?> editDeal(@ModelAttribute DealsRequestDto dealsRequestDto,@PathVariable UUID id) throws IOException{
        return adminService.updateDeal(dealsRequestDto,id);
    }
    @DeleteMapping("/delete/deals/{id}")
    public CommonResponse<?> deleteDeal(@PathVariable UUID id){
        return adminService.deleteDeal(id);
    }

    @PostMapping("/addBanner")
    public CommonResponse<?> addBanner(@ModelAttribute BannerRequestDto bannerRequestDto) throws IOException{
        return adminService.addBanner(bannerRequestDto);
    }
    @PutMapping("/updateBanner/{id}")
    public CommonResponse<?> updateBanner(@ModelAttribute BannerRequestDto bannerRequestDto,@PathVariable UUID id) throws IOException{
        return adminService.updateBanner(bannerRequestDto,id);
    }
    @DeleteMapping("/deleteBanner/{id}")
    public CommonResponse<?> deleteBanner(@PathVariable UUID id) {
        return adminService.deleteBanner(id);
    }

    @PostMapping("/addCategory")
    public CommonResponse<?> addCategory(@ModelAttribute CategoryRequestDto categoryRequestDto) throws CustomException, IOException {
        return adminService.addCategory(categoryRequestDto);
    }

    @PutMapping("/updateCategory/{id}")
    public CommonResponse<?> updateCategory(@PathVariable UUID id,@ModelAttribute CategoryRequestDto categoryRequestDto) throws CustomException, IOException {
        return adminService.updateCategory(categoryRequestDto,id);
    }

    @GetMapping("/getAllOrders")
    public CommonResponse<?> getAllOrders(  @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size){
        return adminService.getAllOrders(page,size);
    }
    @PutMapping("/updateOrder/{id}")
    public CommonResponse<?> updateOrder(@PathVariable UUID id, @RequestBody OrderUpdateRequestDTO orderUpdateRequestDTO) throws CustomException {
        return adminService.updateOrder(id,orderUpdateRequestDTO);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public CommonResponse<?> deleteCategory(@PathVariable UUID id)  {
        return adminService.deleteCategory(id);
    }
}
