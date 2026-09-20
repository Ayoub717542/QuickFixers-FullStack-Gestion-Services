import TicketTable from "../../components/tickets/TicketTable.jsx";
import Pagination from "../../components/Pagination.jsx";
import {useState} from "react";

function Tickets(){

    return(
        <>
            <div>
                <TicketTable title="Tickets" />

            </div>


        </>

    )
}
export default Tickets;