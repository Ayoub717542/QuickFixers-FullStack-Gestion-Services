import { useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { axiosApi } from "../../api/axiosApi";

function UserForm({ onSuccess }) {
    const [typeCompte, setTypeCompte] = useState("USER");
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting }
    } = useForm();

    function onSubmit(data) {
        const url = typeCompte === "SUPPORT" ? "/users/Ajoutersupport" : "/users/ajouterUser";
        axiosApi.post(url, data)
            .then(() => {
                toast.success("Compte ajouté avec succès.");
                reset();
                setTypeCompte("USER");
                onSuccess();
            })
            .catch(() => {
                toast.error("Erreur lors de l'ajout du compte.");
            });
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="bg-white rounded-xl shadow-sm p-5 border mb-4">
            <h2 className="text-lg font-bold text-gray-800 mb-4">Ajouter un compte</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                <div>
                    <input
                        type="text"
                        placeholder="Nom"
                        {...register("nom", { required: "Nom requis" })}
                        className="border rounded-lg px-3 py-2 w-full"
                    />
                    {errors.nom && (<p className="text-red-500 text-xs">{errors.nom.message}</p>)}
                </div>
                <div>
                    <input
                        type="text"
                        placeholder="Prénom"
                        {...register("prenom", { required: "Prénom requis" })}
                        className="border rounded-lg px-3 py-2 w-full"
                    />
                    {errors.prenom && (<p className="text-red-500 text-xs">{errors.prenom.message}</p>)}
                </div>
                <div>
                    <input
                        type="email"
                        placeholder="Email"
                        {...register("email", { required: "Email requis" })}
                        className="border rounded-lg px-3 py-2 w-full"
                    />
                    {errors.email && (<p className="text-red-500 text-xs">{errors.email.message}</p>)}
                </div>
                <div>
                    <input
                        type="password"
                        placeholder="Mot de passe"
                        {...register("password", { required: "Mot de passe requis" })}
                        className="border rounded-lg px-3 py-2 w-full"
                    />
                    {errors.password && (<p className="text-red-500 text-xs">{errors.password.message}</p>
                    )}
                </div>

            </div>

            <div className="flex gap-3 mt-4">
                <select
                    value={typeCompte}
                    onChange={(e) => setTypeCompte(e.target.value)}
                    className="border rounded-lg px-3 py-2"
                >
                    <option value="USER">Utilisateur</option>
                    <option value="SUPPORT">Support</option>
                </select>

                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="bg-green-500 text-white px-4 py-2 rounded-lg"
                >
                    {isSubmitting ? "Ajout..." : "Ajouter"}
                </button>

            </div>
        </form>
    );
}
export default UserForm;