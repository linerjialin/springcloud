package com.liner.springcloud.service;

import com.liner.springcloud.entities.CommonResult;
import com.liner.springcloud.entities.Payment;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("cloud-payment-service")
public interface PaymentFeignService {

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

    //测试超时控制
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout();
}
