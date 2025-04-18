package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.config.OtherConfig;
import com.juaracoding.kepulthymeleaf.dto.validation.ValLoginDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValRegisDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValVerifyOTPRegisDTO;
import com.juaracoding.kepulthymeleaf.handler.FeignCustomException;
import com.juaracoding.kepulthymeleaf.httpservice.AuthService;
import com.juaracoding.kepulthymeleaf.security.BcryptImpl;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.ConstantValue;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

        String decodePassword = new String(Base64.decode(valLoginDTO.getPassword()));

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

        }catch (FeignCustomException e){
            Map<String, Object> errorBody = e.getErrorBody();
            model.addAttribute("errorMessage", errorBody.get("message"));
            GlobalFunction.getCaptchaLogin(valLoginDTO);
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);
            result.rejectValue("username", "error.user", (String) errorBody.get("message"));
            result.rejectValue("password", "error.user", (String) errorBody.get("message"));
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

    @GetMapping("/regis")
    public String regisPage(Model model) {

        ValRegisDTO valRegisDTO = new ValRegisDTO();
        model.addAttribute("usr", valRegisDTO);
        model.addAttribute("logo", ConstantValue.LOGIN_LOGO);

        return ConstantPage.REGISTER_PAGE;
    }

    @PostMapping("/regis")
    public String regis(
            @ModelAttribute("usr") @Valid ValRegisDTO valRegisDTO,
            BindingResult result,
            Model model,
            WebRequest webRequest) {

        String decodePassword = new String(Base64.decode(valRegisDTO.getPassword()));

        GlobalFunction.matchingPattern(decodePassword,"^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                "password","Isi Password dengan benar!!","usr",result);

        valRegisDTO.setPassword(decodePassword);

        if (result.hasErrors()) {
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);
            return ConstantPage.REGISTER_PAGE;
        }

        /** REQUEST Regis */
        ResponseEntity<Object> response = null;
        ResponseEntity<Object> responseVerify = null;

        try{

            response = authService.regis(valRegisDTO);
//            System.out.println(response);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> data = (Map<String, Object>) map.get("data");

            String otp = (String) data.get("otp");
            String email = (String) data.get("email");

            ValVerifyOTPRegisDTO valVerifyOTPRegisDTO = new ValVerifyOTPRegisDTO();
            valVerifyOTPRegisDTO.setOtp(otp);
            valVerifyOTPRegisDTO.setEmail(email);
            response = authService.verifyRegis(valVerifyOTPRegisDTO);

//            System.out.println("Body Response : "+ltMenu);
//            System.out.println("Token JWT : "+tokenJwt);

        }catch (FeignCustomException e){
            Map<String, Object> errorBody = e.getErrorBody();
            model.addAttribute("errorMessage", errorBody.get("message"));
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);
//            result.rejectValue("username", "error.user", "Format Huruf kecil ,numeric dan titik saja min 4 max 20 karakter, contoh : fauzan.123");
//            result.rejectValue("password", "error.user", "Format minimal 1 angka, 1 huruf, min 8 karakter, contoh : aB412345");
//            result.rejectValue("confirmPassword", "error.user", "Format minimal 1 angka, 1 huruf, min 8 karakter, contoh : aB412345");
//            result.rejectValue("email", "error.user", "Format tidak valid contoh : user_name123@sub.domain.com");
//            result.rejectValue("nama", "error.user", "Hanya Alfabet dan spasi Minimal 4 Maksimal 50");

            return ConstantPage.REGISTER_PAGE;

        }
//        webRequest.setAttribute("MENU_NAVBAR",menuNavBar,1);
//        model.addAttribute("MENU_NAVBAR",menuNavBar);

        return "redirect:"+ConstantPage.DEFAULT_PAGE;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:"+ConstantPage.DEFAULT_PAGE;
    }

}
