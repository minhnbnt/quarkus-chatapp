package com.minhnbnt.features.auth.repository

import com.minhnbnt.domain.User
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepository : PanacheRepository<User> {

    suspend fun findByUsername(username: String): User? =
        find("username", username)
            .firstResult<User>()
            .awaitSuspending()
}
