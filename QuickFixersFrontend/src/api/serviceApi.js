import { axiosApi } from "./axiosApi";

export async function fetchServices(pageNumber = 1, pageSize = 6) {
    const response = await axiosApi.get(
        `/service/listerServices?pageNumber=${pageNumber}&pageSize=${pageSize}`
    );
    return response.data;
}

export async function fetchService(id) {
    const response = await axiosApi.get(`/service/consulterUnService/${id}`);
    return response.data;
}


export async function createService(formData) {
    const response = await axiosApi.post("/service/ajouterService", formData);
    return response.data;
}

export async function deleteService(id) {
    await axiosApi.delete(`/service/supprimerService/${id}`);
}

export async function updateServiceStatut(id, statut) {
    // On envoie le statut comme un texte JSON brut : "ACTIVE" ou "INACTIVE"
    const response = await axiosApi.patch(
        `/service/modefieStatut/${id}`,
        JSON.stringify(statut),
        { headers: { "Content-Type": "application/json" } }
    );
    return response.data;
}

export async function countActiveServices() {
    const response = await axiosApi.get("/service/countServices");
    return response.data;
}