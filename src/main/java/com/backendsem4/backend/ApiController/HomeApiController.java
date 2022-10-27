package com.backendsem4.backend.ApiController;

import com.backendsem4.backend.dto.BaseModel;
import com.backendsem4.backend.dto.HomeDto;
import com.backendsem4.backend.entities.Product;
import com.backendsem4.backend.entities.ResponseObject;
import com.backendsem4.backend.repository.ProductRepository;
import com.backendsem4.backend.service.ProductService;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/app/api/products")
public class HomeApiController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Value("${upload.path}")
    private String pathUploadImage;
    @GetMapping("")
    List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    @GetMapping(value = "/loadImage/{imageName}")
    @ResponseBody
    public byte[] index(@PathVariable String imageName, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/jpeg");
        File file = new File(pathUploadImage + File.separatorChar + imageName);
        InputStream inputStream = null;
        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (inputStream != null) {
                return IOUtils.toByteArray(inputStream);
            }
        }
        return IOUtils.toByteArray(inputStream);
    }
    //getDetailProduct
    @GetMapping("/{productId}")
    ResponseEntity<ResponseObject> ProductDetail(@PathVariable Long productId){
        Optional<Product> foundProduct = productRepository.findById(productId);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Query product Success", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Ok", "Cannot find Product With id =" + productId, "foundProduct")
                );
    }

    //Top10 product Same Type
    @GetMapping("/listProductTop/{categoryId}")
    ResponseEntity<ResponseObject> listProductByCategory10(@PathVariable Long categoryId){
        List<Product> foundProduct = productRepository.listProductByCategory10(categoryId);
        return foundProduct.size() > 0 ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Query product Success", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Ok", "Cannot find Product With id =" + categoryId, "foundProduct")
                );
    }
    //Top10 product Same Type
    @GetMapping("/listproduct10")
    ResponseEntity<ResponseObject> listproduct10(){
        List<Product> foundProduct = productRepository.listProductNew20();
        return foundProduct.size() > 0 ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Query product Success", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Ok", "Query product Fails", "foundProduct")
                );
    }

    @GetMapping("home")
    public ResponseEntity getHome(){
        BaseModel<HomeDto> res = new BaseModel<>();
        HomeDto homeDto = new HomeDto();
        homeDto.setListBestSale(productService.listBestSale());
        homeDto.setListNew(productService.listNew());
        homeDto.setListNoiBat(productService.listNoiBat());
        res.setData(homeDto);
        return ResponseEntity.ok(res);
    }

}

