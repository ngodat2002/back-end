package com.backendsem4.backend.controller;

import com.backendsem4.backend.entities.Category;
import com.backendsem4.backend.entities.User;
import com.backendsem4.backend.repository.CategoryRepository;
import com.backendsem4.backend.repository.ProductRepository;
import com.backendsem4.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.security.Principal;
import java.util.List;

@Controller
public class CommomController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}

	@ModelAttribute("categoryList")
	public List<Category> showCategory(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);

		return categoryList;
	}

}
