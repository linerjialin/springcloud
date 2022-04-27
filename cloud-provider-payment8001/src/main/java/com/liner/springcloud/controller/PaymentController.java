package com.liner.springcloud.controller;

import com.liner.springcloud.entities.CommonResult;
import com.liner.springcloud.entities.Payment;
import com.liner.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {
  @Resource private PaymentService paymentService;

  @Value("${server.port}")
  private String serverPort;

  @Resource private DiscoveryClient discoveryClient;

  @PostMapping("/create")
  public CommonResult<Payment> create(@RequestBody Payment payment) {
    int result = paymentService.create(payment);
    log.info("******插入的结果" + result);
    return result > 0
        ? new CommonResult(200, "插入数据库成功,serverPort:" + serverPort, result)
        : new CommonResult(444, "插入数据库失败", null);
  }

  @GetMapping("/get/{id}")
  public CommonResult getPaymentById(@PathVariable("id") Long id) {
    Payment paymentById = paymentService.getPaymentById(id);
    log.info("******查询的结果" + paymentById);
    return paymentById != null
        ? new CommonResult(200, "查询成功,serverPort:" + serverPort, paymentById)
        : new CommonResult(444, "没有对应记录，查询ID" + id, null);
  }

  @GetMapping(value = "/discovery")
  public Object discovery() {
    List<String> services = discoveryClient.getServices();
    for (String element : services) {
      log.info("******element:  " + element);
    }
    List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
    for (ServiceInstance instance : instances) {
      log.info(
          "******instance:  "
              + instance.getServiceId()
              + "\t"
              + instance.getHost()
              + "\t"
              + instance.getPort()
              + "\t"
              + instance.getUri());
    }
    return this.discoveryClient;

  }
}
