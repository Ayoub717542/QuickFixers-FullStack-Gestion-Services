import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { axiosApi } from "../../api/axiosApi";
import Loader from "../Loader";
import Pagination from "../Pagination";

const STATUTS = ["OUVERT", "EN_COURS", "RESOLU", "FERME"];

const statutColors = {
    OUVERT: "bg-blue-100 text-blue-600",
    EN_COURS: "bg-orange-100 text-orange-600",
    RESOLU: "bg-green-100 text-green-600",
    FERME: "bg-gray-100 text-gray-600"
};

function TicketTable({ title = "Tickets", editable = false, pageSize = 5 }) {
    const navigate = useNavigate();

    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const [search, setSearch] = useState("");
    const [statut, setStatut] = useState("");
    const [updatingId, setUpdatingId] = useState(null);

    useEffect(() => {
        loadTickets();
    }, [page, search, statut]);

    async function loadTickets() {
        setLoading(true);
        setError("");
        try {
            let url =
                "/ticket/tickets?pageNumber=" + page +
                "&pageSize=" + pageSize +
                "&sortBy=dateCreation&sortDir=desc";

            if (search.trim() !== "") {
                url =
                    "/ticket/recherche?recherche=" + search.trim() +
                    "&pageNumber=" + page +
                    "&pageSize=" + pageSize;
            } else if (statut !== "") {
                url =
                    "/ticket/statut/" + statut +
                    "?pageNumber=" + page +
                    "&pageSize=" + pageSize;
            }

            const response = await axiosApi.get(url);
            setTickets(response.data.content);
            setTotalPages(response.data.totalPages);
            setTotalElements(response.data.totalElements);
        } catch (err) {
            setError("Impossible de charger les tickets.");
        } finally {
            setLoading(false);
        }
    }

    function handleSearchChange(value) {
        setSearch(value);
        setPage(1);
    }

    function handleStatutChange(value) {
        setStatut(value);
        setPage(1);
    }

    async function changeStatut(ticketId, newStatut) {
        if (!newStatut) return;

        setUpdatingId(ticketId);
        try {
            await axiosApi.patch("/ticket/statut/" + ticketId + "/" + newStatut);
            toast.success("Statut mis à jour avec succès");
            loadTickets();
        } catch (err) {
            toast.error("Erreur lors de la modification du statut");
        } finally {
            setUpdatingId(null);
        }
    }

    function openDetails(ticketId) {
        navigate("/support/tickets/" + ticketId);
    }

    if (loading && tickets.length === 0) {
        return <Loader />;
    }

    return (
        <div className="bg-white rounded-xl shadow-sm overflow-hidden">
            {/* Toolbar */}
            <div className="p-5 border-b border-gray-100 flex flex-wrap items-center justify-between gap-3">
                <div className="flex items-center gap-2">
                    <h2 className="text-lg font-bold text-gray-800">{title}</h2>
                    <span className="bg-orange-100 text-orange-600 px-3 py-1 rounded-full text-xs font-semibold">
                        {totalElements} ticket{totalElements > 1 ? "s" : ""}
                    </span>
                </div>

                <div className="flex flex-wrap items-center gap-2">
                    <input
                        type="text"
                        value={search}
                        placeholder="Rechercher..."
                        onChange={(e) => handleSearchChange(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                    />
                    <select
                        value={statut}
                        onChange={(e) => handleStatutChange(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                    >
                        <option value="">Tous les statuts</option>
                        {STATUTS.map((s) => (
                            <option key={s} value={s}>{s}</option>
                        ))}
                    </select>
                </div>
            </div>

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
                <div className="overflow-x-auto">
                    <table className="w-full">
                        <thead className="bg-gray-50 text-left text-[10px] font-semibold text-gray-500 uppercase tracking-wide">
                            <tr>
                                <th className="p-4">ID</th>
                                <th className="p-4">Titre</th>
                                <th className="p-4">Statut</th>
                                <th className="p-4">Date</th>
                                {editable && <th className="p-4">Changer le statut</th>}
                                {editable && <th className="p-4 text-right">Actions</th>}
                            </tr>
                        </thead>
                        <tbody>
                            {tickets.map((ticket) => (
                                <tr key={ticket.id} className="border-b last:border-b-0 hover:bg-gray-50">
                                    <td className="p-4 text-sm">#{ticket.id}</td>
                                    <td className="p-4 text-sm font-medium">{ticket.titre}</td>
                                    <td className="p-4">
                                        <span className={`px-3 py-1 rounded-full text-xs font-semibold ${statutColors[ticket.statut] || "bg-gray-100 text-gray-600"}`}>
                                            {ticket.statut}
                                        </span>
                                    </td>
                                    <td className="p-4 text-sm text-gray-500">{ticket.dateCreation}</td>

                                    {editable && (
                                        <td className="p-4">
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
                                    )}

                                    {editable && (
                                        <td className="p-4 text-right">
                                            <button
                                                onClick={() => openDetails(ticket.id)}
                                                className="px-3 py-1.5 bg-orange-500 hover:bg-orange-600 text-white text-xs font-semibold rounded-lg transition"
                                            >
                                                Voir détails
                                            </button>
                                        </td>
                                    )}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {!error && tickets.length === 0 && (
                <p className="p-4 text-center text-gray-500 text-sm">Aucun ticket.</p>
            )}

            <div className="px-4 pb-4">
                <Pagination page={page} totalPages={totalPages} onChange={setPage} />
            </div>
        </div>
    );
}

export default TicketTable;