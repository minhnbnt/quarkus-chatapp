package com.minhnbnt.features.auth.service

import com.minhnbnt.domain.User
import com.minhnbnt.errors.ConflictException
import com.minhnbnt.features.auth.repository.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import java.time.Duration

@ApplicationScoped
class AuthService(private val userRepository: UserRepository) {

    suspend fun register(username: String, password: String) {

        val existing = userRepository.findByUsername(username)
        if (existing != null) {
            throw ConflictException("Username already taken")
        }

        val user = User().apply {
            this.username = username
            this.password = BcryptUtil.bcryptHash(password)
        }

        userRepository.persist(user).awaitSuspending()
    }

    suspend fun login(username: String, password: String): String {

        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("Invalid username or password")

        if (!BcryptUtil.matches(password, user.password)) {
            throw IllegalArgumentException("Invalid username or password")
        }

        return Jwt
            .upn(username)
            .issuer("chatapp")
            .subject(username)
            .expiresIn(Duration.ofHours(24))
            .sign()
    }
}
