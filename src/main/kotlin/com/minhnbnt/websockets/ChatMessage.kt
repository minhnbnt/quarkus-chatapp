package com.minhnbnt.websockets

data class ChatMessage(
    val type: MessageType,
    val sender: String,
    val recipient: String?,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
)

enum class MessageType {
    CHAT,
    SYSTEM,
    JOINED,
    LEFT,
}
