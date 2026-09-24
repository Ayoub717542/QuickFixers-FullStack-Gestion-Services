import { Link } from "react-router-dom";
import {
    LayoutDashboard,
    Ticket,
    Users,
    CreditCard,
    Wrench,
    Headset,
    User
} from "lucide-react";

function AdminSidebar({sginOut}) {
    {
        return (
            <aside
                className="group w-16 hover:w-64 sticky top-0 min-h-screen h-screen bg-slate-800 text-white p-3 transition-all duration-300">
                <div className="mb-8 flex items-center gap-3">
                    <Wrench size={24} className="text-orange-500"/>
                    <div className="hidden group-hover:block">
                        <h1 className="text-xl font-bold text-orange-500">QuickFixers</h1>
                        <p className="text-xs text-gray-400">Support Multi-Services</p>
                    </div>
                </div>

                <nav className="space-y-2">
                    <Link to="/admin/dashboard" className="flex items-center gap-3 p-3 rounded hover:bg-slate-700">
                        <LayoutDashboard size={20}/>
                        <span className="hidden group-hover:block">Dashboard</span>
                    </Link>


                    <Link
                        to="/admin/tickets"
                        className="flex items-center gap-3 p-3 rounded hover:bg-slate-700"
                    >
                        <Ticket size={20}/>

                        <span className="hidden group-hover:block">
                        Tickets
                    </span>
                    </Link>


                    <Link
                        to="/admin/users"
                        className="flex items-center gap-3 p-3 rounded hover:bg-slate-700"
                    >
                        <Users size={20}/>

                        <span className="hidden group-hover:block">
                        Utilisateurs
                    </span>
                    </Link>

                    <Link
                        to="/admin/supports"
                        className="flex items-center gap-3 p-3 rounded hover:bg-slate-700"
                    >
                        <Headset size={20}/>

                        <span className="hidden group-hover:block">
                        Supports
                    </span>
                    </Link>
                    <Link
                        to="/admin/clients"
                        className="flex items-center gap-3 p-3 rounded hover:bg-slate-700"
                    >
                        <User size={20}/>

                        <span className="hidden group-hover:block">
                        Clients
                    </span>
                    </Link>



                    <Link
                        to="/admin/payments"
                        className="flex items-center gap-3 p-3 rounded hover:bg-slate-700"
                    >
                        <CreditCard size={20}/>

                        <span className="hidden group-hover:block">
                        Paiements
                    </span>
                    </Link>
                    <Link
                        to="/admin/services"
                        className="flex items-center gap-3 p-3 rounded hover:bg-slate-700"
                    >
                        <Wrench size={20}/>
                        <span className="hidden group-hover:block">
                        Services
                    </span>
                    </Link>
                </nav>
                {sginOut}
            </aside>
        );
    }
}
export default AdminSidebar;