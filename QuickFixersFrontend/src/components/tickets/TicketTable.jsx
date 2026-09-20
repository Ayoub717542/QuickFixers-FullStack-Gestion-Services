import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { axiosApi } from "../../api/axiosApi";
import Loader from "../Loader";
import StatusBadge from "../StatusBadge";
import Pagination from "../Pagination.jsx";

function TicketTable() {
    const navigate = useNavigate();
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [ticketPage, setTicketPage] = useState(1);
    const [ticketTotalPages, setTicketTotalPages] = useState(0);

    function fetchTickets() {
        axiosApi.get("/ticket/tickets?pageNumber="+ ticketPage + "&pageSize=4&sortBy=dateCreation&sortDir=desc")
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
    }, [ticketPage]);

    function voirDetails(id) {
        navigate("/support/tickets/" + id);
    }
    if (loading) {
        return <Loader />;
    }
    return (
        <div className="bg-white rounded-xl shadow-sm overflow-hidden">
            <div className="p-5 border-b">
                <h2 className="text-lg font-bold text-gray-800">Tickets</h2>
            </div>
            <div className="overflow-x-auto">
                <table className="w-full">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="p-4 text-left">ID</th>
                        <th className="p-4 text-left">Titre</th>
                        <th className="p-4 text-left">Statut</th>
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