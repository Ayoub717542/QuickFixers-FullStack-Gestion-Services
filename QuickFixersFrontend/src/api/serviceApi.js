import { axiosApi } from "./axiosApi";


export async function fetchServices
(pageNumber = 1,
 pageSize = 6,
 sortBy = "id",
 sortDir = "asc") {
    const { data } = await axiosApi.get("/service/listerServices", {
        params: { pageNumber, pageSize, sortBy, sortDir }
    })
    return data;
}
export async function fetchService(id) {
    const { data } = await axiosApi.get(`/service/consulterUnService/${id}`);
    return data;
}
export async function createService(dto) {
    const { data } = await axiosApi.post("/service/ajouterService", dto);
    return data;
}

export async function deleteService(id) {
    await axiosApi.delete(`/service/supprimerService/${id}`);
}
export async function deleteService(id) {
    await axiosApi.delete(`/service/supprimerService/${id}`);
}

export async function updateServiceStatut(id, statut) {
    const { data } = await axiosApi.patch(`/service/modefieStatut/${id}`, `"${statut}"`, {
        headers: { "Content-Type": "application/json" }
    });
    return data;
}
