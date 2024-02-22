package com.koh.exchangeCode.controller;

import com.koh.exchangeCode.service.ExchangeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class ExchangeCodeController {

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @GetMapping("/generateExchangeCode")
    public String generateExchangeCode() {
        return exchangeCodeService.generateExchangeCode();
    }
}
