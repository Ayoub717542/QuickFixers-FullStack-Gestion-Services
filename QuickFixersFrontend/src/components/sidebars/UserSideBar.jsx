import { Link } from "react-router-dom";
import {
    LayoutDashboard,
    Ticket,
    CreditCard,
    Wrench,
    User
} from "lucide-react";

function UserSidebar({ sginOut }) {

    return (
        <aside
            className="group w-64 lg:w-16 lg:hover:w-64 lg:sticky lg:top-0 lg:h-screen bg-slate-800 text-white p-3 transition-all duration-300">
            <div className="mb-8 flex items-center gap-3">
                <Wrench size={24} className="text-orange-500" />
                <div className="block lg:hidden lg:group-hover:block">
                    <h1 className="text-xl font-bold text-orange-500">QuickFixers</h1>
                    <p className="text-xs text-gray-400">Support Multi-Services</p>
                </div>
            </div>

            <nav className="space-y-2">
                <Link to="/user/dashboard" className="flex items-center gap-3 p-3 rounded hover:bg-slate-700">
                    <LayoutDashboard size={20} />
                    <span className="block lg:hidden lg:group-hover:block">
                        Dashboard
                    </span>
                </Link>

                <Link to="/user/tickets" className="flex items-center gap-3 p-3 rounded hover:bg-slate-700">
                    <Ticket size={20} />
                    <span className="block lg:hidden lg:group-hover:block">Mes tickets</span>
                </Link>

                <Link to="/user/payments" className="flex items-center gap-3 p-3 rounded hover:bg-slate-700">
                    <CreditCard size={20} />
                    <span className="block lg:hidden lg:group-hover:block">Mes paiements</span>
                </Link>

                <Link to="/user/services" className="flex items-center gap-3 p-3 rounded hover:bg-slate-700">
                    <Wrench size={20} />
                    <span className="block lg:hidden lg:group-hover:block">Services</span>
                </Link>

                <Link to="/user/profile"
                      className="flex items-center gap-3 p-3 rounded hover:bg-slate-700">
                    <User size={20} />
                    <span className="block lg:hidden lg:group-hover:block">Mon profil</span>
                </Link>
            </nav>

            <Link to="/user/tickets/create"
                  className="flex items-center gap-3 w-full mt-8 bg-orange-500 hover:bg-orange-600 p-3 rounded">
                <Ticket size={20} />
                <span className="block lg:hidden lg:group-hover:block whitespace-nowrap">
                    Nouveau ticket
                </span>
            </Link>

            <div className="mt-4">{sginOut}</div>

        </aside>
    );
}

export default UserSidebar;