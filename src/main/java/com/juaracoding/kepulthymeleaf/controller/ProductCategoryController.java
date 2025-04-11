package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.httpservice.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("product-category")
public class ProductCategoryController {

    @Autowired
    ProductCategoryService productCategoryService;

    @GetMapping
    public String findAll(Model model,
                          WebRequest request) {

        ResponseEntity<Object> response = null;
//        String jwt = GlobalFunction.tokenCheck(model, request);

//        if (jwt.equals(ConstantPage.LOGIN_PAGE)) {
//            return jwt;
//        }
        Map<String, Object> mResponse = null;
        try {
//            response = productCategoryService.findAll(jwt);
            response = productCategoryService.findAll();

            mResponse = (Map<String, Object>) response.getBody();
//            GlobalFunction.setDataMainPage(model,request,mResponse,
//                    "group-menu",filterColumn);

        } catch (Exception e) {
//            return "redirect:/er";
        }
//        return ConstantPage.GROUP_MENU_MAIN_PAGE;
        Map<String,Object> data = (Map<String, Object>) mResponse.get("data");//element yang di dalam paging
        List<Map<String,Object>> listContent = (List<Map<String, Object>>) data.get("content");
        model.addAttribute("listContent",listContent);
        System.out.println(listContent);
        return "/productcategory/main";
    }

}
