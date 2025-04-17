package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.dto.validation.ValLoginDTO;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.ConstantValue;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
