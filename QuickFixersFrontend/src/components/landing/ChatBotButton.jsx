import { useEffect, useRef, useState } from "react";
import { Bot, X, Send } from "lucide-react";
import { sendChatMessage } from "../../api/chatbotApi";

const GREETING =
    "Bonjour 👋 Je suis l'assistant QuickFixers. Posez-moi une question sur nos services, la création d'un ticket ou le paiement.";

const SUGGESTIONS = [
    "Comment créer un ticket ?",
    "Quels services proposez-vous ?",
    "Comment fonctionne le paiement ?"
];

function now() {
    return new Date().toLocaleTimeString("fr-FR", {
        hour: "2-digit",
        minute: "2-digit"
    });
}

// Bulle de message (assistant à gauche, visiteur à droite).
function Message({ message }) {
    const isUser = message.role === "user";

    return (
        <div className={`flex items-end gap-2 ${isUser ? "flex-row-reverse" : "flex-row"}`}>
            {!isUser && (
                <div className="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-orange-500 text-white">
                    <Bot size={15} />
                </div>
            )}

            <div
                className={`max-w-[85%] rounded-2xl px-3 py-2 text-[13px] shadow-sm sm:max-w-[80%] sm:px-3.5 sm:text-sm ${
                    isUser
                        ? "rounded-br-md bg-orange-500 text-white"
                        : message.error
                            ? "rounded-bl-md bg-red-50 text-red-600 ring-1 ring-red-100"
                            : "rounded-bl-md bg-white text-gray-700 ring-1 ring-gray-100"
                }`}
            >
                <p className="whitespace-pre-wrap break-words leading-relaxed">{message.text}</p>
                <p
                    className={`mt-1 text-right text-[10px] ${
                        isUser ? "text-white/70" : "text-gray-400"
                    }`}
                >
                    {message.time}
                </p>
            </div>
        </div>
    );
}

// Indicateur de saisie « à la WhatsApp » : trois points qui rebondissent.
function TypingBubble() {
    return (
        <div className="flex items-end gap-2">
            <div className="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-orange-500 text-white">
                <Bot size={15} />
            </div>
            <div className="flex items-center gap-1 rounded-2xl rounded-bl-md bg-white px-4 py-3 ring-1 ring-gray-100">
                {[0, 1, 2].map((i) => (
                    <span
                        key={i}
                        className="h-2 w-2 animate-bounce rounded-full bg-gray-400"
                        style={{ animationDelay: `${i * 150}ms` }}
                    />
                ))}
            </div>
        </div>
    );
}

