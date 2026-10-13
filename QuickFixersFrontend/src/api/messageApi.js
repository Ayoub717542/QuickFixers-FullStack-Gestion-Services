import { axiosApi } from "./axiosApi";

export async function fetchTicketMessages(ticketId) {
    const response = await axiosApi.get(`/messages/ticket/${ticketId}`);
    return response.data;
}

// Messages recus et pas encore lus (utilise par la cloche a la connexion).
export async function fetchUnreadMessages() {
    const response = await axiosApi.get("/messages/unread");
    return response.data;
}

// Marque les messages recus sur ce ticket comme lus.
export async function markTicketRead(ticketId) {
    await axiosApi.patch(`/messages/ticket/${ticketId}/read`);
}
