import { useEffect, useState } from "react";
import { axiosApi } from "../../api/axiosApi";
import Loader from "../../components/Loader";
import TicketTable from "../../components/tickets/TicketTable";
import { Ticket, CreditCard, Wallet, Clock } from "lucide-react";

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

function Dashboard() {
    const [assignedCount, setAssignedCount] = useState(0);
    const [enCoursCount, setEnCoursCount] = useState(0);
    const [incomeLabels, setIncomeLabels] = useState([]);
    const [incomeValues, setIncomeValues] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadDashboard();
    }, []);

    async function loadDashboard() {
        try {
            const ticketsResponse = await axiosApi
                .get("/ticket/tickets?pageNumber=1&pageSize=5&sortBy=dateCreation&sortDir=desc");
            const enCoursResponse = await axiosApi
                .get("/ticket/statut/EN_COURS?pageNumber=1&pageSize=5&sortBy=dateCreation&sortDir=desc");
            const incomeResponse = await axiosApi
                .get("/paiements/incomeByDay");

            setAssignedCount(ticketsResponse.data.totalElements);

            setEnCoursCount(enCoursResponse.data.totalElements);

            setIncomeLabels(incomeResponse.data.map(d => d.jour));
            setIncomeValues(incomeResponse.data.map(d => d.total));

        } catch (error) {
            console.log("Support dashboard error:", error);
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return <Loader />;
    }

    const totalIncome = incomeValues.reduce((sum, value) => sum + value, 0);
    const todayIncome = incomeValues[incomeValues.length - 1] || 0;

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
            {/* Statistics */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-3 mb-3">

                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Tickets assignés</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">
                                {assignedCount}
                            </h2>
                        </div>
                        <div className="bg-blue-100 p-3 rounded-full">
                            <Ticket size={20} className="text-blue-500" />
                        </div>
                    </div>
                </div>

                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Revenus (total)</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">
                                {totalIncome} DH
                            </h2>
                        </div>
                        <div className="bg-green-100 p-3 rounded-full">
                            <CreditCard size={20} className="text-green-500" />
                        </div>
                    </div>
                </div>

                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">Aujourd'hui</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">
                                {todayIncome} DH
                            </h2>
                        </div>
                        <div className="bg-purple-100 p-3 rounded-full">
                            <Wallet size={20} className="text-purple-500" />
                        </div>
                    </div>
                </div>

                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-500">En cours</p>
                            <h2 className="text-3xl font-bold text-gray-800 mt-2">
                                {enCoursCount}
                            </h2>
                        </div>
                        <div className="bg-orange-100 p-3 rounded-full">
                            <Clock size={20} className="text-orange-500" />
                        </div>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-3 mb-3">
                <div className="bg-white p-3 rounded-xl shadow-sm">
                    <h2 className="text-sm font-bold text-gray-800 mb-4">Mes revenus par jour</h2>
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

                <TicketTable title="Tickets assignés" />
            </div>

            </div>
    );
}

export default Dashboard;