import { axiosApi } from "./axiosApi";

export async function fetchTicketMessages(ticketId) {
    const response = await axiosApi.get(`/messages/ticket/${ticketId}`);
    return response.data;
}
