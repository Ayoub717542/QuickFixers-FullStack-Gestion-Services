import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { axiosApi } from "../../api/axiosApi";
import Loader from "../Loader";
import StatusBadge from "../StatusBadge";
import PriorityBadge from "../PriorityBadge";
import Pagination from "../Pagination.jsx";
import { PRIORITIES, getPriorityMeta } from "../../utils/priority";

function TicketTable({ title = "Tickets" , basePath = "/support/tickets"}) {
    const navigate = useNavigate();
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [ticketPage, setTicketPage] = useState(1);
    const [ticketTotalPages, setTicketTotalPages] = useState(0);

    const [recherche, setRecherche] = useState("");
    const [statut, setStatut] = useState("");
    const [priorite, setPriorite] = useState("");

    function fetchTickets() {
        let url = "";
        if (recherche !== "") {
            url = "/ticket/recherche?recherche=" + recherche + "&pageNumber=" + ticketPage + "&pageSize=4";
        } else if (statut !== "") {
            url = "/ticket/statut/" + statut + "?pageNumber=" + ticketPage + "&pageSize=4";
        } else if (priorite !== "") {
            url = "/ticket/priority/" + priorite + "?pageNumber=" + ticketPage + "&pageSize=4";
        } else {
            url = "/ticket/tickets?pageNumber=" + ticketPage + "&pageSize=4&sortBy=dateCreation&sortDir=desc";
        }
        axiosApi.get(url)
            .then((response) => {
                setTickets(response.data.content);
                setTicketTotalPages(response.data.totalPages);
            })
            .catch((error) => {
                console.error(error);
                toast.error("Impossible de charger les tickets.");
            })
            .finally(() => {
                setLoading(false);
            });
    }

    useEffect(() => {
        fetchTickets();
    }, [ticketPage, recherche, statut, priorite]);

    function handleRecherche(e) {
        setRecherche(e.target.value);
        setTicketPage(1);
    }

    function handleStatut(e) {
        setStatut(e.target.value);
        setPriorite("");
        setTicketPage(1);
    }

    function handlePriorite(e) {
        setPriorite(e.target.value);
        setStatut("");
        setTicketPage(1);
    }

    function voirDetails(id) {
        navigate(basePath + "/" + id);
    }

    if (loading) {
        return <Loader />;
    }

    return (
        <div className="bg-white rounded-xl shadow-sm overflow-x-auto">
            <div className="p-5 border-b flex flex-wrap items-center justify-between gap-2">
                <h2 className="text-lg font-bold text-gray-800">{title}</h2>

                <div className="flex flex-wrap gap-2 w-full sm:w-auto">
                    <input
                        type="text"
                        placeholder="Rechercher..."
                        value={recherche}
                        onChange={handleRecherche}
                        className="border rounded-lg px-3 py-2 text-sm flex-1 min-w-[150px]"
                    />

                    <select
                        value={statut}
                        onChange={handleStatut}
                        className="border rounded-lg px-3 py-2 text-sm flex-1 min-w-[150px]"
                    >
                        <option value="">Tous les statuts</option>
                        <option value="OUVERT">OUVERT</option>
                        <option value="EN_COURS">EN_COURS</option>
                        <option value="RESOLU">RESOLU</option>
                        <option value="FERME">FERME</option>
                    </select>

                    <select
                        value={priorite}
                        onChange={handlePriorite}
                        className="border rounded-lg px-3 py-2 text-sm flex-1 min-w-[150px]"
                    >
                        <option value="">Toutes les priorités</option>
                        {PRIORITIES.map((p) => (
                            <option key={p} value={p}>{getPriorityMeta(p).label}</option>
                        ))}
                    </select>
                </div>
            </div>

            <div className="overflow-x-auto">
                <table className="w-full min-w-[640px]">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="p-4 text-left">ID</th>
                        <th className="p-4 text-left">Titre</th>
                        <th className="p-4 text-left">Statut</th>
                        <th className="p-4 text-left">Priorité</th>
                        <th className="p-4 text-left">Date</th>
                        <th className="p-4 text-right">Action</th>
                    </tr>
                    </thead>

                    <tbody>
                    {tickets.map((ticket) => (
                        <tr key={ticket.id} className="border-b hover:bg-gray-50">
                            <td className="p-4">#{ticket.id}</td>
                            <td className="p-4 font-medium">{ticket.titre}</td>
                            <td className="p-4"><StatusBadge status={ticket.statut} /></td>
                            <td className="p-4">
                                <div className="flex items-center gap-2">
                                    <PriorityBadge priority={ticket.priority} />
                                    {ticket.enRetard && (
                                        <span className="rounded-full bg-red-100 px-2 py-0.5 text-[11px] font-semibold text-red-600">
                                            En retard
                                        </span>
                                    )}
                                </div>
                            </td>
                            <td className="p-4 text-gray-500">{ticket.dateCreation}</td>
                            <td className="p-4 text-right">
                                <button
                                    onClick={() => voirDetails(ticket.id)}
                                    className="px-3 py-1.5 bg-orange-500 text-white rounded-lg text-sm"
                                >
                                    Voir détails
                                </button>
                            </td>

                        </tr>

                    ))}

                    </tbody>

                </table>
            </div>
            <div className="p-4">
                <Pagination page={ticketPage} totalPages={ticketTotalPages} onChange={setTicketPage} />
            </div>
        </div>
    );
}
export default TicketTable;
