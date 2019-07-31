package org.jy.meesage.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = {"hello_world_queue"})
@Slf4j
public class MessageConsumer{

    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws IOException {
        log.info("收到消息:{}", new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

}
