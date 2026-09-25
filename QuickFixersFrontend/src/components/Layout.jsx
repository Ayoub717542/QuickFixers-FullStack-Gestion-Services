import { useState } from "react";
import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";

function Layout({ sidebar }) {
    const [open, setOpen] = useState(false);

    return (
        <div className="flex min-h-screen">
            <div className="hidden lg:block">
                {sidebar}
            </div>

            <div className="flex-1 flex flex-col min-w-0">
                <Navbar onMenuClick={() => setOpen(!open)} />
                <main className="flex-1 pt-8 p-3 bg-gray-100">
                    <Outlet />
                </main>
            </div>

            {open && (
                <>
                    <div className="fixed inset-0 bg-black/40 z-40 lg:hidden"
                         onClick={() => setOpen(false)} />
                    <div className="fixed left-2 top-16 z-50 w-64 rounded-xl overflow-hidden shadow-2xl"
                         onClick={() => setOpen(false)}>
                        {sidebar}
                    </div>
                </>
            )}
        </div>
    );
}

export default Layout;