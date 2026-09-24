import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import { axiosApi } from "../../api/axiosApi";
import Loader from "../Loader";
import Pagination from "../Pagination.jsx";

function SupportTable() {
    const [supports, setSupports] = useState([]);
    const [email, setEmail] = useState("");
    const [searchedEmail, setSearchedEmail] = useState("");
    const [serviceType, setServiceType] = useState("");
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    async function fetchSupports() {
        setLoading(true);

        try {
            let url = `/users/listerSupports?pageNumber=${page}&pageSize=5`;

            if (searchedEmail) {
                url = `/users/rechercherSupports?searchedEmail=${encodeURIComponent(searchedEmail)}&pageNumber=${page}&pageSize=5`;
            } else if (serviceType) {
                url = `/users/filtrerSupports?serviceType=${serviceType}&pageNumber=${page}&pageSize=5`;
            }

            const response = await axiosApi.get(url);

            setSupports(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            toast.error("Impossible de charger les supports.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchSupports();
    }, [page, searchedEmail, serviceType]);

    function rechercher(event) {
        event.preventDefault();

        setServiceType("");
        setPage(1);
        setSearchedEmail(email.trim());
    }

    function filtrer(event) {
        setEmail("");
        setSearchedEmail("");
        setPage(1);
        setServiceType(event.target.value);
    }

    async function supprimerSupport(support) {
        if (!window.confirm("Voulez-vous supprimer ce support ?")) {
            return;
        }

        try {
            await axiosApi.delete(`/users/supprimerUser/${support.id}`);
            toast.success("Support supprimé.");
            fetchSupports();
        } catch (error) {
            toast.error("Erreur lors de la suppression.");
        }
    }

    return (
        <div className="bg-white rounded-xl shadow-sm p-4">
            <div className="flex flex-wrap gap-3 mb-4">
                <form onSubmit={rechercher} className="flex gap-2">
                    <input
                        type="text"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                        placeholder="Rechercher par email"
                        className="border rounded-lg px-3 py-2"
                    />

                    <button
                        type="submit"
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg"
                    >
                        Rechercher
                    </button>
                </form>

                <select
                    value={serviceType}
                    onChange={filtrer}
                    className="border rounded-lg px-3 py-2"
                >
                    <option value="">Tous les services</option>
                    <option value="REFRIGERATOR">Réfrigérateur</option>
                    <option value="WASHING_MACHINE">Machine à laver</option>
                    <option value="TELEVISION">Télévision</option>
                    <option value="AIR_CONDITIONER">Climatisation</option>
                    <option value="COMPUTER">Ordinateur</option>
                    <option value="PHONE">Téléphone</option>
                    <option value="OTHER">Autre</option>
                </select>
            </div>

            {loading ? (
                <Loader />
            ) : (
                <>
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead className="bg-gray-50">
                            <tr>
                                <th className="p-4 text-left">ID</th>
                                <th className="p-4 text-left">Nom complet</th>
                                <th className="p-4 text-left">Email</th>
                                <th className="p-4 text-left">Service</th>
                                <th className="p-4 text-right">Actions</th>
                            </tr>
                            </thead>

                            <tbody>
                            {supports.length === 0 ? (
                                <tr>
                                    <td
                                        colSpan={5}
                                        className="p-8 text-center text-gray-500"
                                    >
                                        Aucun support trouvé
                                    </td>
                                </tr>
                            ) : (
                                supports.map((support) => (
                                    <tr key={support.id} className="border-b">
                                        <td className="p-4">#{support.id}</td>

                                        <td className="p-4">
                                            {support.nom} {support.prenom}
                                        </td>

                                        <td className="p-4">{support.email}</td>

                                        <td className="p-4">
                                            {support.serviceType || "—"}
                                        </td>

                                        <td className="p-4 text-right whitespace-nowrap">
                                            <button
                                                onClick={() =>
                                                    navigate(`/admin/users/edit/${support.id}?from=/admin/supports`)
                                                }
                                                className="bg-blue-500 text-white px-3 py-1.5 rounded-lg mr-2"
                                            >
                                                Modifier
                                            </button>

                                            <button
                                                onClick={() =>
                                                    supprimerSupport(support)
                                                }
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

export default SupportTable;