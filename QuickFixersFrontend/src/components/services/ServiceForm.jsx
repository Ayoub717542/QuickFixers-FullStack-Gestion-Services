import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { Plus } from "lucide-react";
import { createService } from "../../api/serviceApi";

const TYPES = ["ELECTROMENAGER", "ELECTRONIQUE", "INFORMATIQUE", "TELEPHONIE"];

function ServiceForm({ onSuccess }) {
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting }
    } = useForm();

    function onSubmit(data) {
        createService(data)
            .then(() => {
                toast.success("Service ajouté avec succès.");
                reset();
                onSuccess();
            })
            .catch(() => {
                toast.error("Erreur lors de l'ajout du service.");
            });
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="bg-white rounded-2xl shadow-sm p-5 mb-4">
            <div className="flex items-center gap-3 mb-4">
                <div className="w-9 h-9 rounded-xl bg-orange-50 flex items-center justify-center">
                    <Plus size={18} className="text-orange-500" />
                </div>
                <h2 className="text-lg font-bold text-gray-900">Ajouter un service</h2>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                <div>
                    <input
                        type="text"
                        placeholder="Nom du service"
                        {...register("nom", { required: "Nom requis" })}
                        className="border border-gray-200 rounded-lg px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-orange-400"
                    />
                    {errors.nom && <p className="text-red-500 text-xs mt-1">{errors.nom.message}</p>}
                </div>
                <div>
                    <select
                        {...register("type", { required: "Type requis" })}
                        className="border border-gray-200 rounded-lg px-3 py-2 w-full bg-white focus:outline-none focus:ring-2 focus:ring-orange-400"
                    >
                        <option value="">Choisir le type</option>
                        {TYPES.map((type) => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>
                    {errors.type && <p className="text-red-500 text-xs mt-1">{errors.type.message}</p>}
                </div>
                <div>
                    <input
                        type="number"
                        min="0"
                        step="0.01"
                        placeholder="Prix (DH)"
                        {...register("prix", { required: "Prix requis", min: { value: 0, message: "Prix invalide" } })}
                        className="border border-gray-200 rounded-lg px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-orange-400"
                    />
                    {errors.prix && <p className="text-red-500 text-xs mt-1">{errors.prix.message}</p>}
                </div>
            </div>
            <div className="flex justify-end mt-4">
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="px-5 py-2 bg-orange-500 text-white rounded-lg text-sm font-medium hover:bg-orange-600 disabled:opacity-50 transition-colors"
                >
                    {isSubmitting ? "Ajout..." : "Ajouter"}
                </button>
            </div>
        </form>
    );
}

export default ServiceForm;