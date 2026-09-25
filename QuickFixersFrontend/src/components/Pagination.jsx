function Pagination({ page, totalPages, onChange }) {
    if (totalPages <= 1) {
        return null;
    }
    return (
        <div className="flex flex-wrap items-center justify-between gap-2 mt-4">
            <button
                onClick={() => onChange(page - 1)}
                disabled={page === 1}
                className="px-4 py-2 bg-white border border-gray-300 rounded-lg text-sm hover:bg-gray-50 disabled:opacity-50"
            >
                Précédent
            </button>
            <span className="text-sm text-gray-600">
                Page {page} / {totalPages}
            </span>
            <button
                onClick={() => onChange(page + 1)}
                disabled={page === totalPages}
                className="px-4 py-2 bg-white border border-gray-300 rounded-lg text-sm hover:bg-gray-50 disabled:opacity-50"
            >
                Suivant
            </button>
        </div>
    );
}
export default Pagination;