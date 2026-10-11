// Référentiel unique des priorités de ticket : libellés, délais cible (SLA) et styles.
export const PRIORITIES = ["BASSE", "MOYENNE", "HAUTE", "URGENTE"];

export const PRIORITY_META = {
    BASSE: {
        label: "Basse",
        description: "Peut attendre, aucune urgence.",
        slaHeures: 72,
        badge: "bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200",
        dot: "bg-emerald-500",
        accent: "border-emerald-400 bg-emerald-50 text-emerald-700"
    },
    MOYENNE: {
        label: "Moyenne",
        description: "Traitement normal.",
        slaHeures: 48,
        badge: "bg-amber-50 text-amber-700 ring-1 ring-amber-200",
        dot: "bg-amber-500",
        accent: "border-amber-400 bg-amber-50 text-amber-700"
    },
    HAUTE: {
        label: "Haute",
        description: "À traiter rapidement.",
        slaHeures: 24,
        badge: "bg-orange-50 text-orange-700 ring-1 ring-orange-200",
        dot: "bg-orange-500",
        accent: "border-orange-400 bg-orange-50 text-orange-700"
    },
    URGENTE: {
        label: "Urgente",
        description: "Bloquant, à traiter immédiatement.",
        slaHeures: 4,
        badge: "bg-red-50 text-red-700 ring-1 ring-red-200",
        dot: "bg-red-500",
        accent: "border-red-400 bg-red-50 text-red-700"
    }
};

export function getPriorityMeta(priority) {
    return PRIORITY_META[priority] || {
        label: priority || "—",
        description: "",
        slaHeures: null,
        badge: "bg-gray-100 text-gray-500 ring-1 ring-gray-200",
        dot: "bg-gray-400",
        accent: "border-gray-300 bg-gray-50 text-gray-600"
    };
}
