package com.minhnbnt

import com.minhnbnt.service.ChatService
import com.minhnbnt.websockets.ChatMessage
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/api/chat")
class ChatResource(private val chatService: ChatService) {

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun sendMessage(message: ChatMessage): Response {
        chatService.onMessageReceived(message)
        return Response.accepted().build()
    }
}
