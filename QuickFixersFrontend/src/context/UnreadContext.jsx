import { createContext, useCallback, useContext, useMemo, useState } from "react";

const UnreadContext = createContext(null);

// Very small store shared by the navbar and the chat button.
// - unread:        { ticketId: numberOfUnreadMessages }
// - notifications: newest first, used by the bell dropdown
export function UnreadProvider({ children }) {
    const [unread, setUnread] = useState({});
    const [notifications, setNotifications] = useState([]);

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
    }, []);

    const clearAll = useCallback(() => {
        setUnread({});
        setNotifications([]);
    }, []);

    const value = useMemo(() => {
        const total = Object.values(unread).reduce((sum, count) => sum + count, 0);
        return { unread, total, notifications, addNotification, clearTicket, clearAll };
    }, [unread, notifications, addNotification, clearTicket, clearAll]);

    return <UnreadContext.Provider value={value}>{children}</UnreadContext.Provider>;
}

export function useUnread() {
    return useContext(UnreadContext);
}
