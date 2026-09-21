const statutColors = {
    ACTIVE: "bg-green-100 text-green-600",
    INACTIVE: "bg-gray-100 text-gray-600"
};
const typeColors = {
    ELECTROMENAGER: "bg-blue-100 text-blue-600",
    ELECTRONIQUE: "bg-purple-100 text-purple-600",
    INFORMATIQUE: "bg-cyan-100 text-cyan-600",
    TELEPHONIE: "bg-yellow-100 text-yellow-600"
};
function ServiceCard({ service, canManage = false, onDelete, onToggleStatut }) {
    return (
        <div className="bg-white rounded-xl shadow-sm border p-5 flex flex-col gap-3">
            <h3 className="text-lg font-bold text-gray-800">{service.nom}</h3>
            <div className="flex gap-2">
                <span className={`px-3 py-1 rounded-full text-xs font-semibold ${statutColors[service.statut] || "bg-gray-100 text-gray-600"}`}>
                    {service.statut}
                </span>
                <span className={`px-3 py-1 rounded-full text-xs font-semibold ${typeColors[service.type] || "bg-gray-100 text-gray-600"}`}>
                    {service.type}
                </span>
            </div>
            <p className="text-sm text-gray-500">
                {service.prix != null ? `${service.prix} DH` : "—"}
            </p>
            {canManage && (
                <div className="flex items-center justify-between mt-auto pt-2 border-t">
                    <button
                        onClick={() => onToggleStatut(service)}
                        className={`px-3 py-1.5 rounded-lg text-sm font-medium ${
                            service.statut === "ACTIVE"
                                ? "bg-gray-100 text-gray-700 hover:bg-gray-200"
                                : "bg-green-500 text-white hover:bg-green-600"
                        }`}
                    >
                        {service.statut === "ACTIVE" ? "Désactiver" : "Activer"}
                    </button>
                    <button
                        onClick={() => onDelete(service)}
                        className="px-3 py-1.5 bg-red-500 text-white rounded-lg text-sm hover:bg-red-600"
                    >
                        Supprimer
                    </button>
                </div>
            )}
        </div>
    );
}

export default ServiceCard;