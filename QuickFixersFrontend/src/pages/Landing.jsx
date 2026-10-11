import { Link } from "react-router-dom";
import {
    Cpu,
    Refrigerator,
    Laptop,
    Smartphone,
    UserPlus,
    ClipboardList,
    Wrench,
    CreditCard,
    ShieldCheck,
    Clock,
    Headphones,
    ArrowRight,
    CheckCircle2,
} from "lucide-react";
import heroBg from "../assets/Atelier de réparation électronique.png";
import LandingNav from "../components/landing/LandingNav.jsx";
import LandingFooter from "../components/landing/LandingFooter.jsx";
import ChatBotButton from "../components/landing/ChatBotButton.jsx";

const services = [
    {
        icon: Cpu,
        title: "Électronique",
        description: "Diagnostic et réparation de vos appareils électroniques.",
    },
    {
        icon: Refrigerator,
        title: "Électroménager",
        description: "Dépannage de vos appareils ménagers : lave-linge, réfrigérateur, etc.",
    },
    {
        icon: Laptop,
        title: "Informatique",
        description: "Ordinateurs, logiciels, réseaux et tout votre matériel informatique.",
    },
    {
        icon: Smartphone,
        title: "Téléphonie",
        description: "Réparation de smartphones, tablettes et appareils de téléphonie.",
    },
];

const steps = [
    {
        icon: UserPlus,
        title: "Créez votre compte",
        description: "Inscrivez-vous gratuitement en quelques secondes.",
    },
    {
        icon: ClipboardList,
        title: "Choisissez un service",
        description: "Sélectionnez le type de réparation et créez un ticket.",
    },
    {
        icon: Wrench,
        title: "Un technicien répare",
        description: "Suivez l'avancement de votre réparation en temps réel.",
    },
    {
        icon: CreditCard,
        title: "Payez en ligne",
        description: "Réglez et téléchargez votre reçu une fois la réparation terminée.",
    },
];

const highlights = [
    { icon: ShieldCheck, label: "Techniciens qualifiés" },
    { icon: Clock, label: "Suivi en temps réel" },
    { icon: Headphones, label: "Support réactif" },
];

function Landing() {
    return (
        <div className="min-h-screen bg-white">
            <LandingNav />

            <main>
                {/* Hero */}
                <section
                    id="accueil"
                    className="relative flex min-h-[80vh] scroll-mt-20 items-center bg-cover bg-center"
                    style={{ backgroundImage: `url(${heroBg})` }}
                >
                    <div className="absolute inset-0 bg-black/40" />

                    <div className="relative mx-auto w-full max-w-7xl px-4 py-20 text-center sm:px-6 lg:px-8">
                        <span className="inline-block rounded-full bg-white/20 px-4 py-1.5 text-xs font-semibold text-white">
                            Plateforme de dépannage
                        </span>

                        <h1 className="mx-auto mt-6 max-w-3xl text-3xl font-extrabold leading-tight text-white sm:text-4xl lg:text-5xl">
                            Faites réparer vos appareils en toute simplicité.
                        </h1>

                        <p className="mx-auto mt-5 max-w-2xl text-base text-white/90 sm:text-lg">
                            QuickFixers met en relation les clients avec des techniciens
                            qualifiés. Créez un ticket, suivez la réparation et payez en ligne.
                        </p>

                        <div className="mt-9 flex flex-col items-center justify-center gap-3 sm:flex-row">
                            <Link
                                to="/register"
                                className="inline-flex w-full items-center justify-center gap-2 rounded-md bg-white px-6 py-3 text-sm font-semibold text-orange-600 shadow-sm transition hover:bg-orange-50 sm:w-auto"
                            >
                                Créer un compte
                                <ArrowRight size={18} />
                            </Link>
                            <Link
                                to="/login"
                                className="inline-flex w-full items-center justify-center gap-2 rounded-md border border-white/60 px-6 py-3 text-sm font-semibold text-white transition hover:bg-white/10 sm:w-auto"
                            >
                                Se connecter
                            </Link>
                        </div>

                        <ul className="mx-auto mt-12 flex max-w-3xl flex-col items-center justify-center gap-3 sm:flex-row sm:gap-6">
                            {highlights.map(({ icon: Icon, label }) => (
                                <li
                                    key={label}
                                    className="flex items-center gap-2 text-sm font-medium text-white"
                                >
                                    <Icon size={18} />
                                    {label}
                                </li>
                            ))}
                        </ul>
                    </div>
                </section>

                {/* Services */}
                <section id="services" className="scroll-mt-20 bg-gray-50 py-20">
                    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                        <div className="mx-auto max-w-2xl text-center">
                            <h2 className="text-2xl font-bold text-gray-900 sm:text-3xl">
                                Nos services
                            </h2>
                            <p className="mt-3 text-gray-500">
                                Des techniciens spécialisés pour chaque type d'appareil.
                            </p>
                        </div>

                        <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
                            {services.map(({ icon: Icon, title, description }) => (
                                <div
                                    key={title}
                                    className="rounded-2xl bg-white p-6 shadow-sm transition-shadow hover:shadow-lg"
                                >
                                    <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-orange-50">
                                        <Icon size={22} className="text-orange-500" />
                                    </div>
                                    <h3 className="mt-4 font-bold text-gray-900">{title}</h3>
                                    <p className="mt-2 text-sm text-gray-500">{description}</p>
                                </div>
                            ))}
                        </div>
                    </div>
                </section>

                {/* How it works */}
                <section id="comment-ca-marche" className="scroll-mt-20 py-20">
                    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                        <div className="mx-auto max-w-2xl text-center">
                            <h2 className="text-2xl font-bold text-gray-900 sm:text-3xl">
                                Comment ça marche
                            </h2>
                            <p className="mt-3 text-gray-500">
                                Quatre étapes simples, de la demande à la réparation.
                            </p>
                        </div>

                        <div className="mt-12 grid gap-8 sm:grid-cols-2 lg:grid-cols-4">
                            {steps.map(({ icon: Icon, title, description }, index) => (
                                <div key={title} className="relative text-center">
                                    <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-full bg-orange-500 text-white shadow-md">
                                        <Icon size={24} />
                                    </div>
                                    <span className="mt-4 block text-xs font-bold uppercase tracking-wide text-orange-500">
                                        Étape {index + 1}
                                    </span>
                                    <h3 className="mt-1 font-bold text-gray-900">{title}</h3>
                                    <p className="mt-2 text-sm text-gray-500">{description}</p>
                                </div>
                            ))}
                        </div>
                    </div>
                </section>

                {/* CTA */}
                <section className="px-4 pb-20 sm:px-6 lg:px-8">
                    <div className="mx-auto max-w-7xl overflow-hidden rounded-3xl bg-gradient-to-br from-orange-500 via-orange-400 to-yellow-400 px-6 py-14 text-center shadow-lg sm:px-12">
                        <h2 className="text-2xl font-bold text-white sm:text-3xl">
                            Prêt à faire réparer votre appareil ?
                        </h2>
                        <p className="mx-auto mt-3 max-w-xl text-white/90">
                            Rejoignez QuickFixers et suivez votre réparation du début à la fin.
                        </p>
                        <Link
                            to="/register"
                            className="mt-8 inline-flex items-center justify-center gap-2 rounded-md bg-white px-6 py-3 text-sm font-semibold text-orange-600 shadow-sm transition hover:bg-orange-50"
                        >
                            <CheckCircle2 size={18} />
                            Commencer maintenant
                        </Link>
                    </div>
                </section>
            </main>

            <LandingFooter />
            <ChatBotButton />
        </div>
    );
}

export default Landing;
