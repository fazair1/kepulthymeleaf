package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.dto.validation.ValProductDTO;
import com.juaracoding.kepulthymeleaf.httpservice.ProductService;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Scanner;

@Controller
@RequestMapping("product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public String findAll(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = productService.findAll(jwt);
            Map<String,Object> mResponse = (Map<String, Object>) response.getBody();
            GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                    "group-menu",filterColumn);
//            System.out.println("Body Response : "+response.getBody());
        }catch (Exception e){
            return "redirect:/er";
        }
        return ConstantPage.PRODUCT_MAIN_PAGE;
    }
//
//    @PostMapping("")
//    public String save(@ModelAttribute("data") @Valid ValProductDTO valProductDTO,
//                       BindingResult bindingResult,
//                       Model model,WebRequest webRequest ){
//
//        return "product/list";
//    }

//    @GetMapping("/product")
//    public String getProducts(@RequestParam(value = "search", required = false) String search,
//                              Model model,
//                              @RequestHeader("Authorization") String token) {
//        ResponseEntity<Object> response = (search == null || search.isEmpty())
//                ? menuService.findAll(token)
//                : menuService.findByParam(token, "asc", "id", 0, 10, "name", search);
//
//        List<Map<String, Object>> productList = (List<Map<String, Object>>) response.getBody();
//        model.addAttribute("productList", productList);
//        model.addAttribute("search", search);
//        return "product/list";
//    }
}
