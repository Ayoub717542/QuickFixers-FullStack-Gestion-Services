import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { axiosApi } from "../../api/axiosApi";
import Loader from "../Loader";
import Pagination from "../Pagination.jsx";
import { useNavigate } from "react-router-dom";

function UserTable() {
    const [users, setUsers] = useState([]);

    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);


    const navigate = useNavigate();

    function fetchUsers() {
        axiosApi.get("/users/listerUsers?pageNumber=" + page + "&pageSize=5")
            .then((response) => {
                setUsers(response.data.content);
                setTotalPages(response.data.totalPages);
            })
            .catch(() => {
                toast.error("Impossible de charger les utilisateurs.");
            })
            .finally(()=>{setLoading(false)}
            )
    }

    function changerRole(user, role) {
        axiosApi.patch(`/users/changeRole/${user.id}?role=${role}`)
            .then(() => { toast.success("Rôle modifié."); fetchUsers(); })
            .catch(() => toast.error("Erreur lors du changement de rôle."));
    }

    useEffect(() => {
        fetchUsers();
    }, [page]);

    function supprimerUser(user) {
        if (!window.confirm("Voulez-vous vraiment supprimer cet utilisateur ?")) {
            return;
        }
        axiosApi.delete("/users/supprimerUser/" + user.id)
            .then(() => {
                toast.success("Utilisateur supprimé.");
                fetchUsers();
            })
            .catch(() => {
                toast.error("Erreur lors de la suppression.");
            });
    }

    if (loading) {
        return <Loader />;
    }

    const roleColors = {
        ADMIN: "bg-red-100 text-red-600",
        SUPPORT: "bg-blue-100 text-blue-600",
        CLIENT: "bg-gray-100 text-gray-600"
    };

    return (
        <div className="bg-white rounded-xl shadow-sm overflow-hidden">
            <div className="overflow-x-auto">
                <table className="w-full">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="p-4 text-left">ID</th>
                        <th className="p-4 text-left">Nom complet</th>
                        <th className="p-4 text-left">Email</th>
                        <th className="p-4 text-left">Rôle</th>
                        <th className="p-4 text-left">Service</th>
                        <th className="p-4 text-right">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.length === 0 ? (
                        <tr>
                            <td colSpan={6} className="p-8 text-center text-gray-500">
                                Aucun utilisateur trouvé
                            </td>
                        </tr>
                    ) : (
                        users.map((user) => (
                            <tr key={user.id} className="border-b hover:bg-gray-50">
                                <td className="p-4">#{user.id}</td>
                                <td className="p-4 font-medium">{user.nom} {user.prenom}</td>
                                <td className="p-4 text-gray-500">{user.email}</td>
                                <td className="p-4">
                                    <select
                                        hidden={user.role === "ADMIN"}
                                        value={user.role}
                                        onChange={(e) => changerRole(user, e.target.value)}
                                        className="px-2 py-1 border border-gray-300 rounded-lg text-sm mr-2 bg-white"
                                    >
                                        <option value="CLIENT">CLIENT</option>
                                        <option value="SUPPORT">SUPPORT</option>
                                    </select>
                                    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${roleColors[user.role]}`}>
                                        {user.role}
                                    </span>
                                </td>
                                <td className="p-4 text-gray-500">{user.serviceType || "—"}</td>
                                <td className="p-4 text-right whitespace-nowrap g-1">
                                    <button
                                        onClick={() => supprimerUser(user)}
                                        className="px-3 py-1.5 bg-red-500 text-white rounded-lg text-sm"
                                    >
                                        Supprimer
                                    </button>

                                    <button
                                        onClick={() => navigate(`/admin/users/edit/${user.id}`)}
                                        className="px-3 py-1.5 bg-blue-500 text-white rounded-lg text-sm mr-2"
                                    >
                                        Modifier
                                    </button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
            <div className="p-4">
                <Pagination page={page} totalPages={totalPages} onChange={setPage} />
            </div>
        </div>
    );
}

export default UserTable;