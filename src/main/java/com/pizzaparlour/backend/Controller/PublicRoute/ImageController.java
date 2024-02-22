package com.pizzaparlour.backend.Controller.PublicRoute;

import com.pizzaparlour.backend.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/pizza-parlour/public")
public class ImageController {

    @Autowired
    private ProductService productService;


    @GetMapping("/img/{id}")
    public ResponseEntity<byte[]> productOrDealsImage(@PathVariable UUID id) throws IOException {
        return productService.getImage(id);
    }
    @GetMapping("/bnrImg/{id}")
    public ResponseEntity<byte[]> bannerImage(@PathVariable UUID id) throws IOException {
        return productService.getBannerImage(id);
    }
    @GetMapping("/ctgImg/{id}")
    public ResponseEntity<byte[]> categoryImage(@PathVariable UUID id) throws IOException {
        return productService.getCategoryImage(id);
    }
}
