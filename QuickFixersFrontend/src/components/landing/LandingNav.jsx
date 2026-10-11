import { useState } from "react";
import { Link } from "react-router-dom";
import { Wrench, Menu, X } from "lucide-react";

const links = [
    { label: "Accueil", href: "#accueil" },
    { label: "Services", href: "#services" },
    { label: "Comment ça marche", href: "#comment-ca-marche" },
    { label: "Contact", href: "#contact" },
];

function LandingNav() {
    const [open, setOpen] = useState(false);

    return (
        <header className="sticky top-0 z-40 border-b border-orange-100 bg-white/90 backdrop-blur">
            <nav className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
                <a href="#accueil" className="flex items-center gap-2">
                    <div className="rounded-lg bg-orange-500 p-2 text-white">
                        <Wrench size={20} />
                    </div>
                    <span className="text-lg font-bold text-gray-900">QuickFixers</span>
                </a>

                <ul className="hidden items-center gap-8 md:flex">
                    {links.map((link) => (
                        <li key={link.href}>
                            <a
                                href={link.href}
                                className="text-sm font-medium text-gray-600 transition-colors hover:text-orange-500"
                            >
                                {link.label}
                            </a>
                        </li>
                    ))}
                </ul>

                <div className="hidden items-center gap-3 md:flex">
                    <Link
                        to="/login"
                        className="text-sm font-semibold text-gray-700 transition-colors hover:text-orange-500"
                    >
                        Se connecter
                    </Link>
                    <Link
                        to="/register"
                        className="rounded-md bg-orange-500 px-4 py-2 text-sm font-semibold text-white shadow-sm transition-colors hover:bg-orange-600"
                    >
                        S'inscrire
                    </Link>
                </div>

                <button
                    type="button"
                    onClick={() => setOpen((o) => !o)}
                    aria-label={open ? "Fermer le menu" : "Ouvrir le menu"}
                    className="rounded-md p-2 text-gray-600 hover:text-orange-500 md:hidden"
                >
                    {open ? <X size={22} /> : <Menu size={22} />}
                </button>
            </nav>

            {open && (
                <div className="border-t border-orange-100 bg-white px-4 pb-4 pt-2 md:hidden">
                    <ul className="flex flex-col">
                        {links.map((link) => (
                            <li key={link.href}>
                                <a
                                    href={link.href}
                                    onClick={() => setOpen(false)}
                                    className="block rounded-md px-3 py-2 text-sm font-medium text-gray-600 hover:bg-orange-50 hover:text-orange-500"
                                >
                                    {link.label}
                                </a>
                            </li>
                        ))}
                    </ul>

                    <div className="mt-3 flex flex-col gap-2 border-t border-gray-100 pt-3">
                        <Link
                            to="/login"
                            className="rounded-md border border-orange-200 px-4 py-2 text-center text-sm font-semibold text-orange-600 hover:bg-orange-50"
                        >
                            Se connecter
                        </Link>
                        <Link
                            to="/register"
                            className="rounded-md bg-orange-500 px-4 py-2 text-center text-sm font-semibold text-white hover:bg-orange-600"
                        >
                            S'inscrire
                        </Link>
                    </div>
                </div>
            )}
        </header>
    );
}

export default LandingNav;
