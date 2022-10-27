package com.backendsem4.backend.controller.admin;

import com.backendsem4.backend.dto.ProductExcelExporter;
import com.backendsem4.backend.dto.ProductPdfExporter;
import com.backendsem4.backend.entities.Category;
import com.backendsem4.backend.entities.Product;
import com.backendsem4.backend.entities.User;
import com.backendsem4.backend.repository.CategoryRepository;
import com.backendsem4.backend.repository.OrderDetailRepository;
import com.backendsem4.backend.repository.ProductRepository;
import com.backendsem4.backend.repository.UserRepository;
import com.backendsem4.backend.service.ProductDetailService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {

	@Value("${upload.path}")
	private String pathUploadImage;
	@Autowired
	ProductDetailService productDetailService;

	@Autowired
	ProductRepository productRepository;
	@Autowired
	OrderDetailRepository orderDetailRepository;
	@Autowired
	CategoryRepository categoryRepository;

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

	public ProductController(CategoryRepository categoryRepository,
                             ProductRepository productRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	// show list product - table list
	@ModelAttribute("products")
	public List<Product> showProduct(Model model) {
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);


		return products;
	}

	@GetMapping(value = "/products")
	public String products(Model model, Principal principal) {
		Product product = new Product();
		model.addAttribute("product", product);
		return "admin/products";
	}

	// add product
	@PostMapping(value = "/addProduct")
	public String addProduct(@ModelAttribute("product") Product product, ModelMap model,
							 @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {

		if(productRepository.ProductName(product.getProductName()).isEmpty()){
			try {
				File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
			} catch (IOException e) {
			}

			product.setProductImage(file.getOriginalFilename());

			product.setEnteredDate(new Date());
			Product p = productRepository.save(product);
			p.setQrCode(p.getProductId() + ".png");
			productRepository.save(p);
			if (null != p) {
				String filePath =pathUploadImage + "/"+ p.getProductId()+".png";
				model.addAttribute("message", "Add success");
				model.addAttribute("product", product);
				return "redirect:/admin/products";

			} else {
				model.addAttribute("error", "Add failure");
				model.addAttribute("product", product);
				return "redirect:/admin/products";
			}
		}else {
			model.addAttribute("error", "Product already exists ");
			model.addAttribute("product", product);
			return "admin/products";
		}

	}

	// show select option ở add product
	@ModelAttribute("categoryList")
	public List<Category> showCategory(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);

		return categoryList;
	}

	// get Edit brand
	@GetMapping(value = "/editProduct/{id}")
	public String editCategory(@PathVariable("id") Long id, ModelMap model) {
		Product product = productRepository.findById(id).orElse(null);

		model.addAttribute("product", product);

		return "admin/editProduct";
	}

	// edit product
	@PostMapping(value = "/editProduct")
	public String editProduct(@ModelAttribute("product") Product p, ModelMap model,
							  @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) throws IOException {
		Product product = productRepository.findById(p.getProductId()).orElse(null);
		if (file.isEmpty()) {
			p.setProductImage(product.getProductImage());
		} else {
			try {
				File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
			} catch (IOException e) {
			}
			p.setProductImage(file.getOriginalFilename());
		}
		p.setQrCode(product.getProductId() + ".png");
		p.setStatus(0);
		productRepository.save(p);
		if (null != p) {
			try{
				Path myPathqr = Paths.get(pathUploadImage + "/" + productRepository.findById(product.getProductId()).get().getQrCode());
				Files.deleteIfExists(myPathqr);
			}catch (Exception e){

			}
			String filePath =pathUploadImage + "/"+ p.getProductId()+".png";
			model.addAttribute("message", "Update success");
			model.addAttribute("product", p);
		} else {
			model.addAttribute("message", "Update failure");
			model.addAttribute("product", p);
		}
		return "redirect:/admin/products";
	}

	// delete category
	@GetMapping("/deleteProduct/{id}")
	public String delProduct(@PathVariable("id") Long id, Model model) throws IOException {
		try{
			Path myPath = Paths.get(pathUploadImage + "/" + productRepository.findById(id).get().getProductImage());
			Files.deleteIfExists(myPath);

		}catch (IOException e) {
		}
		productRepository.deleteById(id);
		model.addAttribute("message", "Delete successful!");

		return "redirect:/admin/products";
	}
	@GetMapping(value = "/productExcel")
	public void exportToExcel(HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename=products.xlsx";

		response.setHeader(headerKey, headerValue);

		List<Product> lisProducts = productDetailService.listAll();

		ProductExcelExporter excelExporter = new ProductExcelExporter(lisProducts);
		excelExporter.export(response);

	}
	@GetMapping("/productPdf")
	public void productToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=products_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<Product> listProduct = productDetailService.listAll();

		ProductPdfExporter exporter = new ProductPdfExporter(listProduct);
		exporter.export(response);
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
}
