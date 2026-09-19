import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";

function Layout({ sidebar }) {
    return (
        <div className="flex min-h-screen">

            {sidebar}

            <div className="flex-1">

                <Navbar />

                <main className="pt-8 p-3 bg-gray-100 min-h-screen">
                    <Outlet />
                </main>

            </div>

        </div>
    );
}

export default Layout;