package com.logic.server_newsapp.chat;

import jakarta.websocket.Session;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@ServerEndpoint("/chat")
public class ChatWebSocketServer {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static final ConcurrentHashMap<String, Session> userSessions = new ConcurrentHashMap<>(); // ID пользователя -> Session
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        log.info("New connection opened: " + session.getId());
    }
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        userSessions.entrySet().removeIf(entry -> entry.getValue() == session); //удаление из userSessions
        log.info("Connection closed: " + session.getId());
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        if (message.startsWith("register:")) {
            String userId = message.substring(message.indexOf(":") + 1);
            registerUser(userId, session);
            log.info("User registered: " + userId + ", session: " + session.getId());
            sendToUser(userId,"Вы успешно зарегистрировались");
        }else if (message.startsWith("message:")){
            String recipientId = message.substring(message.indexOf(":") + 1, message.indexOf(":",message.indexOf(":") + 1));
            String msg = message.substring(message.indexOf(":", message.indexOf(":") + 1) + 1);
            log.info("Message for user: " + recipientId + " from session: " + session.getId() + ", message: "+ msg);
            sendToUser(recipientId, msg);
        } else {
            log.info("Invalid message: " + message);
        }

    }
    @OnError
    public void onError(Throwable throwable) {
        log.info("Error: " + throwable.getMessage());
    }
    public static void sendMessageToAll(String message) throws IOException {
        for (Session session : sessions) {
            session.getBasicRemote().sendText(message);
        }
    }

    public static void sendGroupMessage(String message) throws IOException{
        for (Session session : sessions) {
            session.getBasicRemote().sendText(message);
        }
    }

    public static void registerUser(String userId, Session session){
        userSessions.put(userId, session);
    }
    public static void unregisterUser(String userId){
        userSessions.remove(userId);
    }
    public static void sendToUser(String userId, String message){
        Session session = userSessions.get(userId);
        if (session != null){
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.info("Error send message to user " + userId + ": " + e.getMessage());
            }
        } else{
            log.info("User with id: " + userId + " not found.");
        }
    }
}