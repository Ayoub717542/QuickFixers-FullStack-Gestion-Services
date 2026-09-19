import { useEffect, useState } from "react";
import { axiosApi } from "../../api/axiosApi";
import Loader from "../../components/Loader";
import {Ticket, CreditCard} from "lucide-react";
import { Bar } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Tooltip,
    Legend,
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, Tooltip, Legend);

function Dashboard() {
    const [tickets, setTickets] = useState(0);
    const [payments, setPayments] = useState(0);
    const [recentTickets, setRecentTickets] = useState([]);
    const [recentPayments, setRecentPayments] = useState([]);

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadDashboard();
    }, []);

    if (loading) {
        return <Loader />;
    }

    const dayOf = (dateCreation) => {
        if (Array.isArray(dateCreation)) {
            const [y, m, d] = dateCreation;
            return `${y}-${String(m).padStart(2, "0")}-${String(d).padStart(2, "0")}`;
        }
        return String(dateCreation).slice(0, 10);
    };

    const depensesParJour = recentPayments.reduce((acc, payment) => {
        if (payment.statut !== "TERMINE") return acc;
        const jour = dayOf(payment.dateCreation);
        acc[jour] = (acc[jour] || 0) + payment.montant;
        return acc;
    }, {});

    const jours = Object.keys(depensesParJour).sort();

    const chartData = {
        labels: jours,
        datasets: [
            {
                label: "Dépenses (DH)",
                data: jours.map((jour) => depensesParJour[jour]),
                backgroundColor: "#22c55e",
            },
        ],
    };

    async function loadDashboard() {

        try {

            const ticketsResponse = await axiosApi.get("/ticket/countTickets");
            const paymentsResponse = await axiosApi.get("/paiements/paiements");
            const recentTicketsResponse = await axiosApi.get("/ticket/tickets?pageNumber=1&pageSize=5&sortBy=dateCreation&sortDir=desc");
            const paymentsHistoryResponse = await axiosApi.get("/paiements/paimentHistorique?pageNumber=1&pageSize=1000&sortBy=id&sortDir=desc");

            setTickets(ticketsResponse.data);
            setPayments(paymentsResponse.data);

            setRecentTickets(recentTicketsResponse.data.content);
            setRecentPayments(paymentsHistoryResponse.data.content);
        } catch (error) {
            console.log("Dashboard error:", error);
        }
        finally {
            setLoading(false);
        }
    }
    return (
        <div className="p-3 bg-gray-100 ">
            {/*cards*/}

            <div className="grid grid-cols-1 md:grid-cols-2 gap-3 mb-3">

                <div className="bg-white p-2 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Mes tickets</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">{tickets}</h2>
                        </div>
                        <div className="bg-orange-100 p-3 rounded-full">
                            <Ticket size={20} className="text-orange-500"/>
                        </div>
                    </div>
                </div>


                <div className="bg-white p-3 rounded-xl shadow-sm">

                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Mes paiements</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">{payments}</h2>
                        </div>
                        <div className="bg-green-100 p-3 rounded-full">
                            <CreditCard size={20} className="text-green-500"/>
                        </div>
                    </div>

                </div>

            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-3 mb-3">

                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <h2 className="text-sm font-bold text-gray-800 mb-4">Mes paiements par jour</h2>
                    <div style={{ height: "150px" }}>
                        <Bar
                            data={chartData}
                            options={{
                                maintainAspectRatio: false,
                                plugins: { legend: { display: false } },
                            }}
                        />
                    </div>
                </div>

                {/* Recent Payments */}
                <div className="bg-white rounded-xl shadow-sm">
                    <div className="p-5 border-b">
                        <h2 className="text-lg font-bold text-gray-800">
                            Mes paiements récents
                        </h2>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead>
                            <tr className="text-left text-sm text-gray-500 border-b">
                                <th className="p-4">ID</th>
                                <th className="p-4">Montant</th>
                                <th className="p-4">Statut</th>
                                <th className="p-4">Ticket</th>
                                <th className="p-4">Date</th>
                            </tr>
                            </thead>

                            <tbody>
                            {recentPayments.slice(0, 5).map((payment) => (
                                <tr key={payment.id} className="border-b last:border-b-0 hover:bg-gray-50">

                                    <td className="px-2 py-1 text-sm">#{payment.id}</td>
                                    <td className="px-2 py-1 text-sm font-medium">{payment.montant} DH</td>
                                    <td className="px-2 py-1">
                                    <span className="px-3 py-1 rounded-full text-[10px] bg-green-100 text-green-600">
                                        {payment.statut}
                                    </span></td>
                                    <td className="p-4 text-sm">#{payment.ticketId}</td>
                                    <td className="p-4 text-sm text-gray-500">{payment.dateCreation}</td>

                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            {/* Recent Tickets */}
            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden mb-6">
                <div className="p-5 border-b border-gray-100">
                    <h2 className="text-lg font-bold text-gray-800">Mes tickets récents</h2>
                </div>
                <div className="overflow-x-auto">
                    <table className="w-full">
                        <thead>
                        <tr className="bg-gray-50 text-left text-[10px] font-semibold text-gray-500 uppercase tracking-wide">
                            <th className="p-4">ID</th>
                            <th className="p-4">Titre</th>
                            <th className="p-4">Statut</th>
                            <th className="p-4">Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        {recentTickets.map((ticket) => (
                            <tr key={ticket.id} className="border-b last:border-b-0 hover:bg-gray-50">
                                <td className="p-4 text-sm">#{ticket.id}</td>
                                <td className="p-4 text-sm font-medium">{ticket.titre}</td>
                                <td className="p-4">
                                    <span className="px-3 py-1 rounded-full text-xs bg-orange-100 text-orange-600">
                                        {ticket.statut}
                                    </span>
                                </td>
                                <td className="p-4 text-sm text-gray-500">{ticket.dateCreation}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    );
}
export default Dashboard;