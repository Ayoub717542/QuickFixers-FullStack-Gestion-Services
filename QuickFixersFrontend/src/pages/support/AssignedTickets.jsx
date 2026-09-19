import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import Loader from "../../components/Loader";
import { fetchAssignedTickets, updateTicketStatut } from "../../api/ticketApi";

const STATUTS = ["OUVERT", "EN_COURS", "RESOLU", "FERME"];

const statutColors = {
    OUVERT: "bg-blue-100 text-blue-600",
    EN_COURS: "bg-orange-100 text-orange-600",
    RESOLU: "bg-green-100 text-green-600",
    FERME: "bg-gray-100 text-gray-600"
};

function AssignedTickets() {
    const navigate = useNavigate();

    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [pageNumber, setPageNumber] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [updatingId, setUpdatingId] = useState(null);

    useEffect(() => {
        loadTickets();
    }, [pageNumber]);

    async function loadTickets() {
        setLoading(true);
        setError("");
        try {
            const data = await fetchAssignedTickets(pageNumber, 5, "dateCreation", "desc");

            // If the asked page became empty (e.g. last ticket was deleted), step back.
            if (data.content.length === 0 && pageNumber > 1) {
                setPageNumber(pageNumber - 1);
                return;
            }

            setTickets(data.content);
            setTotalPages(data.totalPages);
            setTotalElements(data.totalElements);
        } catch (err) {
            setError("Impossible de charger vos tickets.");
        } finally {
            setLoading(false);
        }
    }

    async function changeStatut(ticketId, statut) {
        if (!statut) return;

        setUpdatingId(ticketId);
        try {
            await updateTicketStatut(ticketId, statut);
            toast.success("Statut mis à jour avec succès");
            await loadTickets();
        } catch (err) {
            toast.error("Erreur lors de la modification du statut");
        } finally {
            setUpdatingId(null);
        }
    }

    function openDetails(ticketId) {
        navigate(`/support/tickets/${ticketId}`);
    }

    if (loading && tickets.length === 0) {
        return <Loader />;
    }

    return (
        <div className="p-6">
            <div className="flex items-center justify-between mb-4">
                <h1 className="text-2xl font-bold text-gray-800">Mes tickets assignés</h1>
                <span className="bg-orange-100 text-orange-600 px-3 py-1 rounded-full text-xs font-semibold">
                    {totalElements} ticket{totalElements > 1 ? "s" : ""}
                </span>
            </div>

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                {error ? (
                    <div className="p-6 text-center">
                        <p className="text-red-500 text-sm mb-3">{error}</p>
                        <button
                            onClick={loadTickets}
                            className="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white text-sm font-semibold rounded-lg"
                        >
                            Réessayer
                        </button>
                    </div>
                ) : (
                    <table className="w-full text-left">
                        <thead className="bg-gray-100">
                            <tr>
                                <th className="p-3 text-sm text-gray-600">ID</th>
                                <th className="p-3 text-sm text-gray-600">Titre</th>
                                <th className="p-3 text-sm text-gray-600">Statut</th>
                                <th className="p-3 text-sm text-gray-600">Changer le statut</th>
                                <th className="p-3 text-sm text-gray-600 text-right">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tickets.map((ticket) => (
                                <tr key={ticket.id} className="border-t hover:bg-gray-50">
                                    <td className="p-3 text-sm text-gray-500">#{ticket.id}</td>
                                    <td className="p-3 text-sm font-medium text-gray-800">{ticket.titre}</td>
                                    <td className="p-3">
                                        <span className={`px-3 py-1 rounded-full text-xs font-semibold ${statutColors[ticket.statut] || "bg-gray-100 text-gray-600"}`}>
                                            {ticket.statut}
                                        </span>
                                    </td>
                                    <td className="p-3">
                                        {updatingId === ticket.id ? (
                                            <span className="text-xs text-gray-500">Enregistrement...</span>
                                        ) : (
                                            <select
                                                value={ticket.statut}
                                                onChange={(e) => changeStatut(ticket.id, e.target.value)}
                                                className="border border-gray-300 rounded-lg px-2 py-1 text-sm"
                                            >
                                                {STATUTS.map((s) => (
                                                    <option key={s} value={s}>{s}</option>
                                                ))}
                                            </select>
                                        )}
                                    </td>
                                    <td className="p-3 text-right">
                                        <button
                                            onClick={() => openDetails(ticket.id)}
                                            className="px-3 py-1.5 bg-orange-500 hover:bg-orange-600 text-white text-xs font-semibold rounded-lg transition"
                                        >
                                            Voir détails
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}

                {!error && tickets.length === 0 && (
                    <p className="p-4 text-center text-gray-500 text-sm">Aucun ticket assigné.</p>
                )}
            </div>

            {totalPages > 1 && (
                <div className="flex items-center justify-between mt-4">
                    <button
                        onClick={() => setPageNumber((p) => Math.max(1, p - 1))}
                        disabled={pageNumber === 1 || loading}
                        className="px-4 py-2 bg-white border border-gray-300 rounded-lg text-sm hover:bg-gray-50 disabled:opacity-50"
                    >
                        Précédent
                    </button>
                    <span className="text-sm text-gray-600">
                        Page {pageNumber} / {totalPages}
                    </span>
                    <button
                        onClick={() => setPageNumber((p) => Math.min(totalPages, p + 1))}
                        disabled={pageNumber === totalPages || loading}
                        className="px-4 py-2 bg-white border border-gray-300 rounded-lg text-sm hover:bg-gray-50 disabled:opacity-50"
                    >
                        Suivant
                    </button>
                </div>
            )}
        </div>
    );
}

export default AssignedTickets;