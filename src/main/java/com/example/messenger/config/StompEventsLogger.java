package com.example.messenger.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

@Slf4j
@Component
public class StompEventsLogger {

    @EventListener
    public void onConnect(SessionConnectEvent e) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(e.getMessage());
        log.info("STOMP CONNECT session={} headers={}", acc.getSessionId(), acc.toNativeHeaderMap());
    }

    @EventListener
    public void onConnected(SessionConnectedEvent e) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(e.getMessage());
        log.info("STOMP CONNECTED session={}", acc.getSessionId());
    }

    @EventListener
    public void onSubscribe(SessionSubscribeEvent e) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(e.getMessage());
        log.info("STOMP SUBSCRIBE session={} destination={}", acc.getSessionId(), acc.getDestination());
    }

    @EventListener
    public void onUnsubscribe(SessionUnsubscribeEvent e) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(e.getMessage());
        log.info("STOMP UNSUBSCRIBE session={} id={}", acc.getSessionId(), acc.getSubscriptionId());
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent e) {
        log.info("STOMP DISCONNECT session={} closeStatus={}", e.getSessionId(), e.getCloseStatus());
    }
}
