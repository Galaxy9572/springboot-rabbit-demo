package org.jy;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class RabbitDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitDemoApplication.class, args);
    }

}
