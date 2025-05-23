package com.juaracoding.kepulthymeleaf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.kepulthymeleaf.dto.relation.RelProductDTO;
import com.juaracoding.kepulthymeleaf.dto.relation.RelStatusDTO;
import com.juaracoding.kepulthymeleaf.dto.report.RepTransactionDTO;
import com.juaracoding.kepulthymeleaf.dto.response.RespProductDTO;
import com.juaracoding.kepulthymeleaf.dto.response.RespTransactionDTO;
import com.juaracoding.kepulthymeleaf.dto.validation.*;
import com.juaracoding.kepulthymeleaf.httpservice.ProductService;
import com.juaracoding.kepulthymeleaf.httpservice.TransactionService;
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
import java.util.*;
import java.util.stream.Collectors;

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

        }catch (Exception e){
            return "redirect:/transaction";
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
        RelStatusDTO relStatusDTO = new RelStatusDTO();

        if (selectTransactionDTO.getNamaStatus().equals("Approved")) {
            relStatusDTO.setNama(selectTransactionDTO.getNamaStatus());
            relStatusDTO.setId(2L);
            valTransactionDTO.setStatus(relStatusDTO);

        } else if (selectTransactionDTO.getNamaStatus().equals("Waiting for Approval")) {
            relStatusDTO.setNama(selectTransactionDTO.getNamaStatus());
            relStatusDTO.setId(1L);
            valTransactionDTO.setStatus(relStatusDTO);
        }
        valTransactionDTO.setLtProduct(relProductDTOList);
        return valTransactionDTO;
    }

    /** fungsi untuk mengambil data web yang sudah di set sebelumnya di function openModalAdd , agar tidak menghubungi server lagi meminta data product yang sama */
    private void setDataTempAdd(Model model , WebRequest webRequest){
        Long data1[] = (Long[]) webRequest.getAttribute("data1",1);//menampung data id dari all product
        String data2[] = (String[]) webRequest.getAttribute("data2",1);//menampung data nama dari all product
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

        model.addAttribute("data",new SelectTransactionDTO());
        model.addAttribute("x",ltProduct);

        return ConstantPage.TRANSACTION_ADD_PAGE;
    }

    @GetMapping("/e/{id}")
    public String openModalsEdit(
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){
        ResponseEntity<Object> response = null;
        ResponseEntity<Object> responseProduct = null;
        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }
        try{
            response = transactionService.findById(jwt,id);
            responseProduct = productService.allProduct(jwt);
        }catch (Exception e){

        }
        /** cara untuk menyimpan data di session, kurang lebih sama untuk CSR data nya disimpan di dalam storage di web browser
         *  Notes : table session tidak dapat menyimpan data berbentuk object serialize, sehingga nanti dirangkai menjadi array 1 atau 2 dimensi tergantung kebuuthan
         */
//        setDataMenuToEdit(response,responseProduct,model,webRequest);
        Map<String,Object> map = (Map<String, Object>) response.getBody();
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");
        Map<String,Object> mapProduct = (Map<String, Object>) responseProduct.getBody();
        List<Map<String,Object>> listMapDataProduct = (List<Map<String, Object>>) mapProduct.get("data");

        Map<String,Object> mapStatus = (Map<String, Object>) mapData.get("status");
        String status = (String) mapStatus.get("nama");

        List<Map<String, Object>> listMapProduct = (List<Map<String, Object>>) mapData.get("ltProduct");

        RepTransactionDTO repTransactionDTO = new RepTransactionDTO();
        Set<Integer> productSelected = new HashSet<>();

        for (Map<String,Object> product: listMapDataProduct ) {
            for(Map<String,Object> p:listMapProduct){
                if((Integer)product.get("id")==(Integer) p.get("id")) {
                    productSelected.add(((Integer) product.get("id")));
                }
            }
        }

        repTransactionDTO.setId(id);
        repTransactionDTO.setNamaStatus(status);
        repTransactionDTO.setLtProduct(listMapProduct);
        model.addAttribute("data", repTransactionDTO);
        model.addAttribute("listMapDataProduct", listMapDataProduct);
        model.addAttribute("productSelected", productSelected);

//        return "/request/test";
        return ConstantPage.TRANSACTION_EDIT_PAGE;
    }


    @PostMapping("/e/{id}")
    public String edit(
            @ModelAttribute("data") @Valid SelectTransactionDTO selectTransactionDTO,
            BindingResult bindingResult,
            Model model,
            @PathVariable(value = "id") Long id,
            WebRequest webRequest){

        ResponseEntity<Object> response = null;
        ResponseEntity<Object> responseFind = null;
        ValTransactionDTO valTransactionDTO = new ValTransactionDTO();

        try {
            if (selectTransactionDTO.getNamaDivisi().equals("Approved")) {
                String jwt = GlobalFunction.tokenCheck(model, webRequest);
                if(jwt.equals(ConstantPage.LOGIN_PAGE)){
                    return jwt;
                }
                responseFind = transactionService.findById(jwt,id);

                Map<String,Object> mapFind = (Map<String, Object>) responseFind.getBody();
                Map<String,Object> mapFindData = (Map<String, Object>) mapFind.get("data");
                List<Map<String,Object>> listFindProduct = (List<Map<String, Object>>) mapFindData.get("ltProduct");

                List<String> listProduct = new ArrayList<>();
                String temp = "";

                for (Map<String,Object> product: listFindProduct ) {
                    temp = product.get("id").toString();
                    listProduct.add(temp);
                }

                selectTransactionDTO.setLtProduct(listProduct);

                valTransactionDTO = convertToValTransactionDTO(selectTransactionDTO);

            }

        } catch (Exception e) {
            valTransactionDTO = convertToValTransactionDTO(selectTransactionDTO);
        }
        selectTransactionDTO.setId(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("data",selectTransactionDTO);
            return ConstantPage.TRANSACTION_EDIT_PAGE;
        }

        String jwt = GlobalFunction.tokenCheck(model, webRequest);
        if(jwt.equals(ConstantPage.LOGIN_PAGE)){
            return jwt;
        }

        try{
            response = transactionService.edit(jwt,id,valTransactionDTO);
        }catch (Exception e){
            model.addAttribute("data",valTransactionDTO);
            return ConstantPage.TRANSACTION_EDIT_PAGE;
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
            response = transactionService.findByParam(jwt,sort,sortBy,page,size,column,value);
            Map<String,Object> mResponse = (Map<String, Object>) response.getBody();
            GlobalFunction.setDataMainPage(model,webRequest,mResponse,
                    "transaction",filterColumn);
//            System.out.println("Body Response : "+response.getBody());
        }catch (Exception e){
            return "redirect:/transaction";
        }
        return ConstantPage.TRANSACTION_MAIN_PAGE;
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
            response = transactionService.downloadPdf(jwt,column,value);
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
            response = transactionService.delete(jwt,id);
        }catch (Exception e){
            return ConstantPage.TRANSACTION_MAIN_PAGE;
        }

        return "redirect:/transaction";
    }

}
