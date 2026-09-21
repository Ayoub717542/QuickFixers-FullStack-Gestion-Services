import { Wrench, Trash2, Power } from "lucide-react";

// Couleurs des badges selon le statut
const statutColors = {
    ACTIVE: "bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200",
    INACTIVE: "bg-gray-100 text-gray-500 ring-1 ring-gray-200"
};

function ServiceCard({ service, canManage = false, onDelete, onToggleStatut ,onClick}) {
    return (
        <div onClick={onClick} className="cursor-pointer bg-white rounded-2xl shadow-sm hover:shadow-lg transition-shadow p-5 flex flex-col gap-4">
            <div className="flex items-start justify-between gap-3">
                <div className="flex items-center gap-3 min-w-0">
                    <div className="w-10 h-10 rounded-xl bg-orange-50 flex items-center justify-center shrink-0">
                        <Wrench size={20} className="text-orange-500" />
                    </div>
                    <div className="truncate">
                        <h3 className="font-bold text-gray-900 truncate">{service.nom}</h3>
                        <p className="text-xs text-gray-400 uppercase tracking-wide mt-0.5">
                            {service.type}
                        </p>
                    </div>
                </div>
                <span className={`px-2.5 py-1 rounded-full text-[11px] font-semibold whitespace-nowrap ${statutColors[service.statut] || statutColors.INACTIVE}`}>
                    {service.statut}
                </span>
            </div>

            <div className="border-t border-gray-100 pt-4 flex items-center justify-between">
                <span className="text-xs text-gray-400">Prix</span>
                <span className="text-lg font-bold text-gray-900">
                    {service.prix != null ? `${service.prix} DH` : "—"}
                </span>
            </div>
            {canManage && (
                <div className="flex gap-2 pt-1 mt-auto">
                    <button
                        onClick={(e) => {e.stopPropagation(); onToggleStatut(service)}}
                        className={`flex-1 flex items-center justify-center gap-1.5 px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                            service.statut === "ACTIVE"
                                ? "bg-gray-100 text-gray-600 hover:bg-gray-200"
                                : "bg-emerald-500 text-white hover:bg-emerald-600"
                        }`}
                    >
                        <Power size={14} />
                        {service.statut === "ACTIVE" ? "Désactiver" : "Activer"}
                    </button>
                    <button
                        onClick={(e) => { e.stopPropagation(); onDelete(service)}}
                        className="px-3 py-2 flex items-center justify-center gap-1.5 rounded-lg text-sm font-medium text-red-500 bg-red-50 hover:bg-red-100 transition-colors"
                    >
                        <Trash2 size={14} />
                        Supprimer
                    </button>
                </div>
            )}
        </div>

    );
}

export default ServiceCard;