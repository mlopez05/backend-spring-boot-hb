package com.umg.microservicios.citas_service.config;
 
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "ecosistema.exchange.topicos";
 
    public static final String RK_CITA_CONFIRMADA   = "cita.confirmada";
    public static final String RK_CITA_REPROGRAMADA = "cita.reprogramada";
    public static final String RK_CITA_CANCELADA    = "cita.cancelada";
    public static final String RK_CITA_RECORDATORIO = "cita.recordatorio";
 
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
