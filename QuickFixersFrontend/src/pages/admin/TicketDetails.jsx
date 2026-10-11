import TicketDetailsView from "../../components/tickets/TicketDetailsView";
function TicketDetails() {
    return (<>
        <TicketDetailsView backPath={"/admin/tickets" } canEdit canChangePriority ></TicketDetailsView>
    </>)
}
export default TicketDetails;