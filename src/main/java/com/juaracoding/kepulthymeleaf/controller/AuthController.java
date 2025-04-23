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
        String menuNavBar = "";
        String akses = "";

        try{
            response = authService.login(valLoginDTO);
            System.out.println(response);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> data = (Map<String, Object>) map.get("data");
//            List<Map<String,Object>> ltMenu = (List<Map<String, Object>>) data.get("menu");
//            menuNavBar = new GenerateStringMenu().stringMenu(ltMenu);
            tokenJwt = (String) data.get("token");
            akses = (String) data.get("akses");

//            System.out.println("Body Response : "+ltMenu);
//            System.out.println("Token JWT : "+tokenJwt);

        }catch (FeignCustomException e){
            Map<String, Object> errorBody = e.getErrorBody();
            model.addAttribute("errorMessage", errorBody.get("message"));
            GlobalFunction.getCaptchaLogin(valLoginDTO);
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);
            return ConstantPage.LOGIN_PAGE;

        }
        webRequest.setAttribute("MENU_NAVBAR",menuNavBar,1);
        webRequest.setAttribute("JWT",tokenJwt,1    );
        webRequest.setAttribute("USR_NAME",valLoginDTO.getUsername(),1);
        webRequest.setAttribute("PASSWORD",valLoginDTO.getPassword(),1);
        webRequest.setAttribute("AKSES",akses,1);

        GlobalFunction.setGlobalAttribute(model, webRequest);
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

        }catch (FeignCustomException e){
            Map<String, Object> errorBody = e.getErrorBody();
            model.addAttribute("errorMessage", errorBody.get("message"));
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);

            return ConstantPage.REGISTER_PAGE;

        }
//        webRequest.setAttribute("MENU_NAVBAR",menuNavBar,1);
//        model.addAttribute("MENU_NAVBAR",menuNavBar);

        return "redirect:"+ConstantPage.VERIFICATION_PAGE;
    }

    @GetMapping("/verification")
    public String verificationPage(Model model) {

        ValVerifyOTPRegisDTO valVerifyOTPRegisDTO = new ValVerifyOTPRegisDTO();
        model.addAttribute("verification", valVerifyOTPRegisDTO);
        model.addAttribute("logo", ConstantValue.LOGIN_LOGO);

        return ConstantPage.VERIFICATION_PAGE;
    }

    @PostMapping("/verification")
    public String verification(
            @ModelAttribute("verification") @Valid ValVerifyOTPRegisDTO valVerifyOTPRegisDTO,
            BindingResult result,
            Model model,
            WebRequest webRequest) {

        if (result.hasErrors()) {
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);
            return ConstantPage.VERIFICATION_PAGE;
        }

        /** REQUEST Verify */
        ResponseEntity<Object> response = null;

        try{

            response = authService.verifyRegis(valVerifyOTPRegisDTO);
            System.out.println(response);

        }catch (FeignCustomException e){
            Map<String, Object> errorBody = e.getErrorBody();
            model.addAttribute("errorMessage", errorBody.get("message"));
            model.addAttribute("logo", ConstantValue.LOGIN_LOGO);

            return ConstantPage.VERIFICATION_PAGE;

        }
//        webRequest.setAttribute("MENU_NAVBAR",menuNavBar,1);
//        model.addAttribute("MENU_NAVBAR",menuNavBar);

        return "/auth/form-success-verification";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:"+ConstantPage.DEFAULT_PAGE;
    }

}
