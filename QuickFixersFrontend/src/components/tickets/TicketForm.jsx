import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { Ticket } from "lucide-react";
import { createTicket } from "../../api/ticketApi";
import { PRIORITIES, getPriorityMeta } from "../../utils/priority";
import Spinner from "../Spinner.jsx";

function TicketForm({ serviceId, onSuccess }) {
    const {
        register,
        handleSubmit,
        reset,
        watch,
        formState: { errors, isSubmitting }
    } = useForm({ defaultValues: { priority: "MOYENNE" } });

    const selectedPriority = watch("priority");

    async function onSubmit(data) {
        try {
            await createTicket(serviceId, data);
            toast.success("Ticket créé avec succès.");
            reset();
            onSuccess();
        } catch {
            toast.error("Erreur lors de la création du ticket.");
        }
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
                    {...register("titre", {
                        required: "Titre requis"
                    })}
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

            <div className="mb-4">
                <label className="block text-sm font-semibold text-gray-700 mb-2">
                    Priorité
                </label>
                <div className="grid grid-cols-2 gap-2 sm:grid-cols-4">
                    {PRIORITIES.map((priority) => {
                        const meta = getPriorityMeta(priority);
                        const active = selectedPriority === priority;
                        return (
                            <label
                                key={priority}
                                className={`cursor-pointer rounded-xl border-2 px-3 py-2 text-center transition-colors ${
                                    active ? meta.accent : "border-gray-200 text-gray-500 hover:border-gray-300"
                                }`}
                            >
                                <input
                                    type="radio"
                                    value={priority}
                                    className="sr-only"
                                    {...register("priority", { required: "Priorité requise" })}
                                />
                                <span className="block text-sm font-semibold">{meta.label}</span>
                                <span className="mt-0.5 block text-[11px] opacity-80">
                                    {meta.slaHeures}h
                                </span>
                            </label>
                        );
                    })}
                </div>
                {errors.priority && (
                    <p className="text-red-500 text-xs mt-1">{errors.priority.message}</p>
                )}
                <p className="text-xs text-gray-400 mt-2">
                    Le délai indiqué est le temps de traitement cible selon la priorité.
                </p>
            </div>

            <div className="flex justify-end">
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="px-5 py-2 bg-orange-500 text-white rounded-lg text-sm font-medium hover:bg-orange-600 disabled:opacity-50 disabled:cursor-not-allowed transition-colors flex items-center gap-2"
                >
                    {isSubmitting && <Spinner />}
                    {isSubmitting ? "Création..." : "Créer le ticket"}
                </button>
            </div>
        </form>
    );
}

export default TicketForm;
