package com.minhnbnt.infrashtructure

import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.quarkus.websockets.next.WebSocketConnection
import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.*
import org.jboss.logging.Logger
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class ConnectionSubscriptionManager<Message : Any>(
    private val redisDS: ReactiveRedisDataSource,
    private val messageClass: KClass<Message>,
) {

    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val log = Logger.getLogger(ConnectionSubscriptionManager::class.java)
    private val websockets = ConcurrentHashMap<String, MutableSet<WebSocketConnection>>()
    private val channelJobs = ConcurrentHashMap<String, Job>()

    private fun redisChannel(channel: String) = "pubsub:$channel"

    private fun handleMessage(message: Message, channel: String) {

        val connections = websockets[channel]
        if (connections.isNullOrEmpty()) {
            return
        }

        scope.launch {

            val tasks = connections.map { connection ->
                async {
                    try {
                        connection.sendText(message).awaitSuspending()
                    } catch (e: Exception) {
                        log.warnf(e, "Failed to send to %s", connection.id())
                    }
                }
            }

            tasks.awaitAll()
        }
    }

    fun addToChannel(channel: String, connection: WebSocketConnection) {

        val connections = websockets.computeIfAbsent(channel) {
            ConcurrentHashMap.newKeySet()
        }

        connections.add(connection)

        channelJobs.computeIfAbsent(channel) {
            scope.launch {
                redisDS.pubsub(messageClass.java)
                    .subscribe(redisChannel(channel))
                    .asFlow()
                    .collect { message -> handleMessage(message, channel) }
            }
        }
    }

    fun removeFromChannel(channel: String, connection: WebSocketConnection) {

        websockets[channel]?.remove(connection)

        if (websockets[channel].isNullOrEmpty()) {
            websockets.remove(channel)
            channelJobs.remove(channel)?.cancel()
        }
    }

    suspend fun publishTo(channel: String, message: Message) {
        redisDS.pubsub(messageClass.java)
            .publish(redisChannel(channel), message)
            .awaitSuspending()
    }
}
