package org.jy.message.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

@Service
public class MessageProducer {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void send() {
        MessageProperties properties = new MessageProperties();
        properties.setContentEncoding("UTF-8");
        properties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        Message message = new Message("I am a Message".getBytes(), properties);
        rabbitTemplate.send("hello_world_exchange", "hello_world", message);
    }

}
