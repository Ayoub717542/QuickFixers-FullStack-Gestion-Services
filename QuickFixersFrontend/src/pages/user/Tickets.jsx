import { Link } from "react-router-dom";
import TicketTable from "../../components/tickets/TicketTable";

function Tickets() {
    return (
        <div className="p-6">
            <div className="flex flex-wrap items-center justify-between gap-2 mb-4">
                <h1 className="text-2xl font-bold text-gray-800">Mes tickets</h1>
                <Link
                    to="/user/tickets/create"
                    className="px-4 py-2 bg-orange-500 text-white rounded-lg text-sm"
                >
                    + Nouveau ticket
                </Link>
            </div>

            <TicketTable title="Mes tickets" basePath="/user/tickets" />
        </div>
    );
}

export default Tickets;