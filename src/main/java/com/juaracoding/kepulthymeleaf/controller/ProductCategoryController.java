package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.dto.response.RespProductDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValProductCategoryDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValProductDTO;
import com.juaracoding.kepulthymeleaf.httpservice.ProductCategoryService;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import jakarta.validation.Valid;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("product-category")
public class ProductCategoryController {

    @Autowired
    ProductCategoryService productCategoryService;

    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    @GetMapping
    public String findAll(Model model,
                          WebRequest request) {

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);

        if (jwt.equals(ConstantPage.LOGIN_PAGE)) {
            return jwt;
        }
        Map<String, Object> mResponse = null;
        try {
            response = productCategoryService.findAll(jwt);

            mResponse = (Map<String, Object>) response.getBody();
            GlobalFunction.setDataMainPage(model,request,mResponse,
                    "product-category",filterColumn);

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
    @PostMapping
    public String save(
            @ModelAttribute("data") @Valid ValProductCategoryDTO valProductCategoryDTO,
            BindingResult bindingResult,
            Model model,
            WebRequest webRequest){

        if(bindingResult.hasErrors()){
            model.addAttribute("data",valProductCategoryDTO);
            return ConstantPage.PRODUCT_CATEGORY_ADD_PAGE;
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = productCategoryService.save(jwt,valProductCategoryDTO);
        }catch (Exception e){
            model.addAttribute("data",valProductCategoryDTO);
            return ConstantPage.PRODUCT_CATEGORY_ADD_PAGE;
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
        return ConstantPage.PRODUCT_CATEGORY_ADD_PAGE;
    }

}
