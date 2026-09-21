import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import Loader from "../../components/Loader";
import Pagination from "../../components/Pagination";
import { fetchPayments } from "../../api/paiementApi";

const statutColors = {
    EN_ATTENTE: "bg-yellow-100 text-yellow-700",
    TERMINE: "bg-green-100 text-green-700",
    ECHOUE: "bg-red-100 text-red-700",
    REMBOURSE: "bg-gray-100 text-gray-600"
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

function Payments() {
    const [payments, setPayments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);

    function fetchList() {
        setLoading(true);
        fetchPayments(page)
            .then((data) => {
                setPayments(data.content);
                setTotalPages(data.totalPages);
            })
            .catch(() => {
                toast.error("Impossible de charger les paiements.");
            })
            .finally(() => {
                setLoading(false);
            });
    }

    useEffect(() => {
        fetchList();
    }, [page]);

    if (loading) {
        return <Loader />;
    }

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold text-gray-800 mb-4">Paiements</h1>

            <div className="bg-white rounded-2xl shadow-sm overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="p-4 text-left">ID</th>
                            <th className="p-4 text-left">Montant</th>
                            <th className="p-4 text-left">Statut</th>
                            <th className="p-4 text-left">Date</th>
                            <th className="p-4 text-left">Client</th>
                            <th className="p-4 text-left">Ticket</th>
                        </tr>
                        </thead>
                        <tbody>
                        {payments.length === 0 ? (
                            <tr>
                                <td colSpan={6} className="p-8 text-center text-gray-500">
                                    Aucun paiement trouvé
                                </td>
                            </tr>
                        ) : (
                            payments.map((p) => (
                                <tr key={p.id} className="border-b hover:bg-gray-50">
                                    <td className="p-4">#{p.id}</td>
                                    <td className="p-4 font-medium">{p.montant} DH</td>
                                    <td className="p-4">
                                        <span className={`px-3 py-1 rounded-full text-xs font-semibold ${statutColors[p.statut] || "bg-gray-100 text-gray-600"}`}>
                                            {p.statut}
                                        </span>
                                    </td>
                                    <td className="p-4 text-gray-500">{formatDate(p.dateCreation)}</td>
                                    <td className="p-4 text-gray-700">{p.email}</td>
                                    <td className="p-4 text-gray-500">#{p.ticketId}</td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>
            </div>

            <Pagination page={page} totalPages={totalPages} onChange={setPage} />
        </div>
    );
}

export default Payments;