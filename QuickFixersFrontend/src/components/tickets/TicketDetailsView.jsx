import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import Loader from "../../components/Loader";
import { fetchTicket, updateTicketStatut } from "../../api/ticketApi";
import { createPayment } from "../../api/paiementApi";
import ChatWindow from "../chat/ChatWindow";

const STATUTS = ["OUVERT", "EN_COURS", "RESOLU", "FERME"];

const statutColors = {
    OUVERT: "bg-blue-100 text-blue-600",
    EN_COURS: "bg-orange-100 text-orange-600",
    RESOLU: "bg-green-100 text-green-600",
    FERME: "bg-gray-100 text-gray-600"
};

function formatDate(dateStr) {
    if (!dateStr) return "—";
    const date = new Date(dateStr);
    if (isNaN(date.getTime())) return dateStr;
    return date.toLocaleDateString("fr-FR", {
        day: "2-digit",
        month: "long",
        year: "numeric"
    });
}

function TicketDetailsView({ backPath = "/support/tickets", canManage = true }) {
    const { id } = useParams();
    const navigate = useNavigate();

    const [ticket, setTicket] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        loadTicket();
    }, [id]);

    async function loadTicket() {
        setLoading(true);
        setError("");
        try {
            const data = await fetchTicket(id);
            setTicket(data);
        } catch (err) {
            setError("Impossible de charger le ticket.");
        } finally {
            setLoading(false);
        }
    }

    async function changeStatut(statut) {
        if (!statut || statut === ticket.statut) return;

        setSaving(true);
        try {
            const updated = await updateTicketStatut(ticket.id, statut);
            setTicket(updated);
            toast.success("Statut mis à jour avec succès");
        } catch (err) {
            toast.error("Erreur lors de la modification du statut");
        } finally {
            setSaving(false);
        }
    }
    async function handlePay() {
        if (!window.confirm(`Confirmer le paiement de ${ticket.prix} DH ?`)) {
            return;
        }

        setSaving(true);
        try {
            await createPayment(ticket.id, ticket.prix);
            toast.success("Paiement effectué avec succès.");
            loadTicket(); // recharger → statut FERME → le bouton disparaît
        } catch (err) {
            toast.error("Erreur lors du paiement.");
        } finally {
            setSaving(false);
        }
    }

    if (loading) {
        return <Loader />;
    }

    return (
        <div className="p-6">
            <button
                onClick={() => navigate(backPath)}
                className="mb-4 text-sm text-orange-600 hover:underline"
            >
                ← Retour à mes tickets
            </button>

            {error ? (
                <div className="max-w-xl mx-auto bg-white rounded-2xl shadow-md p-6 text-center">
                    <p className="text-red-500 text-sm mb-3">{error}</p>
                    <button
                        onClick={loadTicket}
                        className="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white text-sm font-semibold rounded-lg"
                    >
                        Réessayer
                    </button>
                </div>
            ) : (
                <div className="max-w-2xl mx-auto bg-white rounded-2xl shadow-md overflow-hidden">

                    <div className="bg-gradient-to-r from-orange-500 to-yellow-500 p-6 text-white">
                        <div className="flex flex-wrap items-center justify-between gap-3">
                            <h1 className="text-xl font-bold">Ticket #{ticket.id}</h1>
                            <span className={`px-3 py-1 rounded-full text-xs font-semibold ${statutColors[ticket.statut] || "bg-gray-100 text-gray-600"}`}>
                                {ticket.statut}
                            </span>
                        </div>
                        <p className="text-white/90 mt-2 text-base font-medium">{ticket.titre}</p>
                    </div>

                    <div className="p-6">
                        <h2 className="text-sm font-semibold text-gray-700 mb-2">Description</h2>
                        <p className="text-sm text-gray-600 mb-5">{ticket.description}</p>

                        <dl className="space-y-3 text-sm">
                            <div className="flex justify-between border-b pb-2">
                                <dt className="text-gray-500">Service</dt>
                                <dd className="text-gray-800 font-medium">#{ticket.serviceId}</dd>
                            </div>
                            <div className="flex justify-between border-b pb-2">
                                <dt className="text-gray-500">Client</dt>
                                <dd className="text-gray-800 font-medium">#{ticket.createdById}</dd>
                            </div>
                            <div className="flex justify-between border-b pb-2">
                                <dt className="text-gray-500">Prix</dt>
                                <dd className="text-gray-800 font-medium">
                                    {ticket.prix != null ? `${ticket.prix} DH` : "—"}
                                </dd>
                            </div>
                            <div className="flex justify-between border-b pb-2">
                                <dt className="text-gray-500">Date de création</dt>
                                <dd className="text-gray-800 font-medium">{formatDate(ticket.dateCreation)}</dd>
                            </div>
                        </dl>

                        {!canManage && ticket.statut !== "FERME" && ticket.prix != null && (
                            <button
                                onClick={handlePay}
                                disabled={saving}
                                className="mt-5 w-full px-5 py-3 bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl text-sm font-semibold transition-colors disabled:opacity-50"
                            >
                                {saving ? "Paiement..." : `Payer ${ticket.prix} DH`}
                            </button>
                        )}

                        {canManage && (
                            <div className="mt-5 bg-gray-50 rounded-xl p-4">
                                <label className="block text-xs font-medium text-gray-600 mb-2">
                                    Changer le statut
                                </label>
                                <select
                                    value={ticket.statut}
                                    disabled={saving}
                                    onChange={(e) => changeStatut(e.target.value)}
                                    className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400 disabled:opacity-50"
                                >
                                    {STATUTS.map((s) => (
                                        <option key={s} value={s}>{s}</option>
                                    ))}
                                </select>
                            </div>
                        )}
                    </div>
                </div>
            )}

            {!error && (
                <div className="max-w-2xl mx-auto mt-6">
                    <ChatWindow ticketId={ticket.id} />
                </div>
            )}
        </div>
    );
}

export default TicketDetailsView;