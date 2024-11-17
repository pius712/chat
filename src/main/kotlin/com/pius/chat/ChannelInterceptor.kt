package com.pius.chat

import com.pius.chat.jwt.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor

class ChannelInterceptor(
    private val jwtUtil: JwtUtil
) : ChannelInterceptor {

    val logger = LoggerFactory.getLogger(javaClass)
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = StompHeaderAccessor.wrap(message)

        if (accessor.messageType == SimpMessageType.CONNECT) {
            val authHeader = accessor.getFirstNativeHeader("Authorization") ?: return message
            val userId = getUserId(authHeader)
        }
        if (accessor.messageType == SimpMessageType.SUBSCRIBE) {

            logger.info("SUBSCRIBED TO: ${accessor.destination}")
        }

        return message
    }

    private fun getUserId(authHeader: String): Long {
        if (authHeader.startsWith("Bearer ")) {
            val token = authHeader.replace("Bearer ", "")
            return jwtUtil.getUserId(token)
        }

        throw RuntimeException("invalid token")
    }
}