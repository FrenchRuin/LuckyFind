package com.example.luckyfind.config

import com.example.luckyfind.domain.enum.MessageType
import com.example.luckyfind.model.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener(
    private val messageTemplate: SimpMessageSendingOperations?
) {
    private val logger: Logger? = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent?) {
        logger?.info("Received a new Web Socket connection")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes!!["username"] as String?
        if (username != null) {
            logger?.info("User Disconnects : $username")
            val chatMessage = Message(MessageType.LEAVE, "", username)

            messageTemplate!!.convertAndSend("/topic/public", chatMessage)
        }
    }
}