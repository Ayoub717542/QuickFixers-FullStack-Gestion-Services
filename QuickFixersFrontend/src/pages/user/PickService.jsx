import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { fetchServices } from "../../api/serviceApi";
import ServiceCard from "../../components/services/ServiceCard";
import Loader from "../../components/Loader";

function PickService() {
    const navigate = useNavigate();
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchServices(1, 100)
            .then((data) => setServices(data.content))
            .catch(() => toast.error("Impossible de charger les services."))
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return <Loader />;
    }

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold text-gray-800 mb-1">
                Nouveau ticket
            </h1>
            <p className="text-sm text-gray-500 mb-6">
                Choisissez le service qui correspond à votre problème
            </p>

            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-5 auto-rows-fr">
                {services.length === 0 ? (
                    <p className="text-gray-500 col-span-full text-center py-8">
                        Aucun service disponible
                    </p>
                ) : (
                    services.map((service) => (
                        <ServiceCard
                            key={service.id}
                            service={service}
                            onClick={() => navigate(`/user/create-ticket/${service.id}`)}
                        />
                    ))
                )}
            </div>
        </div>
    );
}

export default PickService;