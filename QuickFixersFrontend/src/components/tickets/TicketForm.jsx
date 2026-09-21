import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { Ticket } from "lucide-react";
import { createTicket } from "../../api/ticketApi";

function TicketForm({ serviceId, onSuccess }) {
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting }
    } = useForm();

    function onSubmit(data) {
        createTicket(serviceId, data)
            .then(() => {
                toast.success("Ticket créé avec succès.");
                reset();
                onSuccess();
            })
            .catch(() => {
                toast.error("Erreur lors de la création du ticket.");
            });
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="bg-white rounded-2xl shadow-sm p-5 mb-4">
            <div className="flex items-center gap-3 mb-4">
                <div className="w-9 h-9 rounded-xl bg-orange-50 flex items-center justify-center">
                    <Ticket size={18} className="text-orange-500" />
                </div>
                <h2 className="text-lg font-bold text-gray-900">Créer un ticket</h2>
            </div>

            <div className="mb-3">
                <input
                    type="text"
                    placeholder="Titre du ticket"
                    {...register("titre", { required: "Titre requis" })}
                    className="border border-gray-200 rounded-lg px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-orange-400"
                />
                {errors.titre && (
                    <p className="text-red-500 text-xs mt-1">{errors.titre.message}</p>
                )}
            </div>

            <div className="mb-4">
                <textarea
                    rows="4"
                    placeholder="Décrivez votre problème en détail..."
                    {...register("description", { required: "Description requise" })}
                    className="border border-gray-200 rounded-lg px-3 py-2 w-full resize-none focus:outline-none focus:ring-2 focus:ring-orange-400"
                />
                {errors.description && (
                    <p className="text-red-500 text-xs mt-1">{errors.description.message}</p>
                )}
            </div>

            <div className="flex justify-end">
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="px-5 py-2 bg-orange-500 text-white rounded-lg text-sm font-medium hover:bg-orange-600 disabled:opacity-50 transition-colors"
                >
                    {isSubmitting ? "Création..." : "Créer le ticket"}
                </button>
            </div>
        </form>
    );
}

export default TicketForm;