import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";


import { axiosApi } from "../../api/axiosApi";
import Loader from "../../components/Loader";




function EditUser() {

    const [searchParams] = useSearchParams();
    const from = searchParams.get("from") || "/admin/users";

    const { id } = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting }
    } = useForm();

    useEffect(() => {
        async function loadUser() {
            try {
                const response = await axiosApi.get(`/users/consulterUser/${id}`);
                reset(response.data);
            } catch (error) {
                toast.error("Impossible de charger l'utilisateur.");
            } finally {
                setLoading(false);
            }
        }

        loadUser();

    }, [id, reset]);

    async function onSubmit(data) {
        try {
            await axiosApi.put(`/users/modifierUser/${id}`, data);
            toast.success("Utilisateur modifié.");
            navigate(from);
        } catch (error) {
            toast.error("Erreur lors de la modification.");
        }
    }

    if (loading) {
        return <Loader />;
    }

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">
                Modifier l'utilisateur
            </h1>

            <form
                onSubmit={handleSubmit(onSubmit)}
                className="bg-white rounded-xl shadow-sm p-5 border"
            >
                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div>
                        <input
                            placeholder="Nom"
                            {...register("nom", { required: "Nom requis" })}
                            className="border rounded-lg px-3 py-2 w-full"
                        />
                        {errors.nom && (
                            <p className="text-red-500 text-xs">{errors.nom.message}</p>
                        )}
                    </div>

                    <div>
                        <input
                            placeholder="Prénom"
                            {...register("prenom", { required: "Prénom requis" })}
                            className="border rounded-lg px-3 py-2 w-full"
                        />
                        {errors.prenom && (
                            <p className="text-red-500 text-xs">{errors.prenom.message}</p>
                        )}
                    </div>

                    <div>
                        <input
                            type="email"
                            placeholder="Email"
                            {...register("email", { required: "Email requis" })}
                            className="border rounded-lg px-3 py-2 w-full"
                        />
                        {errors.email && (
                            <p className="text-red-500 text-xs">{errors.email.message}</p>
                        )}
                    </div>
                </div>

                <div className="flex gap-3 mt-4">
                    <button
                        type="submit"
                        disabled={isSubmitting}
                        className="bg-green-500 text-white px-4 py-2 rounded-lg"
                    >
                        {isSubmitting ? "Enregistrement..." : "Enregistrer"}
                    </button>

                    <button
                        type="button"
                        onClick={() => navigate(from)}
                        className="bg-gray-300 px-4 py-2 rounded-lg"
                    >
                        Annuler
                    </button>
                </div>
            </form>
        </div>
    );
}

export default EditUser;