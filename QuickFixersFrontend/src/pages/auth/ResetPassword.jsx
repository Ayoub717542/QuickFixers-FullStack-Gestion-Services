import { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { axiosApi } from "../../api/axiosApi";

function ResetPassword(){
    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");
    const navigate = useNavigate();
    const [password, setPassword] = useState("");
    const [confirm, setConfirm] = useState("");
    const [loading, setLoading] = useState(false);

    async function onSubmit(e) {
        e.preventDefault();

        if (password.length < 8) {
            toast.error("Le mot de passe doit contenir au moins 8 caractères.");
            return;
        }
        if (password !== confirm) {
            toast.error("Les deux mots de passe ne correspondent pas.");
            return;
        }

        setLoading(true);
        try {
            await axiosApi.post("/auth/reset-password", { token, newPassword: password });
            toast.success("Mot de passe mis à jour ! Connectez-vous.");
            navigate("/login");
        } catch {
            toast.error("Lien invalide ou expiré.");
        } finally {
            setLoading(false);
        }
    }
    return (<>
        <div className="min-h-screen flex items-center justify-center bg-gray-100">                                                             72 096 tokens
    <form onSubmit={onSubmit} className="bg-white rounded-xl shadow-sm p-6 w-full max-w-md">                                            36% used
        <h2 className="text-xl font-bold text-gray-800 mb-4 text-center">Choisir un mot de passe</h2>
    <input
        type="password"
        placeholder="Nouveau mot de passe (min 8 caractères)"
        className="border rounded-lg px-3 py-2 w-full mb-3"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
    />
    <input
        type="password"
        placeholder="Confirmer le mot de passe"
        className="border rounded-lg px-3 py-2 w-full mb-3"
        value={confirm}
        onChange={(e) => setConfirm(e.target.value)}
    />
    <button
        type="submit"
        disabled={loading}
        className="bg-orange-500 text-white px-4 py-2 rounded-lg w-full"
    >
        {loading ? "Enregistrement..." : "Valider le mot de passe"}
    </button>
</form>
</div>

        </>
);
}
export default ResetPassword;