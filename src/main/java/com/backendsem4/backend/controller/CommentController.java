package com.backendsem4.backend.controller;

import com.backendsem4.backend.commom.CommomDataService;
import com.backendsem4.backend.entities.Comment;
import com.backendsem4.backend.entities.Product;
import com.backendsem4.backend.entities.User;
import com.backendsem4.backend.repository.CommentRepository;
import com.backendsem4.backend.repository.ProductRepository;
import com.backendsem4.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;


@Controller
public class CommentController {
    @Autowired
    HttpSession session;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommomDataService commomDataService;

    @PostMapping("/addComment")
    public String AddComment(Model model, Comment comment,
                             @RequestParam("userId") Long userId,
                             @RequestParam("content") String content,
                             @RequestParam("id") Long id,
                             @RequestParam("star") int star) {
        ArrayList<String> list = new ArrayList<>();

        list.add("rau khong ngon");
        list.add("rau rat gio");
        list.add("rau bi hu nhieu qua");
        list.add("khong sach se");
        list.add("khong dang gia tien");
        list.add("khong dam bao ve sinh");
        list.add("khong chat luong nhu mong doi");
        list.add("ban hang khong chat luong");

        if(list.contains(content)){
            Product product = productRepository.findById(id).orElse(null);
            session.setAttribute("error", 1);
            return "redirect:/productDetail?id=" + product.getProductId();
        }
        session.removeAttribute("error");
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(id).orElse(null);
        comment.setProduct(product);
        comment.setUser(user);
        comment.setContent(content);
        comment.setStar(star);
        comment.setStatus(1);
        comment.setRateDate(new Date());
        commentRepository.save(comment);
        commomDataService.commonData(model, user);
        return "redirect:/productDetail?id=" + product.getProductId();
    }

}
