package com.liner.springcloud.service.impl;

import com.liner.springcloud.service.IMessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

@EnableBinding(Source.class) // 定义消息生产者的发送管道
public class IMessageProviderImpl implements IMessageProvider {

  @Resource private MessageChannel output;

  @Override
  public String send() {

    String serial = UUID.randomUUID().toString();
    output.send(MessageBuilder.withPayload(serial).build()); // 消息构建器构建
    System.out.println("**************  serial: " + serial);
    return null;
  }
}
