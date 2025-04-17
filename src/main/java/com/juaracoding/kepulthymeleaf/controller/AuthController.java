package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.config.OtherConfig;
import com.juaracoding.kepulthymeleaf.dto.validation.ValLoginDTO;
import com.juaracoding.kepulthymeleaf.httpservice.AuthService;
import com.juaracoding.kepulthymeleaf.security.BcryptImpl;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.ConstantValue;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import jakarta.validation.Valid;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public String login(
            @ModelAttribute("usr") @Valid ValLoginDTO valLoginDTO,
            BindingResult result,
            Model model,
            WebRequest webRequest) {

//        try{
//            Long sesId = Long.parseLong(Crypto.performDecrypt(valLoginDTO.getSesId()));
//            Long selisih = (System.currentTimeMillis()-sesId)/1000;
//            if(selisih<180){
//                throw new Exception("Anda Terkepung !!");
//            }
//        }catch (Exception e){
//
//        }

        String decodePassword = new String(Base64.decode(valLoginDTO.getPassword()));
//        GlobalFunction.matchingPattern(valLoginDTO.getUsername(),"^[[a-z0-9\\\\.]]{8,16}$","username","Isi Username dengan benar!!",result);
//        GlobalFunction.matchingPattern(valLoginDTO.getCaptcha(),"^[\\w]{5}$","captcha","Isi Captcha dengan benar!!",result);

        GlobalFunction.matchingPattern(decodePassword,"^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                "password","Isi Password dengan benar!!","usr",result);

        String strAnswer = valLoginDTO.getHiddenCaptcha();
        Boolean isValid = false;
        valLoginDTO.setPassword(decodePassword);

        if(OtherConfig.getEnableAutomationTesting().equals("y")){
            // kalau mode automation testing
            isValid = valLoginDTO.getCaptcha().equals(strAnswer);
        }else {
            // kalau mode production
            isValid = BcryptImpl.verifyHash(valLoginDTO.getCaptcha(),strAnswer);
        }
        if(!isValid){
            model.addAttribute("captchaMessage","Invalid Captcha");
            GlobalFunction.getCaptchaLogin(valLoginDTO);
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);
            return ConstantPage.LOGIN_PAGE;
        }

        if (result.hasErrors()) {
            GlobalFunction.getCaptchaLogin(valLoginDTO);
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);
            return ConstantPage.LOGIN_PAGE;
        }

        /** REQUEST LOGIN */
        ResponseEntity<Object> response = null;
        String tokenJwt = "";
//        String menuNavBar = "";

        try{
            response = authService.login(valLoginDTO);
            System.out.println(response);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> data = (Map<String, Object>) map.get("data");
//            List<Map<String,Object>> ltMenu = (List<Map<String, Object>>) data.get("menu");
//            menuNavBar = new GenerateStringMenu().stringMenu(ltMenu);
            tokenJwt = (String) data.get("token");
//            System.out.println("Body Response : "+ltMenu);
//            System.out.println("Token JWT : "+tokenJwt);

        }catch (Exception e){
            GlobalFunction.getCaptchaLogin(valLoginDTO);
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);
            result.rejectValue("username", "error.user", "Username / Password Salah");
            result.rejectValue("password", "error.user", "Username / Password Salah");
            return ConstantPage.LOGIN_PAGE;

        }
//        webRequest.setAttribute("MENU_NAVBAR",menuNavBar,1);
        webRequest.setAttribute("JWT",tokenJwt,1);
        webRequest.setAttribute("USR_NAME",valLoginDTO.getUsername(),1);
        webRequest.setAttribute("PASSWORD",valLoginDTO.getPassword(),1);

        model.addAttribute("USR_NAME",valLoginDTO);
//        model.addAttribute("MENU_NAVBAR",menuNavBar);

        return ConstantPage.HOME_PAGE;
    }

}
