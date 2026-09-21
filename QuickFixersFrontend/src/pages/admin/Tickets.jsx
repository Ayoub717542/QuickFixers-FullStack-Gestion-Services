import TicketTable from "../../components/tickets/TicketTable.jsx";

function Tickets(){

    return(
        <>
            <div>
                <TicketTable title="Tickets" basePath={"/admin/tickets"}/>

            </div>


        </>

    )
}
export default Tickets;