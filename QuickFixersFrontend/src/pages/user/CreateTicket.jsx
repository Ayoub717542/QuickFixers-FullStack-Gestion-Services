import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { fetchService } from "../../api/serviceApi";
import TicketForm from "../../components/tickets/TicketForm";
import Loader from "../../components/Loader";

function CreateTicket() {
    const { serviceId } = useParams();
    const navigate = useNavigate();

    const [service, setService] = useState(null);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        fetchService(serviceId)
            .then((data) => setService(data))
            .catch(() => toast.error("Impossible de charger le service."))
            .finally(() => setLoading(false));
    }, [serviceId]);

    if (loading) {
        return <Loader />;
    }

    return (
        <div className="p-6 max-w-2xl mx-auto">
            <button
                onClick={() => navigate("/user/services")}
                className="mb-4 text-sm text-orange-600 hover:underline"
            >
                ← Retour aux services
            </button>


            {service && (
                <div className="bg-gradient-to-r from-orange-500 to-yellow-500 rounded-2xl shadow-md p-6 mb-4 text-white">
                    <p className="text-sm text-white/80">Créer un ticket pour</p>
                    <h1 className="text-xl font-bold">{service.nom}</h1>
                    <p className="text-sm text-white/90 mt-1">
                        {service.type} — {service.prix != null ? `${service.prix} DH` : "—"}
                    </p>
                </div>
            )}

            <TicketForm
                serviceId={serviceId}
                onSuccess={() => navigate("/user/tickets")}
            />
        </div>
    );
}

export default CreateTicket;