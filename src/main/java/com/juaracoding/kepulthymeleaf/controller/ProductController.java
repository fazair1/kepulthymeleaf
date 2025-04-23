package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.dto.response.RespProductDTO;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Controller
@RequestMapping("product")
public class ProductController {

    @Autowired
    ProductService productService;

    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    public ProductController() {
        filterColumn.put("nama","Nama");
        filterColumn.put("deskripsi","Deskripsi");
    }

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
                    "product",filterColumn);
//            System.out.println("Body Response : "+response.getBody());
        }catch (Exception e){
            return "redirect:/er";
        }
        return ConstantPage.PRODUCT_MAIN_PAGE;
    }

    @GetMapping("/{sort}/{sortBy}/{page}")
    public String findByParam(
            Model model,
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        page = page > 0 ?(page-1):0;
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = productService.findByParam(jwt,sort,sortBy,page,size,column,value);
            Map<String,Object> mResponse = (Map<String, Object>) response.getBody();
            GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                    "product",filterColumn);
//            System.out.println("Body Response : "+response.getBody());
        }catch (Exception e){
            return "redirect:/er";
        }
        return ConstantPage.PRODUCT_MAIN_PAGE;
    }

    @PostMapping
    public String save(
            @ModelAttribute("data") @Valid ValProductDTO valProductDTO,
            BindingResult bindingResult,
            Model model,
            WebRequest webRequest){

        if(bindingResult.hasErrors()){
            model.addAttribute("data",valProductDTO);
            return ConstantPage.PRODUCT_ADD_PAGE;
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = productService.save(jwt,valProductDTO);
        }catch (Exception e){
            model.addAttribute("data",valProductDTO);
            return ConstantPage.PRODUCT_ADD_PAGE;
        }
        return ConstantPage.SUCCESS_MESSAGE;
    }
    @GetMapping("/a")
    public String openModalsAdd(
            Model model,
            WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }
        model.addAttribute("data",new RespProductDTO());
        return ConstantPage.PRODUCT_ADD_PAGE;
    }



}
