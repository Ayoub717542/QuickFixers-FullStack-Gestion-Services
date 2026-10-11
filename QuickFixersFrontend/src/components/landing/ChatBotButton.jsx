import { useState } from "react";
import { Bot, X, Send } from "lucide-react";

// Placeholder chat-bot launcher (bottom-left), to be wired to a real bot later.
function ChatBotButton() {
    const [open, setOpen] = useState(false);

    return (
        <>
            {open && (
                <div className="fixed bottom-36 right-6 z-50 flex w-[calc(100vw-2.5rem)] max-w-sm flex-col overflow-hidden rounded-2xl bg-white shadow-2xl ring-1 ring-black/5">
                    <div className="flex items-center justify-between bg-orange-500 p-4 text-white">
                        <div className="flex items-center gap-2">
                            <Bot size={20} />
                            <div>
                                <h2 className="text-sm font-bold">Assistant QuickFixers</h2>
                                <p className="text-xs text-white/80">Bientôt disponible</p>
                            </div>
                        </div>
                        <button
                            type="button"
                            onClick={() => setOpen(false)}
                            aria-label="Fermer le chat"
                        >
                            <X size={20} />
                        </button>
                    </div>

                    <div className="space-y-3 bg-gray-50 p-4">
                        <p className="rounded-2xl bg-white px-4 py-3 text-sm text-gray-600 shadow-sm">
                            Bonjour 👋 Le chatbot arrive bientôt. En attendant, créez un ticket
                            et notre équipe vous répondra rapidement.
                        </p>
                    </div>

                    <div className="flex gap-2 border-t p-3">
                        <input
                            type="text"
                            disabled
                            placeholder="Bientôt disponible..."
                            className="min-w-0 flex-1 rounded-full border bg-gray-50 px-4 py-2 text-sm text-gray-400 outline-none"
                        />
                        <button
                            type="button"
                            disabled
                            aria-label="Envoyer"
                            className="rounded-full bg-orange-500/50 p-2.5 text-white"
                        >
                            <Send size={18} />
                        </button>
                    </div>
                </div>
            )}

            <button
                type="button"
                onClick={() => setOpen((o) => !o)}
                aria-label={open ? "Fermer le chat" : "Ouvrir le chat"}
                className="fixed bottom-20 right-6 z-50 flex h-14 w-14 items-center justify-center rounded-full bg-orange-500 text-white shadow-lg transition-colors hover:bg-orange-600"
            >
                {open ? <X size={24} /> : <Bot size={26} />}
            </button>
        </>
    );
}

export default ChatBotButton;
