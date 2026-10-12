import { Client } from "@stomp/stompjs";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8081/api";

function buildBrokerUrl() {
    const url = new URL(API_URL);
    const protocol = url.protocol === "https:" ? "wss" : "ws";
    return `${protocol}://${url.host}/ws`;
}

export function ticketTopic(ticketId) {
    return `/topic/ticket.${ticketId}`;
}

export const CHAT_DESTINATION = "/app/chat";

// Private inbox of the logged-in user (used for the toast + red badge).
export const USER_QUEUE = "/user/queue/messages";

export function createStompClient({ onConnect, onDisconnect, onError }) {
    const token = localStorage.getItem("token");

    const client = new Client({
        brokerURL: buildBrokerUrl(),
        connectHeaders: { Authorization: `Bearer ${token}` },
        reconnectDelay: 5000,
        heartbeatIncoming: 10000,
        heartbeatOutgoing: 10000,
    });

    client.onConnect = () => onConnect?.();

    client.onDisconnect = () => onDisconnect?.();
    client.onWebSocketClose = () => onDisconnect?.();

    client.onStompError = (frame) =>
        onError?.(frame.headers["message"] || "The chat server refused the request.");
    client.onWebSocketError = () =>
        onError?.("Chat connection lost. Trying to reconnect...");

    return client;
}
