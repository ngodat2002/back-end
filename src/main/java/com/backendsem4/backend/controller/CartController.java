package com.backendsem4.backend.controller;

import com.backendsem4.backend.commom.CommomDataService;
import com.backendsem4.backend.entities.*;
import com.backendsem4.backend.repository.OrderDetailRepository;
import com.backendsem4.backend.repository.OrderRepository;
import com.backendsem4.backend.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

/**
 * @author DongTHD
 *
 */
@Controller
public class CartController extends CommomController {

	@Autowired
	HttpSession session;

	@Autowired
	CommomDataService commomDataService;

	@Autowired
	ShoppingCartService shoppingCartService;


	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	public Order orderFinal = new Order();

	private Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping(value = "/shoppingCart_checkout")
	public String shoppingCart(Model model) {

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
		}

		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		return "web/shoppingCart_checkout";
	}

	// add cartItem
	@GetMapping(value = "/addToCart")
	public String add(@RequestParam("productId") Long productId, HttpServletRequest request, Model model) {

		Product product = productRepository.findById(productId).orElse(null);

		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setQuantity(1);
			item.setProduct(product);
			item.setId(productId);
			shoppingCartService.add(item);
		}
		session.setAttribute("cartItems", cartItems);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		return "redirect:/products";
	}

	// delete cartItem
	@SuppressWarnings("unlikely-arg-type")
	@GetMapping(value = "/remove/{id}")
	public String remove(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
		Product product = productRepository.findById(id).orElse(null);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		session = request.getSession();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setProduct(product);
			item.setId(id);
			cartItems.remove(session);
			shoppingCartService.remove(item);
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return "redirect:/checkout";
	}

	// show check out
	@GetMapping(value = "/checkout")
	public String checkOut(Model model, User user) {

		Order order = new Order();
		model.addAttribute("order", order);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		model.addAttribute("NoOfItems", shoppingCartService.getCount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
		}

		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		commomDataService.commonData(model, user);

		return "web/shoppingCart_checkout";
	}

	// submit checkout
	@PostMapping(value = "/checkout")
	@Transactional
	public String checkedOut(Model model, Order order, HttpServletRequest request, User user)
			throws MessagingException {
		String checkOut = request.getParameter("checkOut");
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
		}
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
			//save detail
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setQuantitydetail(cartItem.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setProduct(cartItem.getProduct());
			double unitPrice = cartItem.getProduct().getPrice();
			orderDetail.setPricedetail(unitPrice);
			orderDetailRepository.save(orderDetail);
		}

		// sendMail
		commomDataService.sendSimpleEmail(user.getEmail(), "Greeny-Shop Xác Nhận Đơn hàng", "aaaa", cartItems,
				totalPrice, order);

		shoppingCartService.clear();
		session.removeAttribute("cartItems");
		model.addAttribute("orderId", order.getOrderId());

		return "redirect:/checkout_success";
	}

	// done checkout ship cod
	@GetMapping(value = "/checkout_success")
	public String checkoutSuccess(Model model, User user) {
		commomDataService.commonData(model, user);

		return "web/checkout_success";

	}
	// done checkout paypal
	@GetMapping(value = "/checkout_paypal_success")
	public String paypalSuccess(Model model, User user) {
		commomDataService.commonData(model, user);

		return "web/checkout_paypal_success";

	}




	//Long Comment....

	// xoa 1 cartItem
	@GetMapping(value = "/xoaToCart")
	public String update(@RequestParam("productId") Long productId, HttpServletRequest request, Model model) {

		Product product = productRepository.findById(productId).orElse(null);

		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setQuantity(-1);
			item.setProduct(product);
			item.setId(productId);
			shoppingCartService.add(item);
			for (CartItem cartItem : cartItems) {
				if(cartItem.getId() == item.getId() && cartItem.getQuantity()==0){
					return  "redirect:/remove/"+item.getId();
				}
			}
		}
		session.setAttribute("cartItems", cartItems);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		return "redirect:/checkout";
	}
	// xoa 1 cartItem
	@GetMapping(value = "/deleteaproduct")
	public String delete(@RequestParam("productId") Long productId, HttpServletRequest request, Model model) {

		Product product = productRepository.findById(productId).orElse(null);

		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();

		if (product != null) {

			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setQuantity(-1);

			item.setProduct(product);
			item.setId(productId);

			shoppingCartService.add(item);
			for (CartItem cartItem : cartItems) {
				if(cartItem.getId() == item.getId() && cartItem.getQuantity()==0){
//					String a ="redirect:/removeCart/";
//					String id = String.valueOf(item.getId());
//					a.
					return  "redirect:/removeCart/"+item.getId();
				}
			}
		}
		session.setAttribute("cartItems", cartItems);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());


		model.addAttribute("active" , 1);
		return "redirect:/products";
	}
	// tang 1 cartItem
	@GetMapping(value = "/tangToCart")
	public String tang(@RequestParam("productId") Long productId, HttpServletRequest request, Model model) {

		Product product = productRepository.findById(productId).orElse(null);

		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setQuantity(1);
			item.setProduct(product);
			item.setId(productId);
			shoppingCartService.add(item);
		}
		session.setAttribute("cartItems", cartItems);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		return "redirect:/checkout";
	}
	// delete cartItem
	@SuppressWarnings("unlikely-arg-type")
	@GetMapping(value = "/removeCart/{id}")
	public String removeCart(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
		Product product = productRepository.findById(id).orElse(null);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		session = request.getSession();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setProduct(product);
			item.setId(id);
			cartItems.remove(session);
			shoppingCartService.remove(item);
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return "redirect:/products";
	}
}
