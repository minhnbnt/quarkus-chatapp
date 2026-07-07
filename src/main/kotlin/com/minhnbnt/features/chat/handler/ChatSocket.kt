package com.minhnbnt.features.chat.handler

import com.minhnbnt.domain.ChatMessage
import com.minhnbnt.features.chat.service.ChatService
import io.quarkus.websockets.next.OnClose
import io.quarkus.websockets.next.OnOpen
import io.quarkus.websockets.next.OnTextMessage
import io.quarkus.websockets.next.WebSocket
import io.quarkus.websockets.next.WebSocketConnection

@WebSocket(path = "/chat/{userId}")
class ChatSocket(private val chatService: ChatService) {

    @OnOpen
    suspend fun onOpen(connection: WebSocketConnection) {
        val userId = connection.pathParam("userId")
        chatService.onUserConnected(userId, connection)
    }

    @OnTextMessage
    suspend fun onMessage(message: ChatMessage) {
        chatService.onMessageReceived(message)
    }

    @OnClose
    suspend fun onClose(connection: WebSocketConnection) {
        val userId = connection.pathParam("userId")
        chatService.onUserDisconnected(userId, connection)
    }
}
