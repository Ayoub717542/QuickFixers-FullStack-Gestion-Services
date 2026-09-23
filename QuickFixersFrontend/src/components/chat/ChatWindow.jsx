import { useState } from "react";
import { MessageCircle, Send, X } from "lucide-react";

function ChatWindow({ ticketId }) {
    const [isOpen, setIsOpen] = useState(false);
    const [message, setMessage] = useState("");
    const [messages, setMessages] = useState([]);

    function handleSend() {
        const text = message.trim();

        if (text === "") return;

        const newMessage = {
            id: Date.now(),
            text: text,
            time: new Date().toLocaleTimeString("fr-FR", {
                hour: "2-digit",
                minute: "2-digit"
            })
        };

        setMessages([...messages, newMessage]);
        setMessage("");
    }

    return (
        <>
            {!isOpen && (
                <button
                    type="button"
                    onClick={() => setIsOpen(true)}
                    className="fixed bottom-5 right-5 z-50 flex items-center gap-2 rounded-full bg-orange-500 px-4 py-3 text-white shadow-lg hover:bg-orange-600"
                >
                    <MessageCircle size={22} />
                    <span>Chat</span>
                </button>
            )}

            {isOpen && (
                <div className="fixed bottom-5 right-5 z-50 flex h-[450px] w-[calc(100vw-2.5rem)] max-w-sm flex-col overflow-hidden rounded-2xl bg-white shadow-2xl">
                    <div className="flex items-center justify-between bg-orange-500 p-4 text-white">
                        <div>
                            <h2 className="font-bold">Conversation</h2>
                            <p className="text-xs">Ticket #{ticketId}</p>
                        </div>

                        <button
                            type="button"
                            onClick={() => setIsOpen(false)}
                            aria-label="Fermer le chat"
                        >
                            <X size={22} />
                        </button>
                    </div>

                    <div className="flex-1 space-y-3 overflow-y-auto bg-gray-50 p-4">
                        {messages.length === 0 && (
                            <p className="text-center text-sm text-gray-500">
                                Aucun message pour le moment.
                            </p>
                        )}

                        {messages.map((item) => (
                            <div key={item.id} className="flex justify-end">
                                <div className="max-w-[80%]">
                                    <p className="rounded-2xl bg-orange-500 px-4 py-2 text-sm text-white">
                                        {item.text}
                                    </p>
                                    <p className="mt-1 text-right text-xs text-gray-400">
                                        Vous · {item.time}
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="flex gap-2 border-t p-3">
                        <input
                            type="text"
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") handleSend();
                            }}
                            placeholder="Écrire un message..."
                            className="min-w-0 flex-1 rounded-full border px-4 py-2 text-sm outline-none focus:border-orange-500"
                        />

                        <button
                            type="button"
                            onClick={handleSend}
                            disabled={!message.trim()}
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