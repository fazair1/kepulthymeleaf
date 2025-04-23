package com.juaracoding.kepulthymeleaf.controller;

import com.juaracoding.kepulthymeleaf.dto.report.RepTransactionDTO;
import com.juaracoding.kepulthymeleaf.httpservice.TransactionService;
import com.juaracoding.kepulthymeleaf.utils.ConstantPage;
import com.juaracoding.kepulthymeleaf.utils.GlobalFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

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

}
