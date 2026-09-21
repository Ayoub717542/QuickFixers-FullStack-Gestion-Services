import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { Wrench, ArrowLeft } from "lucide-react";

import Loader from "../../components/Loader";
import { fetchService, updateServiceStatut } from "../../api/serviceApi";
import { getUserRole } from "../../utils/auth";

function ServiceDetails() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [service, setService] = useState(null);
    const [loading, setLoading] = useState(true);

    const isAdmin = getUserRole() === "ADMIN";

    async function loadService() {
        try {
            const data = await fetchService(id);
            setService(data);
        } catch (error) {
            toast.error("Impossible de charger le service.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadService();
    }, [id]);

    async function changeStatut(statut) {
        if (statut === service.statut) return;

        try {
            const updatedService = await updateServiceStatut(
                service.id,
                statut
            );

            setService(updatedService);
            toast.success("Statut mis à jour.");
        } catch (error) {
            toast.error("Erreur lors de la modification.");
        }
    }

    if (loading) {
        return <Loader />;
    }

    if (!service) {
        return (
            <div className="p-6 text-center">
                <p className="text-gray-600">Service introuvable.</p>

                <button
                    onClick={() => navigate(-1)}
                    className="mt-4 text-orange-600 hover:underline"
                >
                    ← Retour
                </button>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 p-6">
            <button
                onClick={() => navigate(-1)}
                className="flex items-center gap-2 mb-6 text-sm text-gray-600 hover:text-orange-600"
            >
                <ArrowLeft size={18} />
                Retour aux services
            </button>

            <div className="max-w-3xl mx-auto bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">

                <div className="bg-gradient-to-r from-orange-500 to-orange-400 p-7 text-white">

                    <div className="flex items-center gap-4">

                        <div className="w-14 h-14 rounded-xl bg-white/20 flex items-center justify-center">
                            <Wrench size={28} />
                        </div>

                        <div>
                            <p className="text-sm text-orange-100">
                                Service
                            </p>

                            <h1 className="text-2xl font-bold">
                                {service.nom}
                            </h1>

                            <p className="text-sm text-orange-100 mt-1">
                                {service.type}
                            </p>
                        </div>

                    </div>
                </div>

                <div className="p-7">

                    <h2 className="text-lg font-semibold text-gray-800 mb-5">
                        Informations du service
                    </h2>

                    <div className="space-y-4">

                        <div className="flex justify-between items-center border-b pb-4">
                            <span className="text-sm text-gray-500">
                                Statut
                            </span>

                            <span
                                className={`px-3 py-1 rounded-full text-xs font-medium ${
                                    service.statut === "ACTIVE"
                                        ? "bg-green-100 text-green-700"
                                        : "bg-red-100 text-red-700"
                                }`}
                            >
                                {service.statut}
                            </span>
                        </div>

                        <div className="flex justify-between border-b pb-4">
                            <span className="text-sm text-gray-500">
                                Type
                            </span>

                            <span className="text-sm font-medium text-gray-800">
                                {service.type}
                            </span>
                        </div>

                        <div className="flex justify-between">
                            <span className="text-sm text-gray-500">
                                Prix
                            </span>

                            <span className="text-sm font-semibold text-gray-800">
                                {service.prix != null
                                    ? `${service.prix} DH`
                                    : "Prix négociable"}
                            </span>
                        </div>

                    </div>
                    {isAdmin && (
                        <div className="mt-8 pt-6 border-t">

                            <h3 className="text-sm font-semibold text-gray-800 mb-3">
                                Gestion du service
                            </h3>

                            <label className="block text-sm text-gray-500 mb-2">
                                Modifier le statut
                            </label>

                            <select
                                value={service.statut}
                                onChange={(e) =>
                                    changeStatut(e.target.value)
                                }
                                className="w-full border border-gray-300 rounded-lg px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            >
                                <option value="ACTIVE">
                                    ACTIVE
                                </option>

                                <option value="INACTIVE">
                                    INACTIVE
                                </option>
                            </select>

                        </div>
                    )}

                </div>
            </div>
        </div>
    );
}

export default ServiceDetails;