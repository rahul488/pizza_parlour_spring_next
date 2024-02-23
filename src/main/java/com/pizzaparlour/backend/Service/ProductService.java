package com.pizzaparlour.backend.Service;

import com.pizzaparlour.backend.Dto.Request.*;
import com.pizzaparlour.backend.Dto.Response.*;
import com.pizzaparlour.backend.Entity.*;
import com.pizzaparlour.backend.Exception.CustomException;
import com.pizzaparlour.backend.Exception.ProductNotFoundException;
import com.pizzaparlour.backend.Repo.BannerRepo;
import com.pizzaparlour.backend.Repo.CategoryRepo;
import com.pizzaparlour.backend.Repo.DealsRepo;
import com.pizzaparlour.backend.Repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private BannerRepo bannerRepo;

    @Autowired
    private DealsRepo dealsRepo;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public CommonResponse<?> addProduct(ProductRequestDto productDto) throws CustomException, IOException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDesc(productDto.getDesc());
        product.setRating(productDto.getRating());
        product.setType(productDto.getType());
        product.setImagePath(uploadImage(productDto.getImage()));
        productRepo.save(product);
        return new CommonResponse<>("Product Added Successfully.",true,"");
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

    public CommonResponse<?> deleteProduct(UUID id) throws CustomException {

        Product product = productRepo.findById(id).orElse(null);

        if(product == null){
            throw new ProductNotFoundException("Product not found");
        }
        product.getCustomerTop10Products().forEach(customerTop10Products -> {
            customerTop10Products.getProducts().remove(product);
        });

        product.getCarts().forEach(cart ->{
            cart.getProductSet().remove(product);
            cart.getProductsQuantity().remove(product.getId());
        });
        try{
            productRepo.deleteById(id);
        }catch(Exception e){
            throw new CustomException("This product can't be deleted");
        }

        return new CommonResponse<>("Product deleted successfully",true,"");

    }

    public CommonResponse<?> updateproduct(UUID id, ProductRequestDto productRequestDto) {
        Product product = productRepo.findById(id).orElse(null);

        if(product == null){
            throw new ProductNotFoundException("Product not found for update");
        }
        product.setName(productRequestDto.getName());
        product.setPrice(product.getPrice());
        product.setDesc(product.getDesc());
        try {
            product.setImagePath(uploadImage(productRequestDto.getImage()));
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        productRepo.save(product);

        return new CommonResponse<>("Product update successfully",true,"");
    }

    public CommonResponse<?> getAllCategories(){
        List<Category> categories = categoryRepo.findAll();
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        categories.forEach((cat) -> {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(cat.getId());
            categoryResponse.setType(cat.getType());
            categoryResponse.setName(cat.getName());
            categoryResponse.setImage(getCategoryImages(cat.getId()));
            categoryResponseList.add(categoryResponse);
        });
        return new CommonResponse<>("Category Fetched Successfully",true,categoryResponseList);
    }

    public CommonResponse<?> getProductByPage(int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepo.findAll(pageable);
        Page<ProductResponseDto> productResponseDtos = products.map((product -> {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setId(product.getId());
            productResponseDto.setPrice(product.getPrice());
            productResponseDto.setRating(product.getRating());
            productResponseDto.setImage(getProductImage(product.getId()));
            productResponseDto.setDesc(product.getDesc());
            productResponseDto.setName(product.getName());
            productResponseDto.setType(product.getType());
            return productResponseDto;
        }));
        return new CommonResponse<>("Product fetched successfully",true,productResponseDtos);
    }
    public CommonResponse<?> getProductByCategory(String type,int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> products = productRepo.findByType(type,pageable);
        Category category = categoryRepo.findByType(type);
        List<ProductResponseDto> productResponseDto = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDto pr = new ProductResponseDto();
            pr.setName(product.getName());
            pr.setDesc(product.getDesc());
            pr.setPrice(product.getPrice());
            pr.setRating(product.getRating());
            pr.setImage(getProductImage(product.getId()));
            pr.setId(product.getId());
            productResponseDto.add(pr);

        }
        Page<ProductResponseDto> responsePage = new PageImpl<>(productResponseDto, pageable, products.getTotalElements());
        PageableResponse<?> response = new PageableResponse<>(responsePage,getCategoryImages(category.getId()));
        return new CommonResponse<>("Product Fetched Successfully",true,response);
    }
    public ResponseEntity<byte[]> getImageByProductId(UUID id){
        Optional<Product> productOptional = productRepo.findById(id);
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(product.getImage());
        }else{
            throw new ProductNotFoundException("Product not found");
        }
    }
    public CommonResponse<?> addBanner(BannerRequestDto bannerRequestDto) throws IOException{
        Banner banner = new Banner();
        banner.setName(bannerRequestDto.getName());
        banner.setDesc(bannerRequestDto.getDesc());
        banner.setImage(uploadImage(bannerRequestDto.getImage()));

        bannerRepo.save(banner);

        return new CommonResponse<>("Banner Added Successfully",true,"");
    }

    public CommonResponse<?> getAllBanner(int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Banner> banners = bannerRepo.findAll(pageable);
        List<BannerResponseDto> bannerResponseDtos = new ArrayList<>();
        for (Banner banner : banners) {
            BannerResponseDto bannerResponseDto = new BannerResponseDto();
            bannerResponseDto.setId(banner.getId());
            bannerResponseDto.setName(banner.getName());
            bannerResponseDto.setDesc(banner.getDesc());
            bannerResponseDto.setImage(banner.getName());
            bannerResponseDto.setImage(getBannerImages(banner.getId()));
            bannerResponseDtos.add(bannerResponseDto);
        }
        Page<BannerResponseDto> responsePage = new PageImpl<>(bannerResponseDtos, pageable, banners.getTotalElements());

        return new CommonResponse<>("Banner Fetched Successfully",true,responsePage);
    }

    public CommonResponse<?> deleteBanner(UUID id) {
        Banner banner = bannerRepo.findById(id).orElse(null);

        if(banner == null) throw new ProductNotFoundException("Banner Not Found");

        bannerRepo.deleteById(id);
        return new CommonResponse<>("Banner deleted successfully",true,"");
    }

    public CommonResponse<?> updateBanner(BannerRequestDto bannerRequestDto,UUID id) throws IOException {
        Banner banner = bannerRepo.findById(id).orElse(null);
        if(banner == null) throw new ProductNotFoundException("Banner Not Found");
        banner.setName(bannerRequestDto.getName());
        banner.setImage(uploadImage(bannerRequestDto.getImage()));
        banner.setDesc(bannerRequestDto.getDesc());

        bannerRepo.save(banner);

        return new CommonResponse<>("Banner updated successfully",true,"");
    }

    public CommonResponse<?> getSingleProduct(String id){
        Optional<Product> productOptional = productRepo.findById(UUID.fromString(id));
        if(!productOptional.isPresent()){
           throw new ProductNotFoundException("Product Not Found");
        }
        Product product = productOptional.get();
        ProductResponseDto pr = new ProductResponseDto();
        pr.setName(product.getName());
        pr.setDesc(product.getDesc());
        pr.setPrice(product.getPrice());
        pr.setRating(product.getRating());
        pr.setImage(getProductImage(product.getId()));
        pr.setId(product.getId());
        pr.setBackgroundImage(getProductImage(product.getId()));

        return new CommonResponse<>("Product details fetched successfully",true,pr);

    }

    public CommonResponse<?> addDeals(DealsRequestDto dealsRequestDto) throws IOException {
        Deals deals = new Deals();
        Set<Product> dealProducts = new HashSet<>();
        Set<Deals> productDeals = new HashSet<>();
        double price=0;
        for(UUID id : dealsRequestDto.getProductIds() ){
            Product product = productRepo.findById(id).orElse(null);
            try{
                if(product == null){
                    throw new ProductNotFoundException("Product Not Found");
                }
                price+=product.getPrice();
                dealProducts.add(product);
                productDeals.add(deals);
                product.setDeals(productDeals);
            }catch (ProductNotFoundException e){
                logger.error("Product not found with id",id);
            }
        }
        deals.setName(dealsRequestDto.getName());
        deals.setImagePath(uploadImage(dealsRequestDto.getImage()));
        deals.setProducts(dealProducts);
        deals.setProductDesc(dealsRequestDto.getDesc());
        deals.setDiscount(dealsRequestDto.getDiscount());
        
        deals.setPrice(price-((price*dealsRequestDto.getDiscount())/100));
        dealsRepo.save(deals);
       return new CommonResponse<>("Deals added Successfully",true,"");
    }

    public CommonResponse<?> updateDeal(DealsRequestDto dealsRequestDto,UUID id) throws IOException {
        Deals deals = dealsRepo.findById(id).orElse(null);

        if(deals == null) throw new ProductNotFoundException("Deal not found");
        Set<Product> productSet = deals.getProducts();
        Set<Deals> productDeals = new HashSet<>();
        double price=0;
        for(UUID productId : dealsRequestDto.getProductIds() ){
            Product product = productRepo.findById(productId).orElse(null);
            try{
                if(product == null){
                    throw new ProductNotFoundException("Product Not Found");
                }
                price+=product.getPrice();
                productSet.add(product);
                productDeals.add(deals);
                product.setDeals(productDeals);
            }catch (ProductNotFoundException e){
                logger.error("Product not found with id",id);
            }
        }
        deals.setName(dealsRequestDto.getName());
        deals.setImagePath(uploadImage(dealsRequestDto.getImage()));
        deals.setProducts(productSet);
        deals.setProductDesc(dealsRequestDto.getDesc());
        deals.setDiscount(dealsRequestDto.getDiscount());

        deals.setPrice(price-((price*dealsRequestDto.getDiscount())/100));
        dealsRepo.save(deals);

        return new CommonResponse<>("Deals updated successfully",true,"");
    }

    public CommonResponse<?> getAllDeals(int page,int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Deals> deals = dealsRepo.findAll(pageable);
        Page<DealResponse> response = deals.map((deal -> {
            DealResponse dealResponse = new DealResponse();
            dealResponse.setId(deal.getId());
            dealResponse.setPrice(deal.getPrice());
            dealResponse.setDesc(deal.getProductDesc());
            dealResponse.setName(deal.getName());
            dealResponse.setImage(getProductImage(deal.getId()));
            dealResponse.setDiscount(deal.getDiscount());
            List<Product> prd = dealsRepo.findDealProductsByDealId(deal.getId());
            List<ProductResponseDto> productResponse = generateProductResponseDto(prd);
            dealResponse.setProducts(productResponse);
            return dealResponse;
        }));
        return new CommonResponse<>("Deals fetched successfully",true,response);

    }

    public CommonResponse getDealDetails(UUID id) {
        Deals deal = dealsRepo.findById(id).orElse(null);
        if(deal == null){
            throw new ProductNotFoundException("Particular deals is not exist any more");
        }
        List<Product> products = dealsRepo.findDealProductsByDealId(deal.getId());
        DealResponse dealResponse = new DealResponse();
        List<ProductResponseDto> productResponseDto = generateProductResponseDto(products);
        dealResponse.setId(deal.getId());
        dealResponse.setPrice(deal.getPrice());
        dealResponse.setImage(getProductImage(deal.getId()));
        dealResponse.setName(deal.getName());
        dealResponse.setDesc(deal.getProductDesc());
        dealResponse.setProducts(productResponseDto);
        dealResponse.setBackgroundImage(getProductImage(deal.getId()));
        return new CommonResponse<>("Deal details fetched successfully",true,dealResponse);
    }

    public CommonResponse<?> deleteDeal(UUID id) {
        Deals deal = dealsRepo.findById(id).orElse(null);
        if(deal == null){
            throw new ProductNotFoundException("Deal not found");
        }
        deal.getProducts().forEach((product) -> {
            product.getDeals().remove(deal);
        });
        deal.getCarts().forEach((carts) -> {
            carts.getDealsSet().remove(deal);
        });

        dealsRepo.deleteById(id);

        return new CommonResponse<>("Deals deleted successfully",true,"");
    }

    public CommonResponse<?> addProductCategory(CategoryRequestDto categoryRequestDto) throws CustomException, IOException {
        Category category= new Category();
        category.setName(categoryRequestDto.getName());
        category.setType(categoryRequestDto.getType());
        category.setImagePath(uploadImage(categoryRequestDto.getImage()));
        categoryRepo.save(category);
        return new CommonResponse<>("Category Added Successfully.",true,"");
    }
    public CommonResponse<?> updateCategory(CategoryRequestDto categoryRequestDto,UUID id) throws IOException {

        Category category =  categoryRepo.findById(id).orElse(null);

        if(category == null){
            throw new ProductNotFoundException("Category Not Found");
        }
        category.setImagePath(uploadImage(categoryRequestDto.getImage()));

        category.setName(categoryRequestDto.getName());
        category.setType(categoryRequestDto.getType());

        categoryRepo.save(category);

        return new CommonResponse<>("Category updated successfully",true,"");
    }

    public CommonResponse<?> deleteCategory(UUID id) {
        Category category =  categoryRepo.findById(id).orElse(null);

        if(category == null){
            throw new ProductNotFoundException("Category Not Found");
        }

        categoryRepo.deleteById(id);

        return new CommonResponse<>("Category deleted successfully",true,"");
    }

    public String uploadImage(MultipartFile file) throws IOException{
        String uploadDir = "src/main/resources/static/images";
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(),filePath);
        return filePath.toString();
    }
    public ResponseEntity<byte[]> getImage(UUID id) throws IOException {
        Product product = productRepo.findById(id).orElse(null);
        Deals deals = null;
        if(product == null){
            deals = dealsRepo.findById(id).orElse(null);
            if(deals == null)
                throw new ProductNotFoundException("Product not found");
        }
        String imagePath="";
        if(product != null){
            imagePath = product.getImagePath();
        }
        else if(deals != null){
            imagePath = deals.getImagePath();
        }

        byte[] images = Files.readAllBytes(new File(imagePath).toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .headers(headers)
                .body(images);
    }
    public ResponseEntity<byte[]> getBannerImage(UUID id) throws IOException {
        Banner banner = bannerRepo.findById(id).orElse(null);
        if(banner == null){
            throw new ProductNotFoundException("Banner not found");
        }
        String imagePath = banner.getImage();
        byte[] images = Files.readAllBytes(new File(imagePath).toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .headers(headers)
                .body(images);
    }

    public ResponseEntity<byte[]> getCategoryImage(UUID id) throws IOException {
        Category category = categoryRepo.findById(id).orElse(null);
        if(category == null){
            throw new ProductNotFoundException("Banner not found");
        }
        String imagePath = category.getImagePath();
        byte[] images = Files.readAllBytes(new File(imagePath).toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .headers(headers)
                .body(images);
    }
    //http://localhost:8080/pizza-parlour/public/img/"+id
    public String getProductImage(UUID id){
        return "https://pizza-parlour.onrender.com/pizza-parlour/public/img/"+id;
    }
    public String getBannerImages(UUID id){
        return "https://pizza-parlour.onrender.com/pizza-parlour/public/bnrImg/"+id;
    }
    public String getCategoryImages(UUID id){
        return "https://pizza-parlour.onrender.com/pizza-parlour/public/ctgImg/"+id;
    }

}

