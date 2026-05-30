package com.umg.microservicios.notificacion_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {
    
    public static final String EXCHANGE_NAME = "ecosistema.exchange.topicos";
    public static final String COLA_NOTIFICACIONES = "cola.notificaciones.general";
    public static final String PATRON_ENRUTAMIENTO = "#";

    @Bean
    public TopicExchange globalExchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue notificacionesQueue(){
        return new Queue(COLA_NOTIFICACIONES, true);
    }

    @Bean
    public Binding bindingNotificaciones(Queue notificacionesQueue, TopicExchange globalExchange) {
        return BindingBuilder.bind(notificacionesQueue).to(globalExchange).with(PATRON_ENRUTAMIENTO);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
    }
}
