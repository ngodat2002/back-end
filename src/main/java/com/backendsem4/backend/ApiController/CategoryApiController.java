package com.backendsem4.backend.ApiController;

import com.backendsem4.backend.entities.Category;
import com.backendsem4.backend.repository.CategoryRepository;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping(path = "/app/api/categories")
public class CategoryApiController {
    @Value("${upload.path}")
    private String pathUploadImage;
    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping("")
    List<Category> getAllCategory(){
        return categoryRepository.findAll();
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
}

