import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import ServiceCard from "../../components/services/ServiceCard";
import ServiceForm from "../../components/services/ServiceForm";
import Pagination from "../../components/Pagination";
import Loader from "../../components/Loader";
import { fetchServices, deleteService, updateServiceStatut } from "../../api/serviceApi";
import { getUserRole } from "../../utils/auth";
import {useNavigate} from "react-router-dom";


function ServiceList() {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [showForm, setShowForm] = useState(false);
    const navigate = useNavigate();
    const canManage = getUserRole() === "ADMIN";
    const basePath = canManage ? "/admin/services" : "/user/services";

    function fetchList() {
        setLoading(true);
        fetchServices(page, 6)
            .then((data) => {
                setServices(data.content);
                setTotalPages(data.totalPages);
            })
            .catch(() => {
                toast.error("Impossible de charger les services.");
            })
            .finally(() => {
                setLoading(false);
            });
    }

    useEffect(() => {
        fetchList();
    }, [page]);

    function handleDelete(service) {
        if (!window.confirm("Voulez-vous vraiment supprimer ce service ?")) {
            return;
        }
        deleteService(service.id)
            .then(() => {
                toast.success("Service supprimé.");
                fetchList();
            })
            .catch(() => {
                toast.error("Erreur lors de la suppression.");
            });
    }

    function handleToggleStatut(service) {
        const newStatut = service.statut === "ACTIVE" ? "INACTIVE" : "ACTIVE";
        updateServiceStatut(service.id, newStatut)
            .then(() => {
                toast.success("Statut modifié.");
                fetchList();
            })
            .catch(() => {
                toast.error("Erreur lors du changement de statut.");
            });
    }

    if (loading) {
        return <Loader />;
    }

    return (
        <div className="p-6">
            <div className="flex items-center justify-between mb-4">
                <h1 className="text-2xl font-bold text-gray-800">Services</h1>
                {canManage && (
                    <button
                        onClick={() => setShowForm(!showForm)}
                        className="px-4 py-2 bg-orange-500 text-white rounded-lg text-sm"
                    >
                        {showForm ? "Fermer" : "+ Ajouter"}
                    </button>
                )}
            </div>
            {showForm && (
                <ServiceForm
                    onSuccess={() => {
                        setShowForm(false);
                        fetchList();
                    }}
                />
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-5 auto-rows-fr">
                {services.length === 0 ? (
                    <p className="text-gray-500 col-span-full text-center py-8">
                        Aucun service trouvé
                    </p>
                ) : (
                    services.map((service) => (
                        <ServiceCard
                            onClick={() => navigate(basePath + "/" + service.id)}
                            key={service.id}
                            service={service}
                            canManage={canManage}
                            onDelete={handleDelete}
                            onToggleStatut={handleToggleStatut}
                        />
                    ))
                )}
            </div>

            {/* Navigation entre les pages */}
            <Pagination page={page} totalPages={totalPages} onChange={setPage} />
        </div>
    );
}

export default ServiceList;