import { useEffect, useRef, useState } from "react";
import { MessageCircle, Send, X } from "lucide-react";
import { toast } from "react-toastify";
import useTicketChat from "../../hooks/useTicketChat";
import { useUnread } from "../../context/UnreadContext";
import getUserRole from "../../utils/auth";

function formatTime(dateString) {
    const date = new Date(dateString);
    return isNaN(date.getTime())
        ? ""
        : date.toLocaleTimeString("fr-FR", { hour: "2-digit", minute: "2-digit" });
}

function ChatWindow({ ticketId }) {
    const [isOpen, setIsOpen] = useState(false);
    const [draft, setDraft] = useState("");
    const bottomRef = useRef(null);

    const role = getUserRole();
    const canChat = role === "CLIENT" || role === "SUPPORT";
    const myEmail = localStorage.getItem("userEmail");

    const { unread, clearTicket } = useUnread();
    const ticketUnread = unread[ticketId] || 0;

    const { messages, connected, loading, error, sendMessage } = useTicketChat(ticketId, {
        enabled: isOpen && canChat,
    });

    useEffect(() => {
        if (error) toast.error(error);
    }, [error]);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages, isOpen]);

    useEffect(() => {
        if (isOpen) clearTicket(ticketId);
    }, [isOpen, ticketId, clearTicket]);

    if (!canChat) return null;

    function handleSend() {
        if (sendMessage(draft)) {
            setDraft("");
        }
    }

    return (
        <>
            {!isOpen && (
                <button
                    type="button"
                    onClick={() => setIsOpen(true)}
                    className="fixed bottom-5 right-5 z-50 flex items-center gap-2 rounded-full bg-orange-500 px-4 py-3 text-white shadow-lg hover:bg-orange-600"
                >
                    <span className="relative">
                        <MessageCircle size={22} />
                        {ticketUnread > 0 && (
                            <span className="absolute -right-2 -top-2 flex h-4 min-w-4 items-center justify-center rounded-full bg-red-500 px-1 text-[10px] font-bold leading-none text-white">
                                {ticketUnread}
                            </span>
                        )}
                    </span>
                    <span>Chat</span>
                </button>
            )}

            {isOpen && (
                <div className="fixed inset-0 z-50 flex h-[100dvh] flex-col overflow-hidden bg-white sm:inset-auto sm:bottom-5 sm:right-5 sm:h-[450px] sm:w-[380px] sm:rounded-2xl sm:shadow-2xl">
                    <div className="flex items-center justify-between bg-orange-500 px-4 pb-4 pt-[calc(1rem_+_env(safe-area-inset-top))] text-white sm:pt-4">
                        <div>
                            <h2 className="font-bold">Conversation</h2>
                            <p className="flex items-center gap-1 text-xs">
                                <span
                                    className={`inline-block h-2 w-2 rounded-full ${
                                        connected ? "bg-green-300" : "bg-gray-300"
                                    }`}
                                />
                                Ticket #{ticketId}
                            </p>
                        </div>

                        <button type="button" onClick={() => setIsOpen(false)} aria-label="Fermer le chat">
                            <X size={22} />
                        </button>
                    </div>

                    <div className="flex-1 space-y-3 overflow-y-auto bg-gray-50 p-4">
                        {loading && messages.length === 0 && (
                            <p className="text-center text-sm text-gray-500">Chargement...</p>
                        )}

                        {!loading && messages.length === 0 && (
                            <p className="text-center text-sm text-gray-500">
                                Aucun message pour le moment.
                            </p>
                        )}

                        {messages.map((item) => {
                            const mine = item.senderEmail === myEmail;

                            return (
                                <div key={item.id} className={`flex ${mine ? "justify-end" : "justify-start"}`}>
                                    <div className="max-w-[80%]">
                                        <p
                                            className={`rounded-2xl px-4 py-2 text-sm ${
                                                mine ? "bg-orange-500 text-white" : "bg-white text-gray-800 shadow"
                                            }`}
                                        >
                                            {item.contenu}
                                        </p>
                                        <p className={`mt-1 text-xs text-gray-400 ${mine ? "text-right" : "text-left"}`}>
                                            {mine ? "Vous" : item.senderNom || "Support"} · {formatTime(item.dateEnvoi)}
                                        </p>
                                    </div>
                                </div>
                            );
                        })}

                        <div ref={bottomRef} />
                    </div>

                    <div className="flex gap-2 border-t p-3 pb-[calc(0.75rem_+_env(safe-area-inset-bottom))] sm:pb-3">
                        <input
                            type="text"
                            value={draft}
                            onChange={(e) => setDraft(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") handleSend();
                            }}
                            placeholder="Écrire un message..."
                            className="min-w-0 flex-1 rounded-full border px-4 py-2 text-sm outline-none focus:border-orange-500"
                        />

                        <button
                            type="button"
                            onClick={handleSend}
                            disabled={!draft.trim()}
                            className="rounded-full bg-orange-500 p-2.5 text-white hover:bg-orange-600 disabled:opacity-50"
                            aria-label="Envoyer"
                        >
                            <Send size={18} />
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}

export default ChatWindow;
