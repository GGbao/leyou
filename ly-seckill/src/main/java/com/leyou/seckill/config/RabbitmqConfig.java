package com.leyou.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConfig {

    /**
     * 死信交换机
     *
     * @return
     */
    @Bean
    public DirectExchange userOrderDelayExchange() {
        return new DirectExchange("exchange.delay.order.begin");
    }

    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue userOrderDelayQueue() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("x-dead-letter-exchange", "exchange.delay.order.done");
        map.put("x-dead-letter-routing-key", "delay");
        return new Queue("queue.delay.order.begin", true, false, false, map);
    }

    /**
     * 给死信队列绑定交换机
     *
     * @return
     */
    @Bean
    public Binding userOrderDelayBinding() {
        return BindingBuilder.bind(userOrderDelayQueue()).to(userOrderDelayExchange()).with("delay");
    }

    /**
     * 死信接收交换机
     *
     * @return
     */
    @Bean
    public DirectExchange userOrderReceiveExchange() {
        return new DirectExchange("exchange.delay.order.done");
    }

    /**
     * 死信接收队列
     *
     * @return
     */
    @Bean
    public Queue userOrderReceiveQueue() {
        return new Queue("queue.delay.order.done");
    }

    /**
     * 死信交换机绑定消费队列
     *
     * @return
     */
    @Bean
    public Binding userOrderReceiveBinding() {
        return BindingBuilder.bind(userOrderReceiveQueue()).to(userOrderReceiveExchange()).with("delay");
    }
}
 

