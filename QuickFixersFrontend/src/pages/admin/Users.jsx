import { useState } from "react";
import UserTable from "../../components/users/UserTable";
import UserForm from "../../components/users/UserForm";

function Users() {
    const [showForm, setShowForm] = useState(false);
    return (
        <div className="p-6">
            <div className="flex items-center justify-between mb-4 flex-wrap gap-2 mb-4">
                <h1 className="text-2xl font-bold text-gray-800">Utilisateurs</h1>
                <button
                    onClick={() => setShowForm(!showForm)}
                    className="px-4 py-2 bg-orange-500 text-white rounded-lg text-sm"
                >
                    {showForm ? "Fermer" : "+ Ajouter"}
                </button>
            </div>
            {showForm && (
                <UserForm
                    onSuccess={() => {
                        setShowForm(false);
                }}
                />
            )}

            <UserTable />
        </div>
    );
}

export default Users;