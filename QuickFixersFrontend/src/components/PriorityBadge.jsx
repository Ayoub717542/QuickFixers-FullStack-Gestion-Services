import { ArrowDown, Minus, ArrowUp, Flame } from "lucide-react";
import { getPriorityMeta } from "../utils/priority";

// Icône associée à chaque niveau de priorité.
const ICONS = {
    BASSE: ArrowDown,
    MOYENNE: Minus,
    HAUTE: ArrowUp,
    URGENTE: Flame
};

// Badge de priorité : chaque niveau a sa propre couleur et son icône.
// La priorité URGENTE pulse pour attirer l'attention.
function PriorityBadge({ priority, className = "" }) {
    const meta = getPriorityMeta(priority);
    const Icon = ICONS[priority];
    const urgent = priority === "URGENTE";

    return (
        <span
            className={`inline-flex items-center gap-1.5 rounded-full px-2.5 py-1 text-xs font-semibold ${meta.badge} ${className}`}
        >
            {Icon ? <Icon size={13} className={urgent ? "animate-pulse" : ""} /> : null}
            {meta.label}
        </span>
    );
}

export default PriorityBadge;
