package com.minhnbnt.features.auth.handler

import com.minhnbnt.features.auth.dto.AuthRequest
import com.minhnbnt.features.auth.dto.AuthResponse
import com.minhnbnt.features.auth.service.AuthService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/api/auth")
class AuthResource(private val authService: AuthService) {

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun register(request: AuthRequest): Response {
        authService.register(request.username, request.password)
        return Response.ok(mapOf("message" to "User registered successfully")).build()
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun login(request: AuthRequest): Response {
        val token = authService.login(request.username, request.password)
        return Response.ok(AuthResponse(token = token, username = request.username)).build()
    }
}
