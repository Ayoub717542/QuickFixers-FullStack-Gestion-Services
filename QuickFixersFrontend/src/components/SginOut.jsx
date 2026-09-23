import { LogOut } from "lucide-react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

function SginOut() {

    const navigate = useNavigate();

    function handleLogOut() {
        if (!window.confirm(`Confirmer le Deconnexion ?`)) {
            return;
        }
        localStorage.removeItem("token");
        localStorage.removeItem("userEmail");
        toast.success("Vous êtes déconnecté avec succès !");
        navigate("/login", { replace: true });
    }
    return (
        <button
            onClick={handleLogOut}
            className="flex items-center gap-3 p-3 rounded hover:bg-slate-700 w-full"
        >
            <LogOut size={20} />

            <span className="hidden group-hover:block text-sm">
                Déconnexion
            </span>
        </button>
    );
}

export default SginOut;