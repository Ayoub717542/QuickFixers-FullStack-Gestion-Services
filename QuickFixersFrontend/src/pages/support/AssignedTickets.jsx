import TicketTable from "../../components/tickets/TicketTable";

function AssignedTickets() {
    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold text-gray-800 mb-4">Mes tickets assignés</h1>
            <TicketTable title="Tickets" editable />
        </div>
    );
}

export default AssignedTickets;