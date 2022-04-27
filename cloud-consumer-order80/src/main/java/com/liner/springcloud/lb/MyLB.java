package com.liner.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLB implements LoadBalancer {

  // 原子类
  private AtomicInteger atomicInteger = new AtomicInteger(0);

  // 先得到再增加
  public final int getAndIncrement() {
    int current;
    int next;

    // 得到访问次数
    do {
      current = this.atomicInteger.get();
      next = current >= 2147483647 ? 0 : current + 1; // 2147483647 整形最大值
      System.out.println("*******第" + next + "次访问次数next");
      return next;
    } while (this.atomicInteger.compareAndSet(current, next));
  }

  @Override
  public ServiceInstance instances(List<ServiceInstance> serviceInstances) {

    //负载均衡算法
    int index = getAndIncrement() % serviceInstances.size();
    return serviceInstances.get(index);
  }
}
