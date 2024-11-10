package com.sofka.bingo_backend.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Configura el broker de mensajes
    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Habilita el broker para mensajes salientes
        config.setApplicationDestinationPrefixes("/app"); // Establece el prefijo para mensajes entrantes
    }

    // Registra el endpoint de STOMP para la conexión WebSocket
    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:5500", "http://127.0.0.1:5500").withSockJS(); // Define el endpoint y los orígenes permitidos
    }
}
