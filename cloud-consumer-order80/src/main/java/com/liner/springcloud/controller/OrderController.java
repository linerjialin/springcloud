package com.liner.springcloud.controller;


import com.liner.springcloud.entities.CommonResult;
import com.liner.springcloud.entities.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/consumer")
public class OrderController {

    public static final String PAYENT_URL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/payment/create")
    public CommonResult<Payment> create(Payment payment) {
        return restTemplate.postForObject(PAYENT_URL+"/payment/create",payment,CommonResult.class);
    }
    @GetMapping("payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") long id) {
        return restTemplate.getForObject(PAYENT_URL+"/payment/get/"+id,CommonResult.class);
    }
}
