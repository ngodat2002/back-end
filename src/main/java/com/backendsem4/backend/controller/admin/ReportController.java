package com.backendsem4.backend.controller.admin;

import com.backendsem4.backend.entities.OrderDetail;
import com.backendsem4.backend.entities.User;
import com.backendsem4.backend.repository.OrderDetailRepository;
import com.backendsem4.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ReportController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	// Statistics by product sold
	@GetMapping(value = "/admin/reports")
	public String report(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<Object[]> listReportCommon = orderDetailRepository.repo();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/statistical";
	}

	// Statistics by category sold
	@RequestMapping(value = "/admin/reportCategory")
	public String reportcategory(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<Object[]> listReportCommon = orderDetailRepository.repoWhereCategory();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/stacategory";
	}

	// Statistics of products sold by year
	@RequestMapping(value = "/admin/reportYear")
	public String reportyear(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<Object[]> listReportCommon = orderDetailRepository.repoWhereYear();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/stayear";
	}

	// Statistics of products sold by month
	@RequestMapping(value = "/admin/reportMonth")
	public String reportmonth(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<Object[]> listReportCommon = orderDetailRepository.repoWhereMonth();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/stamonth";
	}

	// Statistics of products sold by quarter
	@RequestMapping(value = "/admin/reportQuarter")
	public String reportquarter(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<Object[]> listReportCommon = orderDetailRepository.repoWhereQUARTER();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/staquar";
	}

	// Statistics by user
	@RequestMapping(value = "/admin/reportOrderCustomer")
	public String reportordercustomer(Model model, Principal principal) throws SQLException {
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<Object[]> listReportCommon = orderDetailRepository.reportCustommer();
		model.addAttribute("listReportCommon", listReportCommon);

		return "admin/stauser";
	}
}
