import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { api } from "../../api/api";
import { toast } from "react-toastify";
import { Wrench, User, Mail, Lock, ArrowRight } from "lucide-react";

function Register() {
    const {register, handleSubmit, formState: { errors }} = useForm();
    const navigate = useNavigate();
    const [registerError, setRegisterError] = useState(null);

    const handleRegister = async (data) => {
        setRegisterError(null);
        try {
            await api.post("/auth/register", data);
            toast.success("Inscription réussie !");
            navigate("/login");
        } catch (error) {
            console.log(error);
            setRegisterError("Erreur lors de l'inscription.");
            toast.error("Échec de l'inscription.");
        }
    };
    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-orange-500 via-orange-400 to-yellow-400 px-4">
            <div className="text-center mb-5 text-white">
                <div className="flex items-center justify-center gap-2 mb-2">
                    <div className="bg-white/20 p-2 rounded-lg">
                        <Wrench size={22} />
                    </div>
                    <h1 className="text-2xl font-bold">QuickFixers</h1>
                </div>
                <p className="text-sm">Créez votre compte QuickFixers.</p>
            </div>

            <div className="w-full max-w-md bg-white rounded-xl shadow-lg p-6">

                <form onSubmit={handleSubmit(handleRegister)}>

                    <div className="mb-4">
                        <label htmlFor="nom" className="block text-xs font-semibold text-gray-700 mb-2">Nom</label>
                        <div className="relative">
                            <User size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"/>
                            <input type="text" id="nom" placeholder="Votre nom"
                                {...register("nom", {
                                    required: "Nom requis"
                                })}
                                className="w-full h-10 pl-9 pr-3 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />
                        </div>
                        {errors.nom && (<p className="text-red-500 text-xs mt-1">{errors.nom.message}</p>)}
                    </div>
                    <div className="mb-4">
                        <label htmlFor="prenom" className="block text-xs font-semibold text-gray-700 mb-2">Prénom</label>

                        <div className="relative">
                            <User size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"/>
                            <input type="text" id="prenom" placeholder="Votre prénom"
                                {...register("prenom", {
                                    required: "Prénom requis"
                                })}
                                className="w-full h-10 pl-9 pr-3 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />
                        </div>
                        {errors.prenom && (<p className="text-red-500 text-xs mt-1">{errors.prenom.message}</p>)}
                    </div>

                    {/* Email */}
                    <div className="mb-4">

                        <label
                            htmlFor="email"
                            className="block text-xs font-semibold text-gray-700 mb-2"
                        >
                            Adresse Email
                        </label>

                        <div className="relative">

                            <Mail
                                size={15}
                                className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
                            />

                            <input
                                type="email"
                                id="email"
                                placeholder="nom@entreprise.com"
                                {...register("email", {
                                    required: "Email requis",
                                    pattern: {
                                        value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                                        message: "Email invalide"
                                    }
                                })}
                                className="w-full h-10 pl-9 pr-3 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />

                        </div>

                        {errors.email && (
                            <p className="text-red-500 text-xs mt-1">
                                {errors.email.message}
                            </p>
                        )}

                    </div>


                    {/* Password */}
                    <div className="mb-5">

                        <label
                            htmlFor="password"
                            className="block text-xs font-semibold text-gray-700 mb-2"
                        >
                            Mot de passe
                        </label>

                        <div className="relative">

                            <Lock
                                size={15}
                                className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
                            />

                            <input
                                type="password"
                                id="password"
                                placeholder="Minimum 6 caractères"
                                {...register("password", {
                                    required: "Mot de passe requis",
                                })}
                                className="w-full h-10 pl-9 pr-3 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />

                        </div>

                        {errors.password && (
                            <p className="text-red-500 text-xs mt-1">
                                {errors.password.message}
                            </p>
                        )}

                    </div>
                    {registerError && (<p className="text-red-500 text-xs text-center mb-4">{registerError}</p>)}
                    <button
                        type="submit"
                        className="w-full h-10 flex items-center justify-center gap-2 bg-orange-500 hover:bg-orange-600 text-white text-xs font-semibold rounded-md shadow-sm transition"
                    >
                        S'inscrire<ArrowRight size={15} />
                    </button>
                </form>
            </div>
            <p className="text-white text-xs mt-5">
                Vous avez déjà un compte ?
                <button
                    onClick={() => navigate("/login")}
                    className="ml-1 font-semibold hover:underline"
                >
                    Se connecter
                </button>
            </p>
        </div>
    );
}

export default Register;