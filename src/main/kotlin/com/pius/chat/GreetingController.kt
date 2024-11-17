package com.pius.chat

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller


@Controller
class GreetingController(
    private val template: SimpMessagingTemplate
) {
    private val logger = LoggerFactory.getLogger(javaClass)

//    @MessageMapping("/hello")
//    @SendToUser("/greetings")
//    fun greeting(message: HelloMessage): Greeting {
//        logger.info("Received message: ${message.name}")
//        return Greeting("Hello, ${message.name}")
//    }

//     send 어노테이션 없으면, /topic/hello 로 보내짐
//     send 어노테이션 있으면 해당 주소로 보내짐
//    @MessageMapping("/hello")
//    fun greeting(message: HelloMessage): Greeting {
//        logger.info("Received message: ${message.name}")
//        return Greeting("Hello, ${message.name}")
//    }

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    fun greeting(message: HelloMessage): Greeting {
//        logger.info("Received message: ${message.name}")
//        return Greeting("Hello, ${message.name}")
//    }

    @SubscribeMapping("/chat/{id}")
    fun subscribe(@DestinationVariable id: Long): Greeting {
        logger.info("Subscribed to chat room $id")
        return Greeting("Subscribed to chat room $id")
    }

    @MessageMapping("/chat/{id}")
    fun greeting(
        @DestinationVariable id: Long,
        message: HelloMessage
    ): Greeting {
        logger.info("Received message: ${message.name}")
        return Greeting("Hello, ${message.name}")
    }
}