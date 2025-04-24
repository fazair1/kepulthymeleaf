package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.httpservice.ProductService;
import com.juaracoding.kepulthymeleaf.httpservice.UserService;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    public UserController() {
        filterColumn.put("username","Username");
        filterColumn.put("email","Email");
        filterColumn.put("nama","Nama");
    }

    @GetMapping
    public String findAll(Model model,
                          WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = userService.findAll(jwt);
            Map<String,Object> mResponse = (Map<String, Object>) response.getBody();
            GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                    "user",filterColumn);
//            System.out.println("Body Response : "+response.getBody());
        }catch (Exception e){
            return "redirect:/user";
        }
        return ConstantPage.USER_MAIN_PAGE;
    }

}
