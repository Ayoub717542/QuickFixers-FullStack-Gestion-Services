function Loader() {
    return (
        <div className="flex items-center justify-center min-h-[60vh]">
            <div className="flex flex-col items-center gap-3">
                <div className="w-10 h-10 border-4 border-slate-200 border-t-orange-500 rounded-full animate-spin" />
                <p className="text-sm text-slate-500">Chargement...</p>
            </div>
        </div>
    );
}

export default Loader;