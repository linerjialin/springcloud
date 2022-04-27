package com.liner.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.serialNumber;

@Service
public class PaymentService {

  // ===========服务降级

  /**
   * 正常访问
   *
   * @param id
   * @return
   */
  public String paymentInfo_OK(Integer id) {
    return "线程池:"
        + Thread.currentThread().getName()
        + "  paymentInfo_OK,  id:"
        + id
        + "\t"
        + "O(∩_∩)O";
  }

  /**
   * 异常访问
   *
   * @param id
   * @return
   */
  @HystrixCommand(
      fallbackMethod = "paymentInfo_TimeOutHandler",
      commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
      })

  // 三秒以内走正常逻辑。超过3秒 fallback 兜底  不管出现什么异常都可兜底
  public String paymentInfo_TimeOut(Integer id) {
    int time = 5;
    try {
      TimeUnit.SECONDS.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "线程池:"
        + Thread.currentThread().getName()
        + "  paymentInfo_TimeOut,  id:"
        + id
        + "\t"
        + "耗时 "
        + time
        + " 秒钟";
  }

  public String paymentInfo_TimeOutHandler(Integer id) {
    return "线程池:"
        + Thread.currentThread().getName()
        + " 系统繁忙。请稍后再试,  id:"
        + id
        + "\t"
        + "/(ㄒoㄒ)/~~";
  }

  // ===========服务熔断

  @HystrixCommand(
      fallbackMethod = "paymentCircuitBreaker_fallback",
      commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),  //是否开启断路器
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间窗口期
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), //失败率到达多少 跳闸
      })
  public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
    if (id < 0) throw new RuntimeException("******id不能负数");
    String serialNumber = IdUtil.simpleUUID(); //== UUID.randomUUID().toString()
    return Thread.currentThread().getName() + "\t" + "调用成功，流水号: " + serialNumber;
  }

  public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
    return "id不能负数，请稍后再试,(ToT)/~~id: " + id;
  }
}
