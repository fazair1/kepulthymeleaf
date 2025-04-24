package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.dto.relation.RelAksesDTO;
import com.juaracoding.kepulthymeleaf.dto.relation.RelProductDTO;
import com.juaracoding.kepulthymeleaf.dto.relation.RelStatusDTO;
import com.juaracoding.kepulthymeleaf.dto.report.RepTransactionDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.*;
import com.juaracoding.kepulthymeleaf.handler.FeignCustomException;
import com.juaracoding.kepulthymeleaf.httpservice.ProductService;
import com.juaracoding.kepulthymeleaf.httpservice.UserService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @PostMapping
    public String save(
            @ModelAttribute("data") @Valid SelectUserDTO selectUserDTO,
            BindingResult bindingResult,
            Model model,
            WebRequest webRequest){

        ValUserDTO valUserDTO = convertToValUserDTO(selectUserDTO);
        if(bindingResult.hasErrors()){
            model.addAttribute("data",selectUserDTO);
//            setDataTempAdd(model,webRequest);
            return ConstantPage.USER_ADD_PAGE;
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = userService.save(jwt,valUserDTO);
        }catch (Exception e){
            model.addAttribute("data",selectUserDTO);
//            setDataTempAdd(model,webRequest);
            return ConstantPage.USER_ADD_PAGE;
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


        model.addAttribute("data",new SelectUserDTO());

        return ConstantPage.USER_ADD_PAGE;
    }

    private ValUserDTO convertToValUserDTO(SelectUserDTO selectUserDTO){
        ValUserDTO valUserDTO = new ValUserDTO();
        RelAksesDTO relAksesDTO = new RelAksesDTO();

        if (selectUserDTO.getAkses().equals("Admin")) {
            relAksesDTO.setNama(selectUserDTO.getAkses());
            relAksesDTO.setId(1L);
            valUserDTO.setAkses(relAksesDTO);

        } else if (selectUserDTO.getAkses().equals("Member")) {
            relAksesDTO.setNama(selectUserDTO.getAkses());
            relAksesDTO.setId(1L);
            valUserDTO.setAkses(relAksesDTO);
        }
        valUserDTO.setEmail(selectUserDTO.getEmail());
        valUserDTO.setNama(selectUserDTO.getNama());
        valUserDTO.setUsername(selectUserDTO.getUsername());
        valUserDTO.setPassword(selectUserDTO.getPassword());
        return valUserDTO;
    }

    private ValEditUserDTO convertToValEditUserDTO(SelectUserDTO selectUserDTO){
        ValEditUserDTO valEditUserDTO = new ValEditUserDTO();
        RelAksesDTO relAksesDTO = new RelAksesDTO();

        if (selectUserDTO.getAkses().equals("Admin")) {
            relAksesDTO.setNama(selectUserDTO.getAkses());
            relAksesDTO.setId(1L);
            valEditUserDTO.setAkses(relAksesDTO);

        } else if (selectUserDTO.getAkses().equals("Member")) {
            relAksesDTO.setNama(selectUserDTO.getAkses());
            relAksesDTO.setId(2L);
            valEditUserDTO.setAkses(relAksesDTO);
        }
        valEditUserDTO.setEmail(selectUserDTO.getEmail());
        valEditUserDTO.setNama(selectUserDTO.getNama());
        valEditUserDTO.setUsername(selectUserDTO.getUsername());
        valEditUserDTO.setPassword(selectUserDTO.getPassword());
        valEditUserDTO.setRegistered(selectUserDTO.getRegistered());
        return valEditUserDTO;
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
            response = userService.findById(jwt,id);
        }catch (Exception e){

        }
        /** cara untuk menyimpan data di session, kurang lebih sama untuk CSR data nya disimpan di dalam storage di web browser
         *  Notes : table session tidak dapat menyimpan data berbentuk object serialize, sehingga nanti dirangkai menjadi array 1 atau 2 dimensi tergantung kebuuthan
         */
//        setDataMenuToEdit(response,responseProduct,model,webRequest);
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");

        Map<String,Object> mapAkses = (Map<String, Object>) mapData.get("akses");
        String akses = (String) mapAkses.get("nama");

        SelectUserDTO selectUserDTO = new SelectUserDTO();

        selectUserDTO.setId(id);
        selectUserDTO.setUsername((String) mapData.get("username"));
        selectUserDTO.setEmail((String) mapData.get("email"));
        selectUserDTO.setNama((String) mapData.get("nama"));
        selectUserDTO.setAkses(akses);
        selectUserDTO.setRegistered((Boolean) mapData.get("registered"));

        model.addAttribute("data", selectUserDTO);

        return ConstantPage.USER_EDIT_PAGE;
    }

    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data") @Valid SelectUserDTO selectUserDTO,
            BindingResult bindingResult,
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){

        selectUserDTO.setId(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("data",selectUserDTO);
            return ConstantPage.USER_EDIT_PAGE;
        }

        ValEditUserDTO valEditUserDTO = convertToValEditUserDTO(selectUserDTO);
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = userService.edit(jwt,id,valEditUserDTO);
        }catch (FeignCustomException e){
            Map<String, Object> errorBody = e.getErrorBody();
            model.addAttribute("data",selectUserDTO);
            return ConstantPage.USER_EDIT_PAGE;
        }
        return ConstantPage.SUCCESS_MESSAGE;
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
            response = userService.findByParam(jwt,sort,sortBy,page,size,column,value);
            Map<String,Object> mResponse = (Map<String, Object>) response.getBody();
            GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                    "user",filterColumn);
//            System.out.println("Body Response : "+response.getBody());
        }catch (Exception e){
            return "redirect:/user";
        }
        return ConstantPage.USER_MAIN_PAGE;
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
            response = userService.delete(jwt,id);
        }catch (Exception e){
            return ConstantPage.USER_MAIN_PAGE;
        }

        return "redirect:/user";
    }

}
