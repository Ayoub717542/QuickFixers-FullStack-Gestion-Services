import { axiosApi } from "./axiosApi";


export async function fetchPayments(pageNumber = 1, pageSize = 5) {
    const response = await axiosApi.get(
        `/paiements/paimentHistorique?pageNumber=${pageNumber}&pageSize=${pageSize}`
    );
    return response.data;
}

export async function countPayments() {
    const response = await axiosApi.get("/paiements/paiements");
    return response.data;
}


export async function createPayment(ticketId, montant) {
    const response = await axiosApi.post("/paiements/effectuerPaiement", {
        ticketId,
        montant
    });
    return response.data;
}