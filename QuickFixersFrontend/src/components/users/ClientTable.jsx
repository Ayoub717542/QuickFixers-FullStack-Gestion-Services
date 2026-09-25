import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import { axiosApi } from "../../api/axiosApi";
import Loader from "../Loader";
import Pagination from "../Pagination.jsx";

function ClientTable() {
    const [clients, setClients] = useState([]);
    const [searchedNom, setSearchedNom] = useState("");
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();
    const { register, handleSubmit } = useForm();

    async function fetchClients() {
        setLoading(true);

        try {
            let url = `/users/listerClients?pageNumber=${page}&pageSize=5`;

            if (searchedNom) {
                url = `/users/rechercherClients?nom=${encodeURIComponent(searchedNom)}&pageNumber=${page}&pageSize=5`;
            }

            const response = await axiosApi.get(url);

            setClients(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            toast.error("Impossible de charger les clients.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchClients();
    }, [page, searchedNom]);

    function rechercher(data) {
        setPage(1);
        setSearchedNom(data.nom.trim());
    }

    async function supprimerClient(client) {
        if (!window.confirm("Voulez-vous supprimer ce client ?")) {
            return;
        }

        try {
            await axiosApi.delete(`/users/supprimerUser/${client.id}`);
            toast.success("Client supprimé.");
            fetchClients();
        } catch (error) {
            toast.error("Erreur lors de la suppression.");
        }
    }

    return (
        <div className="bg-white rounded-xl shadow-sm p-4">
            <div className="flex flex-wrap gap-3 mb-4">
                <form onSubmit={handleSubmit(rechercher)} className="flex flex-wrap gap-2 w-full sm:w-auto">
                    <input
                        type="text"
                        placeholder="Rechercher par nom"
                        {...register("nom")}
                        className="border rounded-lg px-3 py-2 flex-1 min-w-[200px]"
                    />

                    <button
                        type="submit"
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg"
                    >
                        Rechercher
                    </button>
                </form>
            </div>

            {loading ? (
                <Loader />
            ) : (
                <>
                    <div className="overflow-x-auto">
                        <table className="w-full min-w-[600px]">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="p-4 text-left">ID</th>
                                    <th className="p-4 text-left">Nom complet</th>
                                    <th className="p-4 text-left">Email</th>
                                    <th className="p-4 text-right">Actions</th>
                                </tr>
                            </thead>

                            <tbody>
                                {clients.length === 0 ? (
                                    <tr>
                                        <td colSpan={4} className="p-8 text-center text-gray-500">
                                            Aucun client trouvé
                                        </td>
                                    </tr>
                                ) : (
                                    clients.map((client) => (
                                        <tr key={client.id} className="border-b">
                                            <td className="p-4">#{client.id}</td>

                                            <td className="p-4">
                                                {client.nom} {client.prenom}
                                            </td>

                                            <td className="p-4">{client.email}</td>

                                            <td className="p-4 text-right whitespace-nowrap">
                                                <button
                                                    onClick={() =>
                                                        navigate(
                                                            `/admin/users/edit/${client.id}?from=/admin/clients`
                                                        )
                                                    }
                                                    className="bg-blue-500 text-white px-3 py-1.5 rounded-lg mr-2"
                                                >
                                                    Modifier
                                                </button>

                                                <button
                                                    onClick={() => supprimerClient(client)}
                                                    className="bg-red-500 text-white px-3 py-1.5 rounded-lg"
                                                >
                                                    Supprimer
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>

                    <div className="mt-4">
                        <Pagination
                            page={page}
                            totalPages={totalPages}
                            onChange={setPage}
                        />
                    </div>
                </>
            )}
        </div>
    );
}

export default ClientTable;