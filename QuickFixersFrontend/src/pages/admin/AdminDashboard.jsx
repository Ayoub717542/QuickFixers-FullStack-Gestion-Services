import { useEffect, useState } from "react";
import { axiosApi } from "../../api/axiosApi";
import Loader from "../../components/Loader";
import Pagination from "../../components/Pagination";
import TicketTable from "../../components/tickets/TicketTable";
import {
    Ticket,
    Users,
    Wrench,
    CreditCard
} from "lucide-react";

import { Line } from "react-chartjs-2";

import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Tooltip,
    Legend
} from "chart.js";

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Tooltip,
    Legend
);

function AdminDashboard() {
    const [services, setServices] = useState(0);
    const [tickets, setTickets] = useState(0);
    const [users, setUsers] = useState(0);
    const [payments, setPayments] = useState(0);
    const [recentPayments, setRecentPayments] = useState([]);
    const [paymentPage, setPaymentPage] = useState(1);
    const [paymentTotalPages, setPaymentTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);
    const [incomeLabels, setIncomeLabels] = useState([]);
    const [incomeValues, setIncomeValues] = useState([]);

    useEffect(() => {
        loadDashboard();
    }, []);

    useEffect(() => {
        loadPayments();
    }, [paymentPage]);

    async function loadDashboard() {

        try {
            const servicesResponse = await axiosApi
                .get("/service/countServices");
            const ticketsResponse = await axiosApi
                .get("/ticket/countTickets");
            const usersResponse = await axiosApi
                .get("/users/countUsers");
            const paymentsResponse = await axiosApi
                .get("/paiements/paiements");
            const res = await axiosApi.get("/paiements/incomeByDay");

            setIncomeLabels(res.data.map(d => d.jour));
            setIncomeValues(res.data.map(d => d.total));

            setServices(servicesResponse.data);
            setTickets(ticketsResponse.data);
            setUsers(usersResponse.data);
            setPayments(paymentsResponse.data);

        } catch (error) {
            console.log("Dashboard error:", error);
        } finally {
            setLoading(false);
        }
    }

    async function loadPayments() {
        try {
            const response = await axiosApi
                .get("/paiements/paimentHistorique?pageNumber=" + paymentPage + "&pageSize=5&sortBy=id&sortDir=desc");
            setRecentPayments(response.data.content);
            setPaymentTotalPages(response.data.totalPages);
        } catch (error) {
            console.log("Payments error:", error);
        }
    }
    if (loading) {
        return <Loader />;
    }
    const chartData = {
        labels: incomeLabels,
        datasets: [
            {
                label: "Revenus",
                data: incomeValues,
                borderColor: "#22c55e",
                backgroundColor: "#22c55e",
                tension: 0.3
            }
        ]
    };
    return (
        <div className="p-3 bg-gray-100">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-3 mb-3">
                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Services</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">{services}</h2>
                        </div>

                        <div className="bg-orange-100 p-3 rounded-full">
                            <Wrench size={20} className="text-orange-500"/>
                        </div>
                    </div>

                </div>
                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Tickets</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">{tickets}</h2>
                        </div>
                        <div className="bg-blue-100 p-3 rounded-full">
                            <Ticket size={20} className="text-blue-500"/>
                        </div>
                    </div>
                </div>

                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Utilisateurs</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">{users}</h2>
                        </div>
                        <div className="bg-green-100 p-3 rounded-full">
                            <Users size={20} className="text-green-500"/>
                        </div>
                    </div>
                </div>

                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Paiements</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">{payments}</h2>
                        </div>
                        <div className="bg-purple-100 p-3 rounded-full">
                            <CreditCard size={20} className="text-purple-500"/>
                        </div>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-3 mb-3">

                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <h2 className="text-sm font-bold text-gray-800 mb-4">Revenus par jour</h2>

                    <div style={{ height: "150px" }}>
                        <Line
                            data={chartData}
                            options={{
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: {
                                        display: false
                                    }
                                }
                            }}
                        />
                    </div>

                </div>
                <div className="bg-white rounded-xl shadow-sm">
                    <div className="p-5 border-b">
                        <h2 className="text-lg font-bold text-gray-800">Paiements récents</h2>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead>
                            <tr className="text-left text-sm text-gray-500 border-b">
                                <th className="p-4">ID</th>
                                <th className="p-4">Montant</th>
                                <th className="p-4">Statut</th>
                                <th className="p-4">Ticket</th>
                            </tr>
                            </thead>
                            <tbody>
                            {recentPayments.map((payment) => (
                                <tr key={payment.id} className="border-b last:border-b-0 hover:bg-gray-50">
                                    <td className="px-2 py-2 text-sm">#{payment.id}</td>
                                    <td className="px-2 py-2 text-sm font-medium">{payment.montant} DH</td>
                                    <td className="px-2 py-2">
                                        <span className="px-3 py-1 rounded-full text-[10px] bg-green-100 text-green-600">
                                            {payment.statut}
                                        </span>
                                    </td>
                                    <td className="px-2 py-2 text-sm">#{payment.ticketId}</td>

                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

                    <div className="p-4">
                        <Pagination page={paymentPage} totalPages={paymentTotalPages} onChange={setPaymentPage} />
                    </div>
                </div>
            </div>

            <TicketTable title="Tickets récents" />

        </div>
    );
}

export default AdminDashboard;