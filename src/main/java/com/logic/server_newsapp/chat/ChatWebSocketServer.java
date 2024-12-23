package com.logic.server_newsapp.chat;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.log4j.Log4j2;

/**
 * WebSocket server for chat functionality.
 */
@Log4j2
@ServerEndpoint("/chat")
public class ChatWebSocketServer {

    /** Set of all active WebSocket sessions. */
    private static final Set<Session> SESSIONS =
            Collections.synchronizedSet(new HashSet<>());

    /** Map of user IDs to their WebSocket sessions. */
    private static final ConcurrentHashMap<String, Session> USER_SESSIONS =
            new ConcurrentHashMap<>();

    /**
     * Handles opening of new WebSocket connection.
     *
     * @param session The WebSocket session.
     */
    @OnOpen
    public void onOpen(final Session session) {
        SESSIONS.add(session);
        log.info("New connection opened: " + session.getId());
    }

    /**
     * Handles closing of WebSocket connection.
     *
     * @param session The WebSocket session.
     */
    @OnClose
    public void onClose(final Session session) {
        SESSIONS.remove(session);
        USER_SESSIONS.entrySet().removeIf(entry -> entry.getValue() == session);
        log.info("Connection closed: " + session.getId());
    }

    /**
     * Handles incoming messages over WebSocket.
     *
     * @param message The incoming message.
     * @param session The WebSocket session.
     */
    @OnMessage
    public void onMessage(final String message, final Session session) {
        if (message.startsWith("register:")) {
            String userId = message.substring(message.indexOf(":") + 1);
            registerUser(userId, session);
            log.info("User registered: " + userId
                    + ", session: " + session.getId());
            sendToUser(userId, "Вы успешно зарегистрировались");
        } else if (message.startsWith("message:")) {
            String recipientId =
                    message.substring(
                            message.indexOf(":") + 1, message.indexOf(
                                    ":", message.indexOf(":") + 1));
            String msg = message.substring(
                    message.indexOf(":", message.indexOf(":") + 1) + 1);
            log.info("Message for user: " + recipientId
                    + " from session: " + session.getId()
                    + ", message: " + msg);
            sendToUser(recipientId, msg);
        } else {
            log.info("Invalid message: " + message);
        }
    }

    /**
     * Handles WebSocket error.
     *
     * @param throwable The Throwable that occurred.
     */
    @OnError
    public void onError(final Throwable throwable) {
        log.info("Error: " + throwable.getMessage());
    }

    /**
     * Sends message to all connected sessions.
     *
     * @param message The message to send.
     * @throws IOException If an I/O error occurs.
     */
    public static void sendMessageToAll(final String message)
            throws IOException {
        for (Session session : SESSIONS) {
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * Sends message to all connected sessions.
     *
     * @param message The message to send.
     * @throws IOException If an I/O error occurs.
     */
    public static void sendGroupMessage(final String message)
            throws IOException {
        for (Session session : SESSIONS) {
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * Registers a user with their WebSocket session.
     *
     * @param userId The user ID.
     * @param session The WebSocket session.
     */
    public static void registerUser(final String userId,
                                    final Session session) {
        USER_SESSIONS.put(userId, session);
    }

    /**
     * Unregisters a user.
     *
     * @param userId The user ID.
     */
    public static void unregisterUser(final String userId) {
        USER_SESSIONS.remove(userId);
    }

    /**
     * Sends a message to a specific user.
     *
     * @param userId The user ID.
     * @param message The message to send.
     */
    public static void sendToUser(final String userId, final String message) {
        Session session = USER_SESSIONS.get(userId);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.info("Error send message to user "
                        + userId + ": " + e.getMessage());
            }
        } else {
            log.info("User with id: " + userId + " not found.");
        }
    }
}
