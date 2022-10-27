package com.backendsem4.backend.controller.admin;

import com.backendsem4.backend.dto.OrderExcelExporter;
import com.backendsem4.backend.dto.OrderPdfExport;
import com.backendsem4.backend.entities.Order;
import com.backendsem4.backend.entities.OrderDetail;
import com.backendsem4.backend.entities.Product;
import com.backendsem4.backend.entities.User;
import com.backendsem4.backend.repository.OrderDetailRepository;
import com.backendsem4.backend.repository.OrderRepository;
import com.backendsem4.backend.repository.ProductRepository;
import com.backendsem4.backend.repository.UserRepository;
import com.backendsem4.backend.service.OrderDetailService;
import com.backendsem4.backend.service.ProductDetailService;
import com.backendsem4.backend.service.SendMailService;
import com.backendsem4.backend.service.UserService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class OrderController {

	@Autowired
	UserService userService;
	@Autowired
	ProductDetailService productDetailService;
	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	SendMailService sendMailService;

	@Autowired
	UserRepository userRepository;

	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}

	// list order
	@GetMapping(value = "/orders")
	public String orders(Model model, Principal principal) {

		List<Order> orderDetails = orderRepository.findAll();
		model.addAttribute("orderDetails", orderDetails);

		return "admin/orders";
	}

	@GetMapping("/order/detail/{order_id}")
	public ModelAndView detail(ModelMap model, @PathVariable("order_id") Long id) {

		List<OrderDetail> listO = orderDetailRepository.findByOrderId(id);

		model.addAttribute("amount", orderRepository.findById(id).get().getAmount());
		model.addAttribute("orderDetail", listO);
		model.addAttribute("orderId", id);
		// set active front-end
		model.addAttribute("menuO", "menu");
		return new ModelAndView("admin/editOrder", model);
	}

	@RequestMapping("/order/cancel/{order_id}")
	public ModelAndView cancel(ModelMap model, @PathVariable("order_id") Long id) {
		Optional<Order> o = orderRepository.findById(id);
		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/orders", model);
		}
		Order oReal = o.get();
		oReal.setStatus((short) 3);
		orderRepository.save(oReal);

		return new ModelAndView("forward:/admin/orders", model);
	}

	@RequestMapping("/order/confirm/{order_id}")
	public ModelAndView confirm(ModelMap model, @PathVariable("order_id") Long id) {
		Optional<Order> o = orderRepository.findById(id);
		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/orders", model);
		}
		Order oReal = o.get();
		oReal.setStatus((short) 1);
		orderRepository.save(oReal);

		return new ModelAndView("forward:/admin/orders", model);
	}

	@RequestMapping("/order/delivered/{order_id}")
	public ModelAndView delivered(ModelMap model, @PathVariable("order_id") Long id) {
		Optional<Order> o = orderRepository.findById(id);
		if (o.isEmpty()) {
			return new ModelAndView("forward:/admin/orders", model);
		}
		Order oReal = o.get();
		oReal.setStatus((short) 2);
		orderRepository.save(oReal);

		Product p = null;
		List<OrderDetail> listDe = orderDetailRepository.findByOrderId(id);
		for (OrderDetail od : listDe) {
			p = od.getProduct();
			p.setQuantity(p.getQuantity() - od.getQuantitydetail());
			productRepository.save(p);
		}

		return new ModelAndView("forward:/admin/orders", model);
	}

	// to excel
	@GetMapping(value = "/orderExcel")
	public void exportToExcel(HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename=orders.xlsx";

		response.setHeader(headerKey, headerValue);

		List<Order> lisOrders = orderDetailService.listAll();

		OrderExcelExporter excelExporter = new OrderExcelExporter(lisOrders);
		excelExporter.export(response);

	}
	// to excel


	//to pdf
	@GetMapping("/orderPdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=orders_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<Order> listOrder = orderDetailService.listAll();

		OrderPdfExport exporter = new OrderPdfExport(listOrder);
		exporter.export(response);

	}
}
