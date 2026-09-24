import { useState } from "react";
import SupportTable from "../../components/users/SupportTable";
import UserForm from "../../components/users/UserForm";

function Supports() {
    const [showForm, setShowForm] = useState(false);
    return (
        <div className="p-6">
            <div className="flex items-center justify-between mb-4">
                <h1 className="text-2xl font-bold text-gray-800">Supports</h1>
                <button onClick={() => setShowForm(!showForm)}
                        className="px-4 py-2 bg-orange-500 text-white rounded-lg text-sm">
                    {showForm ? "Fermer" : "+ Ajouter"}
                </button>
            </div>
            {showForm && <UserForm fixedType="SUPPORT" onSuccess={() => setShowForm(false)} />}
            <SupportTable />
        </div>
    );
}
export default Supports;