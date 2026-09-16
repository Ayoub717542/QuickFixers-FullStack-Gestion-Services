import axios from "axios";
import { toast } from "react-toastify";

export const api = axios.create({
    baseURL: "http://localhost:8081/api",
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        toast.error("Request error");
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        console.log(
            "Response:",
            response.status,
            response.config.url,
            response.data
        );

        return response;
    },
    (error) => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    console.log("401 Unauthorized:", error.config?.url);

                    localStorage.removeItem("token");

                    if (!window.location.pathname.includes("/login")) {
                        toast.error("Please login again.");
                        window.location.href = "/login";
                    }
                    break;

                case 403:
                    console.log("403 Forbidden:", error.config?.url);
                    toast.error("You don't have permission for this action.");
                    break;

                case 404:
                    console.log("404 Not Found:", error.config?.url);
                    toast.error("Resource not found.");
                    break;

                case 500:
                    console.log("500 Server Error");
                    toast.error("Server error.");
                    break;

                default:
                    console.log(
                        "HTTP Error:",
                        error.response.status
                    );
            }
        } else if (error.request) {
            console.log("No response received from the server.");
            toast.error("Cannot connect to the server.");
        } else {
            console.log("Request error:", error.message);
        }

        return Promise.reject(error);
    }
);

export default api;