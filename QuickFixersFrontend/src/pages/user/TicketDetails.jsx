import TicketDetailsView from "../../components/tickets/TicketDetailsView";

function TicketDetails() {
    return <TicketDetailsView backPath="/user/tickets" canManage={false} />;
}
export default TicketDetails;