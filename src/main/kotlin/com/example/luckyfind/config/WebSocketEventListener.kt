package com.example.luckyfind.config

import com.example.luckyfind.domain.enum.MessageType
import com.example.luckyfind.model.ChatMessage
import org.slf4j.*
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener(
    private val messageTemplate: SimpMessageSendingOperations?
) {
    private val logger: Logger? = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    @EventListener
    fun handleWebSocketConnetListener(event: SessionConnectedEvent?) {
        logger?.info("Received a new Web Socket connection")
    }

    @EventListener
    fun handleWebSocketDisconnectListner(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes!!["username"] as String?
        if (username != null) {
            logger?.info("User Disconnectes : $username")
            val chatMessage = ChatMessage(MessageType.LEAVE, "", username)

            messageTemplate!!.convertAndSend("/topic/public", chatMessage)
        }
    }
}