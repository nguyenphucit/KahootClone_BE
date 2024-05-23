package com.jee.quizapp.controller;

import com.jee.quizapp.model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    //send message to topic/lobby-chat with each gameId through url mapping /Sendchat/{gameId}
    @MessageMapping("/Sendchat/{gameId}")
    @SendTo("/topic/lobby-chat/{gameId}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String gameId){
        return chatMessage;
    }
}
