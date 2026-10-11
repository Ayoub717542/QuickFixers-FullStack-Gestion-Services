import { Link } from "react-router-dom";
import { Wrench, Mail, MapPin, Phone } from "lucide-react";

function LandingFooter() {
    return (
        <footer id="contact" className="scroll-mt-20 bg-gray-900 text-gray-300">
            <div className="mx-auto grid max-w-7xl gap-10 px-4 py-14 sm:px-6 md:grid-cols-3 lg:px-8">
                <div>
                    <div className="flex items-center gap-2 text-white">
                        <div className="rounded-lg bg-orange-500 p-2">
                            <Wrench size={20} />
                        </div>
                        <span className="text-lg font-bold">QuickFixers</span>
                    </div>
                    <p className="mt-4 max-w-xs text-sm text-gray-400">
                        La plateforme qui met en relation clients et techniciens pour réparer
                        vos appareils en toute simplicité.
                    </p>
                </div>

                <div>
                    <h3 className="text-sm font-semibold uppercase tracking-wide text-white">
                        Navigation
                    </h3>
                    <ul className="mt-4 space-y-2 text-sm">
                        <li><a href="#accueil" className="hover:text-orange-400">Accueil</a></li>
                        <li><a href="#services" className="hover:text-orange-400">Services</a></li>
                        <li><a href="#comment-ca-marche" className="hover:text-orange-400">Comment ça marche</a></li>
                        <li><Link to="/login" className="hover:text-orange-400">Se connecter</Link></li>
                        <li><Link to="/register" className="hover:text-orange-400">S'inscrire</Link></li>
                    </ul>
                </div>

                <div>
                    <h3 className="text-sm font-semibold uppercase tracking-wide text-white">
                        Contact
                    </h3>
                    <ul className="mt-4 space-y-3 text-sm">
                        <li className="flex items-center gap-2">
                            <Mail size={16} className="text-orange-400" />
                            contact@quickfixers.com
                        </li>
                        <li className="flex items-center gap-2">
                            <Phone size={16} className="text-orange-400" />
                            +212 5 00 00 00 00
                        </li>
                        <li className="flex items-center gap-2">
                            <MapPin size={16} className="text-orange-400" />
                            Casablanca, Maroc
                        </li>
                    </ul>
                </div>
            </div>

            <div className="border-t border-white/10 py-5">
                <p className="text-center text-xs text-gray-500">
                    © {new Date().getFullYear()} QuickFixers. Tous droits réservés.
                </p>
            </div>
        </footer>
    );
}

export default LandingFooter;
