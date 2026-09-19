import { axiosApi } from "./axiosApi";

export async function fetchMyProfile() {
    const response = await axiosApi.get("/users/me");
    return response.data;
}

export async function updateMyProfile(userData) {
    const response = await axiosApi.put("/users/me", userData);
    return response.data;
}