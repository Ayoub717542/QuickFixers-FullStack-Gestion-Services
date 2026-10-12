import { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { createStompClient, USER_QUEUE } from "../../api/websocket";
import { useUnread } from "../../context/UnreadContext";
import getUserRole from "../../utils/auth";

function ChatNotifications() {
    const navigate = useNavigate();
    const { addNotification, clearTicket } = useUnread();
    const subscriptionRef = useRef(null);

    useEffect(() => {
        const role = getUserRole();
        if (role !== "CLIENT" && role !== "SUPPORT") return undefined;

        const basePath = role === "SUPPORT" ? "/support/tickets" : "/user/tickets";

        function openTicket(ticketId) {
            clearTicket(ticketId);
            navigate(`${basePath}/${ticketId}`);
        }

        function handleMessage(message) {
            const notification = {
                id: `${Date.now()}-${Math.random()}`,
                ticketId: message.ticketId,
                senderNom: message.senderNom || "un utilisateur",
            };

            addNotification(notification);
            toast.info(
                <span className="cursor-pointer">
                    Nouveau message de {notification.senderNom} (Ticket #{notification.ticketId})
                </span>,
                { onClick: () => openTicket(notification.ticketId) }
            );
        }

        const client = createStompClient({
            onConnect: () => {
                subscriptionRef.current = client.subscribe(USER_QUEUE, (frame) => {
                    try {
                        handleMessage(JSON.parse(frame.body));
                    } catch {
                    }
                });
            },
        });

        client.activate();

        return () => {
            subscriptionRef.current?.unsubscribe();
            subscriptionRef.current = null;
            client.deactivate();
        };
    }, [addNotification, clearTicket, navigate]);

    return null;
}

export default ChatNotifications;