function ChatBotButton() {
    const [open, setOpen] = useState(false);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");
    const [sending, setSending] = useState(false);

    const seededRef = useRef(false);
    const scrollRef = useRef(null);
    const inputRef = useRef(null);

    // Message d'accueil au premier affichage, puis focus sur le champ.
    useEffect(() => {
        if (!open) return;

        if (!seededRef.current) {
            seededRef.current = true;
            setMessages([{ id: "greeting", role: "bot", text: GREETING, time: now() }]);
        }
        const id = setTimeout(() => inputRef.current?.focus(), 60);
        return () => clearTimeout(id);
    }, [open]);

    // Défilement automatique vers le dernier message.
    useEffect(() => {
        scrollRef.current?.scrollIntoView({ behavior: "smooth", block: "end" });
    }, [messages, sending, open]);

    async function handleSend(text) {
        const content = (text ?? input).trim();
        if (content === "" || sending) return;

        setMessages((prev) => [
            ...prev,
            { id: Date.now(), role: "user", text: content, time: now() }
        ]);
        setInput("");
        setSending(true);

        try {
            const reply = await sendChatMessage(content);
            setMessages((prev) => [
                ...prev,
                {
                    id: Date.now() + 1,
                    role: "bot",
                    text: reply || "Je n'ai pas de réponse pour le moment. Réessayez.",
                    time: now()
                }
            ]);
        } catch (err) {
            setMessages((prev) => [
                ...prev,
                {
                    id: Date.now() + 2,
                    role: "bot",
                    error: true,
                    text: "Oups, une erreur est survenue en contactant l'assistant. Réessayez dans un instant.",
                    time: now()
                }
            ]);
        } finally {
            setSending(false);
        }
    }

    function handleSubmit(e) {
        e.preventDefault();
        handleSend();
    }

    function handleKeyDown(e) {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            handleSend();
        }
    }

    return (
        <>
            {open && (
                <div className="fixed bottom-36 right-3 z-50 flex h-[min(70dvh,540px)] max-h-[calc(100dvh-11rem)] w-[calc(100vw-1.5rem)] max-w-sm flex-col overflow-hidden rounded-2xl bg-white shadow-2xl ring-1 ring-black/5 sm:right-6 sm:h-[560px] sm:w-[380px]">
                    {/* En-tête */}
                    <div className="flex items-center justify-between gap-2 bg-gradient-to-r from-orange-500 to-yellow-500 px-3 py-2.5 text-white sm:px-4 sm:py-3">
                        <div className="flex items-center gap-2.5">
                            <div className="relative flex h-9 w-9 items-center justify-center rounded-full bg-white/20 sm:h-10 sm:w-10">
                                <Bot size={20} />
                                <span className="absolute -bottom-0.5 -right-0.5 h-3 w-3 rounded-full border-2 border-orange-500 bg-emerald-400" />
                            </div>
                            <div>
                                <h2 className="text-sm font-bold leading-tight">
                                    Assistant QuickFixers
                                </h2>
                                <p className="text-[11px] text-white/85">
                                    {sending
                                        ? "est en train d'écrire…"
                                        : "En ligne · réponse instantanée"}
                                </p>
                            </div>
                        </div>
                        <button
                            type="button"
                            onClick={() => setOpen(false)}
                            aria-label="Fermer le chat"
                            className="rounded-full p-1.5 transition-colors hover:bg-white/15"
                        >
                            <X size={18} />
                        </button>
                    </div>

                    {/* Messages */}
                    <div
                        role="log"
                        aria-live="polite"
                        className="flex min-h-0 flex-1 flex-col gap-2.5 overflow-y-auto bg-gray-50 p-3 sm:gap-3 sm:p-4"
                    >
                        {messages.map((message) => (
                            <Message key={message.id} message={message} />
                        ))}

                        {sending && <TypingBubble />}

                        <div ref={scrollRef} />
                    </div>

                    {/* Suggestions de départ */}
                    {messages.length <= 1 && !sending && (
                        <div className="flex gap-2 overflow-x-auto border-t border-gray-100 bg-white px-3 py-2 [scrollbar-width:none] sm:px-4 [&::-webkit-scrollbar]:hidden">
                            {SUGGESTIONS.map((s) => (
                                <button
                                    key={s}
                                    type="button"
                                    onClick={() => handleSend(s)}
                                    className="shrink-0 whitespace-nowrap rounded-full border border-orange-200 bg-orange-50 px-3 py-1 text-[11px] font-medium text-orange-600 transition-colors hover:bg-orange-100 sm:text-xs"
                                >
                                    {s}
                                </button>
                            ))}
                        </div>
                    )}

                    {/* Saisie */}
                    <form
                        onSubmit={handleSubmit}
                        className="flex items-center gap-2 border-t border-gray-100 bg-white p-2.5 sm:p-3"
                    >
                        <input
                            ref={inputRef}
                            type="text"
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                            onKeyDown={handleKeyDown}
                            disabled={sending}
                            maxLength={1000}
                            placeholder="Écrivez votre message…"
                            aria-label="Votre message"
                            className="min-w-0 flex-1 rounded-full border border-gray-200 bg-gray-50 px-3.5 py-2 text-sm text-gray-800 outline-none transition-colors focus:border-orange-400 focus:bg-white focus:ring-2 focus:ring-orange-400/30 disabled:opacity-60 sm:px-4 sm:py-2.5"
                        />
                        <button
                            type="submit"
                            disabled={sending || input.trim() === ""}
                            aria-label="Envoyer"
                            className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-orange-500 text-white transition-colors hover:bg-orange-600 disabled:cursor-not-allowed disabled:opacity-50 sm:h-11 sm:w-11"
                        >
                            <Send size={18} />
                        </button>
                    </form>
                </div>
            )}

            {/* Bouton flottant */}
            <button
                type="button"
                onClick={() => setOpen((o) => !o)}
                aria-label={open ? "Fermer le chat" : "Ouvrir le chat"}
                className="fixed bottom-20 right-6 z-50 flex h-14 w-14 items-center justify-center rounded-full bg-gradient-to-br from-orange-500 to-yellow-500 text-white shadow-lg shadow-orange-500/30 transition-transform hover:scale-105"
            >
                {open ? <X size={24} /> : <Bot size={26} />}

                {!open && (
                    <span className="absolute -right-0.5 -top-0.5 flex h-3.5 w-3.5">
                        <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-emerald-400 opacity-75" />
                        <span className="relative inline-flex h-3.5 w-3.5 rounded-full border-2 border-white bg-emerald-500" />
                    </span>
                )}
            </button>
        </>
    );
}

export default ChatBotButton;
