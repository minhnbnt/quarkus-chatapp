package com.minhnbnt.errors

import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

class ConflictException(message: String) : RuntimeException(message)

class ApiExceptionHandler {

    @ServerExceptionMapper
    fun conflict(e: ConflictException): RestResponse<Map<String, String>> =
        RestResponse.status(Response.Status.CONFLICT, mapOf("error" to (e.message ?: "Conflict")))

    @ServerExceptionMapper
    fun badRequest(e: IllegalArgumentException): RestResponse<Map<String, String>> =
        RestResponse.status(Response.Status.BAD_REQUEST, mapOf("error" to (e.message ?: "Bad request")))

    @ServerExceptionMapper
    fun general(e: Exception): RestResponse<Map<String, String>> =
        RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, mapOf("error" to "Internal server error"))
}
