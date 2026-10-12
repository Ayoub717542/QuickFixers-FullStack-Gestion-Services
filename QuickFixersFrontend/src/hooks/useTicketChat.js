import { useCallback, useEffect, useRef, useState } from "react";
import { fetchTicketMessages } from "../api/messageApi";
import { CHAT_DESTINATION, createStompClient, ticketTopic } from "../api/websocket";
import { toast } from "react-toastify";

function mergeMessages(current, incoming) {
    const byId = new Map();
    for (const message of current) byId.set(message.id, message);
    for (const message of incoming) {
        if (!byId.has(message.id)) byId.set(message.id, message);
    }
    return [...byId.values()].sort((a, b) => a.id - b.id);
}

export default function useTicketChat(ticketId, { enabled = true } = {}) {
    const [messages, setMessages] = useState([]);
    const [connected, setConnected] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const clientRef = useRef(null);
    const subscriptionRef = useRef(null);

    useEffect(() => {
        if (!enabled || ticketId == null) return undefined;

        let cancelled = false;
        setMessages([]);
        setConnected(false);
        setError(null);
        setLoading(true);

        fetchTicketMessages(ticketId)
            .then((history) => {
                if (cancelled) return;
                setMessages((previous) => mergeMessages(previous, history));
            })
            .catch(() => {
                if (!cancelled) setError("Could not load the conversation.");
            })
            .finally(() => {
                if (!cancelled) setLoading(false);
            });

        const client = createStompClient({
            onConnect: () => {
                if (cancelled) return;
                subscriptionRef.current = null;
                setConnected(true);
                subscribe();
            },
            onDisconnect: () => {
                if (!cancelled) setConnected(false);
            },
            onError: (message) => {
                if (!cancelled) setError(message);
            },
        });

        function subscribe() {
            if (!client.connected || subscriptionRef.current) return;

            subscriptionRef.current = client.subscribe(ticketTopic(ticketId), (frame) => {
                const incoming = JSON.parse(frame.body);
                setMessages((previous) => mergeMessages(previous, [incoming]));
            });
        }

        clientRef.current = client;
        client.activate();

        return () => {
            cancelled = true;
            subscriptionRef.current?.unsubscribe();
            subscriptionRef.current = null;
            client.deactivate();
            clientRef.current = null;
        };
    }, [enabled, ticketId]);

    const sendMessage = useCallback(
        (text) => {
            const content = text.trim();
            if (content === "") return false;

            const client = clientRef.current;
            if (!client || !client.connected) {
                setError("The chat is not connected yet. Please wait a moment.");
                return false;
            }

            client.publish({
                destination: CHAT_DESTINATION,
                body: JSON.stringify({ ticketId, contenu: content }),
            });

            return true;
        },
        [ticketId]
    );

    return { messages, connected, loading, error, sendMessage };

    const incoming = JSON.parse(frame.body);

    setMessages((previous) => mergeMessages(previous, [incoming]));

    const myEmail = localStorage.getItem("userEmail");

    if (incoming.senderEmail !== myEmail) {
        toast.info("Vous avez reçu un nouveau message.");
    }
}
