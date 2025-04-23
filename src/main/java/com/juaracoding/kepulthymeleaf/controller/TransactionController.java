package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.dto.relation.RelProductDTO;
import com.juaracoding.kepulthymeleaf.dto.report.RepTransactionDTO;
import com.juaracoding.kepulthymeleaf.dto.response.RespProductDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.SelectProductDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.SelectTransactionDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValProductCategoryDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.ValTransactionDTO;
import com.juaracoding.kepulthymeleaf.httpservice.ProductService;
import com.juaracoding.kepulthymeleaf.httpservice.TransactionService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductService productService;

    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    public TransactionController() {
        filterColumn.put("divisi","Divisi");
        filterColumn.put("admin","Admin");
        filterColumn.put("status","Status");
    }

    @GetMapping
    public String findAll(Model model, WebRequest webRequest){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = transactionService.findAll(jwt);
            Map<String,Object> mResponse = (Map<String, Object>) response.getBody();
            GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                    "transaction",filterColumn);
//            System.out.println("Body Response : "+response.getBody());
            Map<String,Object> data = (Map<String, Object>) mResponse.get("data");//element yang di dalam paging
            List<RepTransactionDTO> repTransactionDTOList = (List<RepTransactionDTO>) data.get("content");//element yang di dalam paging
            model.addAttribute("transactionList",repTransactionDTOList);

        }catch (Exception e){
            return "redirect:/er";
        }
        return ConstantPage.TRANSACTION_MAIN_PAGE;
    }

    @PostMapping
    public String save(
            @ModelAttribute("data") @Valid SelectTransactionDTO selectTransactionDTO,
            BindingResult bindingResult,
            Model model,
            WebRequest webRequest){

        ValTransactionDTO valTransactionDTO = convertToValTransactionDTO(selectTransactionDTO);
        if(bindingResult.hasErrors()){
            model.addAttribute("data",selectTransactionDTO);
            setDataTempAdd(model,webRequest);
            return ConstantPage.TRANSACTION_ADD_PAGE;
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = transactionService.save(jwt,valTransactionDTO);
        }catch (Exception e){
            model.addAttribute("data",selectTransactionDTO);
            setDataTempAdd(model,webRequest);
            return ConstantPage.TRANSACTION_ADD_PAGE;
        }
        return ConstantPage.SUCCESS_MESSAGE;
    }

    private ValTransactionDTO convertToValTransactionDTO(SelectTransactionDTO selectTransactionDTO){
        ValTransactionDTO valTransactionDTO = new ValTransactionDTO();
        List<RelProductDTO> relProductDTOList = new ArrayList<>();
        RelProductDTO relProductDTO = new RelProductDTO();
        for (String s:
                selectTransactionDTO.getLtProduct()) {
            relProductDTO = new RelProductDTO();
            relProductDTO.setId(Long.parseLong(s));
            relProductDTOList.add(relProductDTO);
        }
        valTransactionDTO.setLtProduct(relProductDTOList);
        return valTransactionDTO;
    }

    /** fungsi untuk mengambil data web yang sudah di set sebelumnya di function openModalAdd , agar tidak menghubungi server lagi meminta data menu yang sama */
    private void setDataTempAdd(Model model , WebRequest webRequest){
        Long data1[] = (Long[]) webRequest.getAttribute("data1",1);//menampung data id dari all menu
        String data2[] = (String[]) webRequest.getAttribute("data2",1);//menampung data nama dari all menu
        List<SelectProductDTO> listAllProduct = new ArrayList<>();
        SelectProductDTO selectProductDTO = null;
        for(int i=0;i<data1.length;i++){
            selectProductDTO = new SelectProductDTO();
            selectProductDTO.setId(data1[i]);
            selectProductDTO.setNama(data2[i]);
            listAllProduct.add(selectProductDTO);
        }
        model.addAttribute("x",listAllProduct);
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
        try{
            response = productService.allProduct(jwt);
        }catch (Exception e){
        }

        Map<String,Object> map = (Map<String, Object>) response.getBody();
        List<Map<String,Object>> ltProduct = (List<Map<String,Object>>) map.get("data");
        int ltProductSize = ltProduct.size();
        Long[] data1 = new Long[ltProductSize];
        String[] data2 = new String[ltProductSize];
        int i=0;
        for (Map<String,Object> map1 : ltProduct) {
            data1[i] = Long.parseLong(map1.get("id").toString());
            data2[i] = (String) map1.get("nama");
            i++;
        }
        webRequest.setAttribute("data1",data1,1);
        webRequest.setAttribute("data2",data2,1);

        model.addAttribute("data",new ValTransactionDTO());
        model.addAttribute("x",ltProduct);

        return ConstantPage.TRANSACTION_ADD_PAGE;
    }


}
