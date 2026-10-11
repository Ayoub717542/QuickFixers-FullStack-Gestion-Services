import { axiosApi } from "./axiosApi";

/**
 * Envoie un message à l'assistant QuickFixers et renvoie sa réponse texte.
 * Endpoint public : POST /api/chatbot  { message } -> { reply }
 */
export async function sendChatMessage(message) {
    const response = await axiosApi.post("/chatbot", { message });
    return response.data?.reply ?? "";
}
