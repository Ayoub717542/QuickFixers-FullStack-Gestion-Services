import TicketDetailsView from "../../components/tickets/TicketDetailsView";
function TicketDetails() {
    return (<>
        <TicketDetailsView backPath={"/admin/tickets" } canEdit ></TicketDetailsView>
    </>)
}
export default TicketDetails;