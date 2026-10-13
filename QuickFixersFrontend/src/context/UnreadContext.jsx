import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { fetchUnreadMessages, markTicketRead } from "../api/messageApi";

const UnreadContext = createContext(null);

// Very small store shared by the navbar and the chat button.
// - unread:        { ticketId: numberOfUnreadMessages }
// - notifications: newest first, used by the bell dropdown
export function UnreadProvider({ children }) {
    const [unread, setUnread] = useState({});
    const [notifications, setNotifications] = useState([]);

    // Recharge les messages non lus depuis le backend.
    // C'est ce qui fait apparaitre la cloche apres une connexion (messages recus hors ligne).
    const refresh = useCallback(async () => {
        try {
            const messages = await fetchUnreadMessages();

            const nextUnread = {};
            const nextNotifications = [];

            for (const message of messages) {
                nextUnread[message.ticketId] = (nextUnread[message.ticketId] || 0) + 1;
                nextNotifications.push({
                    id: `${message.id}`,
                    ticketId: message.ticketId,
                    senderNom: message.senderNom || "un utilisateur",
                });
            }

            setUnread(nextUnread);
            setNotifications(nextNotifications.slice(0, 20));
        } catch {
            // Pas grave : la cloche reste simplement vide.
        }
    }, []);

    useEffect(() => {
        refresh();
    }, [refresh]);

    // A new message arrived for this ticket.
    const addNotification = useCallback((notification) => {
        setUnread((current) => ({
            ...current,
            [notification.ticketId]: (current[notification.ticketId] || 0) + 1,
        }));
        setNotifications((current) => [notification, ...current].slice(0, 20));
    }, []);

    // The user opened this ticket, so it is no longer "unread".
    const clearTicket = useCallback((ticketId) => {
        setUnread((current) => {
            if (!current[ticketId]) return current;
            const next = { ...current };
            delete next[ticketId];
            return next;
        });
        setNotifications((current) => current.filter((item) => item.ticketId !== ticketId));

        // Et on le marque comme lu cote serveur, pour qu'il ne revienne pas apres un refresh.
        markTicketRead(ticketId).catch(() => {});
    }, []);

    const clearAll = useCallback(() => {
        setUnread({});
        setNotifications([]);
    }, []);

    const value = useMemo(() => {
        const total = Object.values(unread).reduce((sum, count) => sum + count, 0);
        return { unread, total, notifications, addNotification, clearTicket, clearAll, refresh };
    }, [unread, notifications, addNotification, clearTicket, clearAll, refresh]);

    return <UnreadContext.Provider value={value}>{children}</UnreadContext.Provider>;
}

export function useUnread() {
    return useContext(UnreadContext);
}
