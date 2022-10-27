package com.backendsem4.backend.ApiController;

import com.backendsem4.backend.commom.CommomDataService;
import com.backendsem4.backend.entities.*;
import com.backendsem4.backend.repository.OrderDetailRepository;
import com.backendsem4.backend.repository.OrderRepository;
import com.backendsem4.backend.repository.ProductRepository;
import com.backendsem4.backend.repository.UserRepository;
import com.backendsem4.backend.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Date;


@RestController
@RequestMapping(path = "/app/api/cart")
public class CartApiController {
    @Autowired
    HttpSession session;
    @Autowired
    CommomDataService commomDataService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ProductRepository productRepository;
    public Order orderFinal = new Order();


    private Logger log = LoggerFactory.getLogger(getClass());
    @GetMapping("/{productId}/{quantity}")
    ResponseEntity<ResponseObject> addToCart(@PathVariable Long productId, @PathVariable int quantity){
        Product foundProduct = productRepository.findById(productId).orElse(null);


        if (foundProduct != null) {
            Collection<CartItem> cartItems = shoppingCartService.getCartItems();

            CartItem item = new CartItem();
            BeanUtils.copyProperties(foundProduct, item);
            item.setName(foundProduct.getProductName());
            item.setUnitPrice(foundProduct.getPrice());

            item.setQuantity(quantity);
            item.setProduct(foundProduct);
            item.setId(productId);
            shoppingCartService.add(item);
            return
                    ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("Ok", "Add to Cart Success", foundProduct));
        }else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Ok", "Cannot find Product With id =" + productId, "foundProduct"));
        }
    }

    @DeleteMapping("/remove/{productId}")
    ResponseEntity<ResponseObject> deleteCartItem(@PathVariable Long productId ) {
        Product foundProduct = productRepository.findById(productId).orElse(null);
        if (foundProduct != null) {
            Collection<CartItem> cartItems = shoppingCartService.getCartItems();
            CartItem item = new CartItem();
            BeanUtils.copyProperties(foundProduct, item);
            item.setProduct(foundProduct);
            item.setId(productId);
            shoppingCartService.remove(item);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Delete  Cart Success", foundProduct));
        } else {
            return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Ok", "Cannot find Product With id =" + productId, "foundProduct"));
        }
    }
    @GetMapping("/checkout")
    ResponseEntity<ResponseObject> checkout() {
        Collection<CartItem> cartItems = shoppingCartService.getCartItems();

        if (cartItems != null) {

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Get all Cart Success", cartItems));
        } else {
            return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Ok", "Cannot find Order ", "foundOrder"));
        }
    }
    @PostMapping("/suc/checkout/{email}")
    ResponseEntity<ResponseObject> checkedout(@RequestBody Order order, @PathVariable String email, HttpServletRequest request) throws MessagingException {

        Collection<CartItem> cartItems = shoppingCartService.getCartItems();

        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
            totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
        }
        User user = userRepository.findByEmail(email);
        BeanUtils.copyProperties(order, orderFinal);
        session = request.getSession();
        Date date = new Date();
        order.setOrderDate(date);
        order.setStatus(0);
        order.getOrderId();
        order.setAmount(totalPrice);
        order.setUser(user);

        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantitydetail(cartItem.getQuantity());
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            double unitPrice = cartItem.getProduct().getPrice();
            orderDetail.setPricedetail(unitPrice);
            orderDetailRepository.save(orderDetail);
        }

        // sendMail
        commomDataService.sendSimpleEmail(email, "VegetableOrganic-Shop Xác Nhận Đơn hàng", "Love", cartItems,
                totalPrice, order);
        shoppingCartService.clear();
        if (cartItems != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Order success","Love"));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Ok", "F success", "Love"));

    }
    @GetMapping("/clear")
    ResponseEntity<ResponseObject> clear() {
        shoppingCartService.clear();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Ok", "Clear success", "Love"));

    }

}