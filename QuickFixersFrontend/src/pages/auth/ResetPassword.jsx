import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { Wrench, Mail, Lock, Eye, EyeOff, ArrowRight, ArrowLeft } from "lucide-react";

function ResetPassword() {
    const navigate = useNavigate();

    const [step, setStep] = useState(1);
    const [email, setEmail] = useState("");
    const [demoCode, setDemoCode] = useState("");
    const [codeError, setCodeError] = useState(null);
    const [showPassword, setShowPassword] = useState(false);

    const { register: emailForm, handleSubmit: submitEmail, formState: { errors: emailErrors } } = useForm();
    const { register: codeForm, handleSubmit: submitCode, formState: { errors: codeErrors } } = useForm();
    const { register: passwordForm, handleSubmit: submitPassword, formState: { errors } } = useForm();

    const onEmailSubmit = (data) => {
        setEmail(data.email);
        const code = Math.floor(100000 + Math.random() * 900000).toString();
        setDemoCode(code);
        console.log("Code de réinitialisation pour", data.email, ":", code);
        toast.success(`Code envoyé à ${data.email} !`);
        setStep(2);
    };

    const onCodeSubmit = (data) => {
        setCodeError(null);
        if (data.code !== demoCode) {
            setCodeError("Code incorrect. Vérifiez votre email.");
            return;
        }
        setStep(3);
    };

    const resendCode = () => {
        const code = Math.floor(100000 + Math.random() * 900000).toString();
        setDemoCode(code);
        toast.success("Nouveau code envoyé !");
    };

    const onPasswordSubmit = () => {
        toast.success("Mot de passe mis à jour !");
        navigate("/login");
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
                <p className="text-sm">Réinitialisez votre mot de passe.</p>
            </div>

            <div className="w-full max-w-md bg-white rounded-xl shadow-lg p-6">
                <p className="text-xs text-gray-400 mb-4">Étape {step} / 3</p>

                {step === 1 && (
                    <form onSubmit={submitEmail(onEmailSubmit)}>
                        <div className="mb-5">
                            <label htmlFor="email" className="block text-xs font-semibold text-gray-700 mb-2">Adresse Email</label>
                            <div className="relative">
                                <Mail size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"/>
                                <input type="email" id="email" placeholder="nom@entreprise.com"
                                    {...emailForm("email", {
                                        required: "Email requis",
                                        pattern: {
                                            value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                                            message: "Email invalide"
                                        }
                                    })}
                                    className="w-full h-10 pl-9 pr-3 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                                />
                            </div>
                            {emailErrors.email && (
                                <p className="text-red-500 text-xs mt-1">{emailErrors.email.message}</p>
                            )}
                        </div>
                        <button
                            type="submit"
                            className="w-full h-10 flex items-center justify-center gap-2 bg-orange-500 hover:bg-orange-600 text-white text-xs font-semibold rounded-md shadow-sm transition">
                            Envoyer le code <ArrowRight size={15} />
                        </button>
                    </form>
                )}

                {step === 2 && (
                    <form onSubmit={submitCode(onCodeSubmit)}>
                        <p className="text-xs text-gray-600 mb-4">
                            Un code à 6 chiffres a été envoyé à <span className="font-semibold">{email}</span>.
                        </p>

                        <div className="mb-4 bg-amber-50 border border-amber-200 text-amber-700 text-xs rounded-md p-3">
                            Mode démo : votre code est <span className="font-bold">{demoCode}</span> (l'envoi d'email sera branché plus tard).
                        </div>

                        <div className="mb-5">
                            <label htmlFor="code" className="block text-xs font-semibold text-gray-700 mb-2">Code de vérification</label>
                            <input type="text" id="code" placeholder="123456" maxLength={6}
                                {...codeForm("code", {
                                    required: "Code requis",
                                    pattern: {
                                        value: /^\d{6}$/,
                                        message: "Le code doit contenir 6 chiffres"
                                    }
                                })}
                                className="w-full h-10 px-3 border border-orange-200 rounded-md text-sm text-center tracking-widest focus:outline-none focus:ring-2 focus:ring-orange-400"
                            />
                            {codeErrors.code && (
                                <p className="text-red-500 text-xs mt-1">{codeErrors.code.message}</p>
                            )}
                            {codeError && <p className="text-red-500 text-xs mt-1">{codeError}</p>}
                        </div>

                        <button
                            type="submit"
                            className="w-full h-10 flex items-center justify-center gap-2 bg-orange-500 hover:bg-orange-600 text-white text-xs font-semibold rounded-md shadow-sm transition">
                            Vérifier le code <ArrowRight size={15} />
                        </button>

                        <div className="flex justify-between items-center mt-4">
                            <button type="button" onClick={() => setStep(1)}
                                className="flex items-center gap-1 text-xs text-gray-500 hover:text-orange-500">
                                <ArrowLeft size={13} /> Retour
                            </button>
                            <button type="button" onClick={resendCode}
                                className="text-xs text-orange-500 hover:text-orange-600">
                                Renvoyer le code
                            </button>
                        </div>
                    </form>
                )}

                {step === 3 && (
                    <form onSubmit={submitPassword(onPasswordSubmit)}>
                        <div className="mb-4">
                            <label htmlFor="password" className="block text-xs font-semibold text-gray-700 mb-2">Nouveau mot de passe</label>
                            <div className="relative">
                                <Lock size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"/>
                                <input type={showPassword ? "text" : "password"} id="password" placeholder="Minimum 8 caractères"
                                    {...passwordForm("password", {
                                        required: "Mot de passe requis",
                                        minLength: {
                                            value: 8,
                                            message: "Le mot de passe doit contenir au moins 8 caractères"
                                        }
                                    })}
                                    className="w-full h-10 pl-9 pr-10 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    aria-label={showPassword ? "Masquer le mot de passe" : "Afficher le mot de passe"}
                                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-orange-500"
                                >
                                    {showPassword ? <EyeOff size={15}/> : <Eye size={15}/>}
                                </button>
                            </div>
                            {errors.password && <p className="text-red-500 text-xs mt-1">{errors.password.message}</p>}
                        </div>

                        <div className="mb-5">
                            <label htmlFor="confirmPassword" className="block text-xs font-semibold text-gray-700 mb-2">Confirmer le mot de passe</label>
                            <div className="relative">
                                <Lock size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"/>
                                <input type={showPassword ? "text" : "password"} id="confirmPassword" placeholder="Retapez le mot de passe"
                                    {...passwordForm("confirmPassword", {
                                        required: "Confirmation requise",
                                        validate: (value, formValues) =>
                                            value === formValues.password || "Les mots de passe ne correspondent pas"
                                    })}
                                    className="w-full h-10 pl-9 pr-10 border border-orange-200 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-orange-400"
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    aria-label={showPassword ? "Masquer le mot de passe" : "Afficher le mot de passe"}
                                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-orange-500"
                                >
                                    {showPassword ? <EyeOff size={15}/> : <Eye size={15}/>}
                                </button>
                            </div>
                            {errors.confirmPassword && <p className="text-red-500 text-xs mt-1">{errors.confirmPassword.message}</p>}
                        </div>

                        <button
                            type="submit"
                            className="w-full h-10 flex items-center justify-center gap-2 bg-orange-500 hover:bg-orange-600 text-white text-xs font-semibold rounded-md shadow-sm transition">
                            Mettre à jour <ArrowRight size={15} />
                        </button>

                        <button type="button" onClick={() => setStep(2)}
                            className="flex items-center gap-1 text-xs text-gray-500 hover:text-orange-500 mt-4">
                            <ArrowLeft size={13} /> Retour
                        </button>
                    </form>
                )}
            </div>

            <p className="text-white text-xs mt-5">Retour à la
                <button onClick={() => navigate("/login")} className="ml-1 font-semibold hover:underline">
                    page de connexion
                </button>
            </p>
        </div>
    );
}

export default ResetPassword;
