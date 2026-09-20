function StatusBadge({ status }) {
    const colors = {
        OUVERT: "bg-blue-100 text-blue-600",
        EN_COURS: "bg-orange-100 text-orange-600",
        RESOLU: "bg-green-100 text-green-600",
        FERME: "bg-gray-100 text-gray-600"
    };
    return (
        <span className={`px-3 py-1 rounded-full text-xs font-semibold ${colors[status]}`}>
            {status}
        </span>
    );
}

export default StatusBadge;