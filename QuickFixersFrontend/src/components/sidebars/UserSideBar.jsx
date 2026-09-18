import { Link } from "react-router-dom";

function UserSidebar() {
    return (
        <aside className="w-64 min-h-screen bg-slate-800 text-white p-5">
            <div className="mb-8">
                <h1 className="text-2xl font-bold text-orange-500">QuickFixers</h1>
                <p className="text-sm text-gray-400">Support Multi-Services</p>
            </div>
            <nav className="space-y-2">
                <Link to="/user/dashboard" className="block p-3 rounded hover:bg-slate-700">Dashboard</Link>
                <Link to="/user/tickets" className="block p-3 rounded hover:bg-slate-700">Mes tickets</Link>
                <Link to="/user/payments" className="block p-3 rounded hover:bg-slate-700">Mes paiements</Link>
                <Link to="/user/services" className="block p-3 rounded hover:bg-slate-700">Services</Link>
                <Link to="/user/profile" className="block p-3 rounded hover:bg-slate-700">Mon profil</Link>
            </nav>

            <button className="w-full mt-8 bg-orange-500 hover:bg-orange-600 p-3 rounded">
                + Nouveau ticket
            </button>

        </aside>
    );
}

export default UserSidebar;