package com.juaracoding.kepulthymeleaf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.kepulthymeleaf.dto.response.RespProductCategoryDTO;
import com.juaracoding.kepulthymeleaf.dto.response.RespProductDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValProductCategoryDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValProductDTO;
import com.juaracoding.kepulthymeleaf.httpservice.ProductCategoryService;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import feign.Response;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("product-category")
public class ProductCategoryController {

    @Autowired
    ProductCategoryService productCategoryService;

    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    public ProductCategoryController() {
        filterColumn.put("nama","Nama");

    }

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
            return "redirect:/er";
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

    @GetMapping("/{idComp}/{descComp}/{sort}/{sortBy}/{page}")
    public String dataTable(Model model,
                            @PathVariable(value = "sort") String sort,
                            @PathVariable(value = "sortBy") String sortBy,//name
                            @PathVariable(value = "page") Integer page,
                            @RequestParam(value = "size") Integer size,
                            @RequestParam(value = "column") String column,
                            @RequestParam(value = "value") String value,
                            @PathVariable(value = "idComp") String idComp,
                            @PathVariable(value = "descComp") String descComp,
                            WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model,webRequest);
        page = page!=0?(page-1):page;
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }
        Map<String,Object> mResponse = null;
        try{
            response = productCategoryService.findByParam(jwt,sort,sortBy,page,size,column,value);
            mResponse = (Map<String, Object>) response.getBody();
        }catch (Exception e){
            GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                    "product-category",filterColumn);
            model.addAttribute("idComp", idComp);
            model.addAttribute("descComp",descComp);
            return ConstantPage.DATA_TABLE_MODALS;
        }

        GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                "product-category",filterColumn);
        model.addAttribute("idComp", idComp);
        model.addAttribute("descComp",descComp);
        return ConstantPage.DATA_TABLE_MODALS;
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
            response = productCategoryService.findByParam(jwt,sort,sortBy,page,size,column,value);
            Map<String,Object> mResponse = (Map<String, Object>) response.getBody();
            GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                    "product-category",filterColumn);
//            System.out.println("Body Response : "+response.getBody());
        }catch (Exception e){
            return "redirect:/er";
        }
        return ConstantPage.PRODUCT_CATEGORY_MAIN_PAGE;
    }
    @GetMapping("/{id}")
    public String findById(
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){

        return null;
    }
    @GetMapping("/e/{id}")
    public String openModalsEdit(
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }
        try{
            response = productCategoryService.findById(jwt,id);
        }catch (Exception e){

        }
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
        model.addAttribute("data",new ObjectMapper().convertValue(mapData, RespProductCategoryDTO.class));
        return ConstantPage.PRODUCT_CATEGORY_EDIT_PAGE;
    }
    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data") @Valid ValProductCategoryDTO valProductCategoryDTO,
            BindingResult bindingResult,
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){
        if(bindingResult.hasErrors()){
            model.addAttribute("data",valProductCategoryDTO);
            return ConstantPage.PRODUCT_CATEGORY_EDIT_PAGE;
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = productCategoryService.edit(jwt,id,valProductCategoryDTO);
        }catch (Exception e){
            model.addAttribute("data",valProductCategoryDTO);
            return ConstantPage.PRODUCT_CATEGORY_EDIT_PAGE;
        }
        return ConstantPage.SUCCESS_MESSAGE;
    }
    @GetMapping("/d/{id}")
    public String delete(
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = productCategoryService.delete(jwt,id);
        }catch (Exception e){
            return ConstantPage.PRODUCT_CATEGORY_MAIN_PAGE;
        }

        return "redirect:/product-category";
    }

    @GetMapping("/pdf")
    public ResponseEntity<Resource> downloadPdf(
            Model model,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            WebRequest webRequest
    ){

        ByteArrayResource resource =null;
        Response response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        String fileName = "";
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resource);
        }
        try{
            response = productCategoryService.downloadPdf(jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            System.out.println("Value Content-Disposition Server : "+fileName);
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition",fileName.substring(0,fileName.length()-1));

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/pdf")).
                body(resource);
    }
}
