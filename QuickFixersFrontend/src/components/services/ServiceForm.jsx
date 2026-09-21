import { useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
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
        <form onSubmit={handleSubmit(onSubmit)} className="bg-white rounded-xl shadow-sm p-5 border mb-4">
            <h2 className="text-lg font-bold text-gray-800 mb-4">Ajouter un service</h2>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                <div>
                    <input
                        type="text"
                        placeholder="Nom du service"
                        {...register("nom", { required: "Nom requis" })}
                        className="border rounded-lg px-3 py-2 w-full"
                    />
                    {errors.nom && <p className="text-red-500 text-xs">{errors.nom.message}</p>}
                </div>
                <div>
                    <select
                        {...register("type", { required: "Type requis" })}
                        className="border rounded-lg px-3 py-2 w-full"
                    >
                        <option value="">Choisir le type</option>
                        {TYPES.map((type) => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>
                    {errors.type && <p className="text-red-500 text-xs">{errors.type.message}</p>}
                </div>
                <div>
                    <input
                        type="number"
                        min="0"
                        step="0.01"
                        placeholder="Prix (DH)"
                        {...register("prix", { required: "Prix requis", min: { value: 0, message: "Prix invalide" } })}
                        className="border rounded-lg px-3 py-2 w-full"
                    />
                    {errors.prix && <p className="text-red-500 text-xs">{errors.prix.message}</p>}
                </div>
            </div>
            <button
                type="submit"
                disabled={isSubmitting}
                className="mt-4 bg-green-500 text-white px-4 py-2 rounded-lg disabled:opacity-50"
            >
                {isSubmitting ? "Ajout..." : "Ajouter"}
            </button>
        </form>
    );
}

export default ServiceForm;