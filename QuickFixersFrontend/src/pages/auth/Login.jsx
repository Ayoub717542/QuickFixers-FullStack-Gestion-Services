import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { api } from "../../api/api";
import { toast } from "react-toastify";
import { getUserRole } from "../../utils/auth.js";
import { Wrench, Mail, Lock, ArrowRight } from "lucide-react";

function Login() {
    const {register, handleSubmit, formState: { errors }} = useForm();

    const navigate = useNavigate();
    const [loginError, setLoginError] = useState(null);

    const handleLogin = async (data) => {
        setLoginError(null);

        try {
            const response = await api.post("/auth/login", data);
            localStorage.setItem("token", response.data.token);
            const role = getUserRole();
            console.log("User role:", role);
            toast.success("Connexion réussie !");

            if (role === "ROLE_ADMIN") {
                navigate("/admin-dashboard");
            } else if (role === "ROLE_USER") {
                navigate("/user-dashboard");
            } else if (role === "ROLE_SUPPORT") {
                navigate("/support-dashboard");
            } else {
                setLoginError("Rôle utilisateur inconnu.");
            }

        } catch (error) {
            console.log(error);
            setLoginError("Email ou mot de passe incorrect.");
            toast.error("Échec de la connexion.");
        }
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-orange-500 via-orange-400 to-yellow-400 px-4">

            <div className="text-center mb-5 text-white">

                <div className="flex items-center justify-center gap-2 mb-2">
                    <div className="bg-white/20 p-2 rounded-lg">
                        <Wrench size={22} />
                    </div>

                    <h1 className="text-2xl font-bold">
                        QuickFixers
                    </h1>
                </div>

                <p className="text-sm">Accédez à votre espace support IT.</p>

            </div>

            <div className="w-full max-w-md bg-white rounded-xl shadow-lg p-6">
                <form onSubmit={handleSubmit(handleLogin)}>
                    <div className="mb-5">
                        <label htmlFor="userEmail" className="block text-xs font-semibold text-gray-700 mb-2">Adresse Email</label>
                        <div className="relative"><Mail size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"/>
                            <input type="email" id="userEmail" placeholder="nom@entreprise.com"
                                   {...register("userEmail", {required: "Email requis"})}
                                className="w-full h-10 pl-9 pr-3 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />
                        </div>
                        {errors.userEmail && (<p className="text-red-500 text-xs mt-1">{errors.userEmail.message}</p>)}
                    </div>
                    <div className="mb-4">
                        <div className="flex justify-between items-center mb-2">
                            <label htmlFor="password" className="text-xs font-semibold text-gray-700">Mot de passe</label>
                            <button type="button" className="text-xs text-orange-500 hover:text-orange-600">Mot de passe oublié ?</button>
                        </div>
                        <div className="relative">
                            <Lock size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"/>
                            <input type="password" id="password" placeholder="••••••••"
                                   {...register("password", {
                                    required: "Mot de passe requis"
                                })}
                                className="w-full h-10 pl-9 pr-3 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />
                        </div>
                        {errors.password && (<p className="text-red-500 text-xs mt-1">{errors.password.message}</p>)}
                    </div>
                    <div className="flex items-center gap-2 mb-5">
                        <input type="checkbox" id="remember" className="accent-orange-500"/>
                        <label htmlFor="remember" className="text-xs text-gray-600">Se souvenir de moi</label>
                    </div>
                    {loginError && (<p className="text-red-500 text-xs text-center mb-4">{loginError}</p>)}
                    <button
                        type="submit"
                        className="w-full h-10 flex items-center justify-center gap-2 bg-orange-500 hover:bg-orange-600 text-white text-xs font-semibold rounded-md shadow-sm transition"
                    >
                        Se connecter
                        <ArrowRight size={15} />
                    </button>
                </form>
            </div>

            <p className="text-white text-xs mt-5">Pas encore de compte ?
                <button
                    onClick={() => navigate("/register")}
                    className="ml-1 font-semibold hover:underline"
                >
                    S'inscrire
                </button>
            </p>

        </div>
    );
}
export default Login;