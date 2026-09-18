import { Link } from "react-router-dom";
function AdminSidebar() {
    return (
        <aside className="w-64 min-h-screen bg-slate-800 text-white p-5">

            <div className="mb-8">
                <h1 className="text-2xl font-bold text-orange-500">QuickFixers</h1>
                <p className="text-sm text-gray-400">Support Multi-Services</p>
            </div>
            <nav className="space-y-2">

                <Link to="/admin/dashboard" className="block p-3 rounded hover:bg-slate-700">Dashboard</Link>
                <Link to="/admin/tickets" className="block p-3 rounded hover:bg-slate-700">Tickets</Link>
                <Link to="/admin/users" className="block p-3 rounded hover:bg-slate-700">Utilisateurs</Link>
                <Link to="/admin/payments" className="block p-3 rounded hover:bg-slate-700">Paiements</Link>
                <Link to="/admin/services" className="block p-3 rounded hover:bg-slate-700">Services</Link>

            </nav>

        </aside>
    )
}
export default AdminSidebar