import { axiosApi } from "./axiosApi";

// Récupère l'historique des paiements (paginé).
// USER  → ses propres paiements
// ADMIN → tous les paiements
export async function fetchPayments(pageNumber = 1, pageSize = 5) {
    const response = await axiosApi.get(
        `/paiements/paimentHistorique?pageNumber=${pageNumber}&pageSize=${pageSize}`
    );
    return response.data;
}

// Nombre de paiements (USER → les siens, ADMIN → total)
export async function countPayments() {
    const response = await axiosApi.get("/paiements/paiements");
    return response.data;
}

// Effectue le paiement d'un ticket (USER uniquement).
// Le backend marque le paiement TERMINE et ferme le ticket.
export async function createPayment(ticketId, montant) {
    const response = await axiosApi.post("/paiements/effectuerPaiement", {
        ticketId,
        montant
    });
    return response.data;
}