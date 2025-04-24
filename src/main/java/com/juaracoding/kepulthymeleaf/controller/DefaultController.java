package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.dto.validation.ValLoginDTO;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.ConstantValue;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String defaultPage(Model model) {

        ValLoginDTO valLoginDTO = new ValLoginDTO();
        GlobalFunction.getCaptchaLogin(valLoginDTO);
//        valLoginDTO.setSesId(Crypto.performEncrypt(String.valueOf(System.currentTimeMillis())));
        model.addAttribute("usr", valLoginDTO);
        model.addAttribute("logo", ConstantValue.LOGIN_LOGO);

        return ConstantPage.LOGIN_PAGE;
    }

    @GetMapping("/home")
    public String home(Model model, WebRequest webRequest) {
        GlobalFunction.setGlobalAttribute(model, webRequest);
        return "auth/home";
    }

//    @GetMapping("/product")
//    public String product(Model model, WebRequest webRequest) {
////        model.addAttribute("USR_NAME",webRequest.getAttribute("USR_NAME",1));
////        model.addAttribute("MENU_NAVBAR",webRequest.getAttribute("MENU_NAVBAR",1));
//        return "product/main";
//    }
    @GetMapping("/request")
    public String request(Model model, WebRequest webRequest) {
//        model.addAttribute("USR_NAME",webRequest.getAttribute("USR_NAME",1));
//        model.addAttribute("MENU_NAVBAR",webRequest.getAttribute("MENU_NAVBAR",1));
        return "request/main";
    }

}
