import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import Loader from "../../components/Loader";
import { fetchMyProfile, updateMyProfile } from "../../api/userApi";

function Profile() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [editing, setEditing] = useState(false);
    const [nom, setNom] = useState("");
    const [prenom, setPrenom] = useState("");
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        loadProfile();
    }, []);

    async function loadProfile() {
        setLoading(true);
        try {
            const data = await fetchMyProfile();
            setUser(data);
            setNom(data.nom || "");
            setPrenom(data.prenom || "");
        } catch (error) {
            toast.error("Impossible de charger le profil");
        } finally {
            setLoading(false);
        }
    }

    function startEditing() {
        setNom(user.nom || "");
        setPrenom(user.prenom || "");
        setEditing(true);
    }

    async function saveProfile() {
        setSaving(true);
        try {
            const updated = await updateMyProfile({ nom, prenom });
            setUser(updated);
            setEditing(false);
            toast.success("Profil mis à jour avec succès");
        } catch (error) {
            toast.error("Erreur lors de la mise à jour du profil");
        } finally {
            setSaving(false);
        }
    }

    function initials() {
        const first = (user.prenom || "?").charAt(0).toUpperCase();
        const last = (user.nom || "?").charAt(0).toUpperCase();
        return first + last;
    }

    if (loading) {
        return <Loader />;
    }

    if (!user) {
        return <p className="p-6 text-gray-500">Profil introuvable.</p>;
    }

    return (
        <div className="p-6">
            <div className="max-w-xl mx-auto bg-white rounded-2xl shadow-md overflow-hidden">
                {/* Header */}
                <div className="bg-gradient-to-r from-orange-500 to-yellow-500 p-6 text-center text-white">
                    <div className="w-20 h-20 mx-auto rounded-full bg-white/25 flex items-center justify-center text-2xl font-bold shadow-inner">
                        {initials()}
                    </div>
                    <h1 className="text-xl font-bold mt-3">
                        {user.prenom} {user.nom}
                    </h1>
                    <span className="inline-block mt-2 px-3 py-1 rounded-full bg-white/20 text-xs font-semibold">
                        {user.role}
                    </span>
                </div>

                {/* Body */}
                <div className="p-6">
                    <dl className="space-y-3 text-sm">
                        <div className="flex justify-between border-b pb-2">
                            <dt className="text-gray-500">Nom</dt>
                            <dd className="text-gray-800 font-medium">{user.nom}</dd>
                        </div>
                        <div className="flex justify-between border-b pb-2">
                            <dt className="text-gray-500">Prénom</dt>
                            <dd className="text-gray-800 font-medium">{user.prenom}</dd>
                        </div>
                        <div className="flex justify-between border-b pb-2">
                            <dt className="text-gray-500">Email</dt>
                            <dd className="text-gray-800 font-medium">{user.email}</dd>
                        </div>
                        <div className="flex justify-between border-b pb-2">
                            <dt className="text-gray-500">Rôle</dt>
                            <dd className="text-gray-800 font-medium">{user.role}</dd>
                        </div>
                    </dl>

                    {editing ? (
                        <div className="mt-5 bg-gray-50 rounded-xl p-4">
                            <h3 className="text-sm font-semibold text-gray-700 mb-3">
                                Modifier mes informations
                            </h3>

                            <label className="block text-xs font-medium text-gray-600 mb-1">Nom</label>
                            <input
                                type="text"
                                value={nom}
                                onChange={(e) => setNom(e.target.value)}
                                className="w-full border border-gray-300 rounded-lg px-3 py-2 mb-3 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />

                            <label className="block text-xs font-medium text-gray-600 mb-1">Prénom</label>
                            <input
                                type="text"
                                value={prenom}
                                onChange={(e) => setPrenom(e.target.value)}
                                className="w-full border border-gray-300 rounded-lg px-3 py-2 mb-4 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />

                            <div className="flex gap-2">
                                <button
                                    onClick={saveProfile}
                                    disabled={saving}
                                    className="flex-1 bg-orange-500 hover:bg-orange-600 text-white text-sm font-semibold rounded-lg py-2 disabled:opacity-50"
                                >
                                    {saving ? "Enregistrement..." : "Enregistrer"}
                                </button>
                                <button
                                    onClick={() => setEditing(false)}
                                    className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-700 text-sm font-semibold rounded-lg py-2"
                                >
                                    Annuler
                                </button>
                            </div>
                        </div>
                    ) : (
                        <button
                            onClick={startEditing}
                            className="mt-5 w-full bg-orange-500 hover:bg-orange-600 text-white text-sm font-semibold rounded-lg py-2"
                        >
                            Modifier mon profil
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Profile;