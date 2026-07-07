package com.minhnbnt.features.chat.service

import com.minhnbnt.domain.ChatMessage
import com.minhnbnt.domain.MessageType
import com.minhnbnt.infrastructure.ConnectionSubscriptionManager
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.quarkus.websockets.next.WebSocketConnection
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ChatService(redisDS: ReactiveRedisDataSource) {

    private val connectionManager = ConnectionSubscriptionManager(redisDS, ChatMessage::class)

    private fun userChannel(userId: String) = "chat:user:$userId"

    suspend fun onUserConnected(userId: String, connection: WebSocketConnection) {

        connectionManager.addToChannel(userChannel(userId), connection)

        val message = ChatMessage(
            type = MessageType.JOINED,
            sender = userId,
            recipient = userId,
            content = "Joined chat room",
        )

        connection.sendText(message).awaitSuspending()
    }

    suspend fun onMessageReceived(message: ChatMessage) {

        val recipient = message.recipient

        if (!recipient.isNullOrBlank()){
            connectionManager.publishTo(userChannel(recipient), message)
        }
    }

    suspend fun onUserDisconnected(userId: String, connection: WebSocketConnection) {
        connectionManager.removeFromChannel(userChannel(userId), connection)
    }
}
