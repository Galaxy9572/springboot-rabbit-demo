package org.jy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitConfig {

    private CachingConnectionFactory connectionFactory;

    @Autowired
    public RabbitConfig(CachingConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * 配置Direct类型的exchange
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange helloWorldExchange() {
        return new DirectExchange("hello_world_exchange", true, false, null);
    }

    /**
     * 声明一个队列
     *
     * @return Queue
     */
    @Bean
    public Queue helloWorldQueue() {
        return new Queue("hello_world_queue", true, false, false, null);
    }

    /**
     * 绑定exchange核queue
     *
     * @param queue          Queue
     * @param helloWorldExchange DirectExchange
     * @return Binding
     */
    @Bean
    public Binding binding(@Qualifier("helloWorldQueue") Queue queue, @Qualifier("helloWorldExchange") DirectExchange helloWorldExchange) {
        return BindingBuilder.bind(queue).to(helloWorldExchange).with("hello_world");
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        //若使用confirm-callback或return-callback，必须要配置publisherConfirms或publisherReturns为true
        //每个rabbitTemplate只能有一个confirm-callback和return-callback，如果这里配置了，那么写生产者的时候不能再写confirm-callback和return-callback
        //使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
//        /**
//         * 如果消息没有到exchange,则confirm回调,ack=false
//         * 如果消息到达exchange,则confirm回调,ack=true
//         * exchange到queue成功,则不回调return
//         * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
//         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
            } else {
                log.info("消息发送失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
            }
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
        });
        return rabbitTemplate;
    }

}
