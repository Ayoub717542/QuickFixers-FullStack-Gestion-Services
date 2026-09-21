import { axiosApi } from "./axiosApi";

export async function fetchAssignedTickets(pageNumber, pageSize, sortBy, sortDir) {
    const response = await axiosApi.get("/ticket/tickets", {
        params: { pageNumber, pageSize, sortBy, sortDir }
    });
    return response.data;
}

export async function fetchTicket(id) {
    const response = await axiosApi.get(`/ticket/${id}`);
    return response.data;
}

export async function updateTicketStatut(ticketId, statut) {
    const response = await axiosApi.patch(`/ticket/statut/${ticketId}/${statut}`);
    return response.data;
}

export async function createTicket(serviceId, ticketData) {
    const response = await axiosApi.post(`/ticket/${serviceId}/tickets`, ticketData);
    return response.data;
}