package com.pizzaparlour.backend.Service;

import com.pizzaparlour.backend.Config.CustomerDetails;
import com.pizzaparlour.backend.Config.CustomerInfo;
import com.pizzaparlour.backend.Dto.Request.*;
import com.pizzaparlour.backend.Dto.Response.*;
import com.pizzaparlour.backend.Entity.*;
import com.pizzaparlour.backend.Exception.*;
import com.pizzaparlour.backend.Repo.*;
import com.pizzaparlour.backend.Util.JwtUtil;
import com.pizzaparlour.backend.helper.ServiceHelper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationProvider authManager;

    @Autowired
    private CustomerInfo customerInfo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private Top10ProductRepo top10ProductRepo;

    @Autowired
    private DealsRepo dealsRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Value("${rzp_key_id}")
    private String apiKey;

    @Value("${rzp_key_secret}")
    private String secret;

    @Value("${rzp_company_name}")
    private String company;

    @Value("${rzp_currency}")
    private String currency;

    @Autowired
    private ServiceHelper serviceHelper;


    public CommonResponse<?> signup(@RequestBody CustomerSignupRequestDto customerRequestDto) {

        Customer isCustomerExist = customerRepo.findByEmail(customerRequestDto.getEmail()).orElse(null);
        if (isCustomerExist != null) {
            throw new EmailAlreadyExistException("Email Already Exist");
        }
        Customer customer = new Customer();
        customer.setEmail(customerRequestDto.getEmail());
        customer.setName(customerRequestDto.getName());
        customer.setPassword(passwordEncoder.encode(customerRequestDto.getPassword()));
        if (customerRequestDto.getUserRole() != null && customerRequestDto.getUserRole().equalsIgnoreCase("admin")) {
            customer.setRole("ROLE_ADMIN");
        } else {
            customer.setRole("ROLE_USER");
        }
        customerRepo.save(customer);
        return new CommonResponse<>("Account created successfully", true, "");
    }

    public CommonResponse<?> createToken(@RequestBody CustomerSignInRequestDto customerSignInRequestDto) {
        CustomerDetails userDetails = (CustomerDetails) customerInfo
                .loadUserByUsername(customerSignInRequestDto.getEmail());
        String token = null;
        String userName = customerSignInRequestDto.getEmail();
        String password = customerSignInRequestDto.getPassword();
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid U/P");
        }
        token = jwtUtil.generateToken(userDetails);
        return new CommonResponse<>("Login success", true, new CustomerResponseDto(null, userDetails.getName(),
                userDetails.getUsername(), token, (List<SimpleGrantedAuthority>) userDetails.getAuthorities()));
    }

    public CommonResponse<?> addToCart(AddToCartRequestDto addToCartRequestDto) {
        Customer customer = customerRepo.findByEmail(addToCartRequestDto.getCustomer_email()).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        Product product = productRepo.findById(addToCartRequestDto.getProduct_id()).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with id:-" + addToCartRequestDto.getProduct_id());
        }

        // for cart
        Set<Product> productSet = new HashSet<>();
        // for product
        Set<Cart> cartSet = new HashSet<>();

        HashMap<UUID, Integer> quantity = new HashMap<>();

        // Check if cart exist or not for a particular customer
        Cart existingCart = customer.getCart();
        if (existingCart == null) {
            Cart cart = new Cart();
            // set products in cart and cart in products
            productSet.add(product);
            cart.setCustomer(customer);
            cart.setProductSet(productSet);
            cartSet.add(cart);
            product.setCarts(cartSet);

            quantity.put(addToCartRequestDto.getProduct_id(), addToCartRequestDto.getQuantity());
            cart.setProductsQuantity(quantity);

            // set cart to customer and customer to cart
            customer.setCart(cart);
            cart.setCustomer(customer);

        } else {
            Map<UUID, Integer> currQuantity = existingCart.getProductsQuantity();
            Set<Product> currProductSet = existingCart.getProductSet();
            UUID currProductId = addToCartRequestDto.getProduct_id();
            if (currQuantity.containsKey(currProductId)) {
                currQuantity.put(currProductId, currQuantity.get(currProductId) + addToCartRequestDto.getQuantity());
                existingCart.setProductsQuantity(currQuantity);
            } else {
                currQuantity.put(currProductId, addToCartRequestDto.getQuantity());
                currProductSet.add(product);
            }
        }
        // save customer
        customerRepo.save(customer);
        return new CommonResponse<>("Product added to cart", true, "");
    }

    public CommonResponse<?> addDealsToCart(AddToCartRequestDto addToCartRequestDto) {
        Customer customer = customerRepo.findByEmail(addToCartRequestDto.getCustomer_email()).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        Deals deals = dealsRepo.findById(addToCartRequestDto.getProduct_id()).orElse(null);
        if (deals == null) {
            throw new ProductNotFoundException("Deals not found with id:-" + addToCartRequestDto.getProduct_id());
        }
        Set<Deals> dealsSet = new HashSet<>();
        Set<Cart> cartSet = new HashSet<>();
        // check if the customer have cart or not
        Cart existingCart = customer.getCart();
        if (existingCart == null) {
            HashMap<UUID, Integer> quantity = new HashMap<>();
            Cart cart = new Cart();
            dealsSet.add(deals);
            cartSet.add(cart);
            deals.setCarts(cartSet);
            cart.setDealsSet(dealsSet);
            quantity.put(addToCartRequestDto.getProduct_id(), addToCartRequestDto.getQuantity());
            cart.setProductsQuantity(quantity);
            cart.setCustomer(customer);
            customer.setCart(cart);
        } else {
            Map<UUID, Integer> quantity = existingCart.getProductsQuantity();
            Set<Deals> dealsSet1 = existingCart.getDealsSet();
            if (quantity.containsKey(addToCartRequestDto.getProduct_id())) {
                quantity.put(addToCartRequestDto.getProduct_id(),
                        quantity.get(addToCartRequestDto.getProduct_id()) + 1);
            } else {
                dealsSet1.add(deals);
                quantity.put(addToCartRequestDto.getProduct_id(), addToCartRequestDto.getQuantity());
                customer.setCart(existingCart);
            }
        }
        customerRepo.save(customer);
        return new CommonResponse<>("Product added to cart", true, "");

    }

    public CommonResponse<?> getCartDetails() throws CustomException {
        Customer customer = serviceHelper.getCurrentCustomer();
        List<CartResponse> cartResponseList = new ArrayList<>();
        Cart cart = customer.getCart();
        if (cart == null) {
            return new CommonResponse<>("Cart Not found", true, cartResponseList);
        }
        cart.getProductSet().forEach(product -> {
            CartResponse cartResponse = new CartResponse();
            cartResponse.setId(product.getId());
            cartResponse.setName(product.getName());
            cartResponse.setPrice(product.getPrice());
            cartResponse.setQuantity(cart.getProductsQuantity().get(product.getId()));
            cartResponse.setImage(getProductImage(product.getId()));
            cartResponseList.add(cartResponse);
        });
        cart.getDealsSet().forEach(deals -> {
            CartResponse cartResponse = new CartResponse();
            cartResponse.setId(deals.getId());
            cartResponse.setName(deals.getName());
            cartResponse.setPrice(deals.getPrice());
            cartResponse.setQuantity(cart.getProductsQuantity().get(deals.getId()));
            cartResponse.setImage(getProductImage(deals.getId()));
            cartResponseList.add(cartResponse);
        });
        return new CommonResponse<>("Cart details fetched successfully", true, cartResponseList);
    }

    public CommonResponse<?> getCustomerDetails() throws CustomException {
        Customer customer = serviceHelper.getCurrentCustomer();
        return new CommonResponse<>("Customer fetched successfully", true, customer);
    }

    public CommonResponse<?> updateCustomerPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        String email = forgotPasswordRequestDto.getEmail();
        Customer customer = customerRepo.findByEmail(email).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        customer.setPassword(passwordEncoder.encode(forgotPasswordRequestDto.getPassword()));
        customerRepo.save(customer);

        return new CommonResponse<>("Password updated successfully", true, "");
    }

    public CommonResponse<?> getTop10Products() throws CustomException {
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

        // logic for last access time
        CustomerTop10Products customerTop10Products = new CustomerTop10Products();
        LocalDateTime lastAccessedTime = LocalDateTime.now().minusHours(24);
        // TODO:- valid for 24hrs not last accesstime
        Optional<CustomerTop10Products> loggedInCustomerBetween24hr = top10ProductRepo
                .findByLastAccessTime(customer.getId(), lastAccessedTime);
        Set<Product> products;
        if (loggedInCustomerBetween24hr.isPresent()) {
            products = top10ProductRepo.findProductsByCustomerId(customer.getId()).stream().collect(Collectors.toSet());
        } else {
            products = generateTop10Products();
            CustomerTop10Products isCustomerTop10ProductsExist;
            if (customer.getTop10Products() != null) {
                // Update the existing entity
                isCustomerTop10ProductsExist = customer.getTop10Products();
                isCustomerTop10ProductsExist.setProducts(products);
                isCustomerTop10ProductsExist.setLastAccessTime(LocalDateTime.now());
            } else {
                // Create a new entity
                isCustomerTop10ProductsExist = new CustomerTop10Products();
                isCustomerTop10ProductsExist.setProducts(products);
                isCustomerTop10ProductsExist.setLastAccessTime(LocalDateTime.now());
                isCustomerTop10ProductsExist.setCustomer(customer);
                customer.setTop10Products(isCustomerTop10ProductsExist);
            }

            customerRepo.save(customer);

        }
        return new CommonResponse<>("Product fetched successfully", true, getTop10ProductResponse(products));
    }

    private Set<Product> generateTop10Products() {
        List<Product> products = productRepo.findAll();
        Collections.shuffle(products);
        Set<Product> random10Products = products.stream().limit(10).collect(Collectors.toSet());
        return random10Products;
    }

    public List<Top10ProductResponse> getTop10ProductResponse(Set<Product> random10Products) {
        List<Top10ProductResponse> productResponse = new ArrayList<>();
        random10Products.forEach((product) -> {
            Top10ProductResponse response = new Top10ProductResponse();
            response.setCategory(product.getType());
            response.setImage(getProductImage(product.getId()));
            response.setId(product.getId());
            response.setPrice(product.getPrice());
            response.setName(product.getName());
            productResponse.add(response);
        });
        return productResponse;
    }

    public CommonResponse<?> deleteProductFromCart(UUID productId) throws CustomException {
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
        Cart cartDetails = customer.getCart();
        Set<Product> productSet = cartDetails.getProductSet();
        Map<UUID, Integer> productQuantity = cartDetails.getProductsQuantity();
        productSet.forEach((product) -> {
            if (product.getId().equals(productId)) {
                productQuantity.remove(productId);
            }
        });
        cartDetails.setProductSet(
                productSet.stream().filter(product -> !product.getId().equals(productId)).collect(Collectors.toSet()));
        cartDetails.setProductsQuantity(productQuantity);
        customer.setCart(cartDetails);
        customerRepo.save(customer);
        return new CommonResponse<>("Product removed successfully", true, "");
    }

    @Transactional(rollbackOn = Exception.class)
    public CommonResponse<?> saveOrder(OrderRequestDTO orderRequestDTO) throws CustomException {
        Customer customer = serviceHelper.getCurrentCustomer();
        Map<UUID,Integer> productWithQuantity = orderRequestDTO.getOrderedProducts();
        Set<Product> productSet = new HashSet<>();
        CustomerOrder customerOrder = new CustomerOrder();
        Cart cart = customer.getCart();
        productWithQuantity.forEach((id,val) -> {
            Product product = productRepo.findById(id).orElse(null);
            if(product == null){
                Deals deal = dealsRepo.findById(id).orElse(null);
                if(deal == null && product == null){
                    throw new ProductNotFoundException("Product not found");
                }
                //link order with deal and remove deal from cart after order
                cart.getDealsSet().remove(deal);
                customerOrder.getOrderedDealProducts().add(deal);
                deal.getCustomerOrders().add(customerOrder);
            }
            cart.getProductsQuantity().remove(id);
            productSet.add(product);
            if (product != null) {
                //link order with product and remove product from cart after order
                cart.getProductSet().remove(product);
                product.getCustomerOrders().add(customerOrder);
            }
        });
        try {
            customerOrder.setOrderedProducts(productSet);
            customerOrder.setTotalPrice(orderRequestDTO.getPrice());
            customerOrder.setCustomer(customer);
            customerOrder.setOrderStatus(OrderStatus.ORDER_INITIATED);
            customerOrder.setProductsQuantity(productWithQuantity);
            customerOrder.setCity(orderRequestDTO.getCity());
            customerOrder.setReceiverName(orderRequestDTO.getReceiverName());
            customerOrder.setReceiverEmail(orderRequestDTO.getReceiverEmail());
            customerOrder.setAddress(orderRequestDTO.getAddress());
            customerOrder.setPhoneNumber(orderRequestDTO.getPhoneNumber());
            customerOrder.setAltPhoneNumber(orderRequestDTO.getAltPhoneNumber());
            customerOrder.setDistrict(orderRequestDTO.getDistrict());
            customerOrder.setState(orderRequestDTO.getState());
            if(orderRequestDTO.getPaymentMode() == PaymentMode.CASH_ON_DELIVERY){
                customerOrder.setPaymentMode(PaymentMode.CASH_ON_DELIVERY);
                customerOrder.setPaymentId(UUID.randomUUID());
                customerOrder.setPaymentStatus(PaymentStatus.PAYMENT_PENDING);
            }else{
                customerOrder.setPaymentMode(PaymentMode.ONLINE_PAYMENT);
                customerOrder.setPaymentId(UUID.fromString(orderRequestDTO.getTransactionId()));
                customerOrder.setPaymentStatus(PaymentStatus.PAYMENT_FULL_FILLED);
            }
            List<CustomerOrder> orderList = customer.getOrders();
            orderList.add(customerOrder);

            //save order in customer
            customer.setOrders(orderList);
            customerRepo.save(customer);
        } catch (Exception e) {
            throw new PaymentFailedException(e.getMessage());
        }

        return new CommonResponse<>("Payment success",true,"");
    }

    public CommonResponse<?> processPayment(OrderRequestDTO request) throws RazorpayException, CustomException {

       Customer customer = serviceHelper.getCurrentCustomer();

        double amt = request.getPrice();

        var client = new RazorpayClient(apiKey, secret);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amt * 100);
        jsonObject.put("currency", currency);
        jsonObject.put("receipt", UUID.randomUUID());

        Order order = client.Orders.create(jsonObject);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setOrderId(order.get("id"));
        paymentResponse.setPrice(request.getPrice());
        paymentResponse.setCustomerName(customer.getName());
        paymentResponse.setCustomerEmail(customer.getEmail());
        paymentResponse.setAppName(company);

        return new CommonResponse<>("Payment process initiated",true,paymentResponse);
    }


    public String getProductImage(UUID id) {
        return "http://localhost:8080/pizza-parlour/public/img/" + id;
    }

    public CommonResponse<?> getAllOrders(int page,int size) throws CustomException {
        Customer customer = serviceHelper.getCurrentCustomer();
        if(customer == null){
            throw new CustomException("Customer Not Found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerOrder> customerOrders = orderRepo.findOrderByCustomerId(customer.getId(),pageable);
        Page<OrderResponse> orders = serviceHelper.getCustomerOrders(customerOrders);


        return new CommonResponse<>("Customer order fetched successfully",true,orders);
    }

}
