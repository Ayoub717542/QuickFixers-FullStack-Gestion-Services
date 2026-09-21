import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";

function Layout({ sidebar }) {
    return (
        <div className="flex min-h-screen">

            {sidebar}

            <div className="flex-1 flex flex-col">

                <Navbar />

                <main className="flex-1 pt-8 p-3 bg-gray-100">
                    <Outlet />
                </main>

            </div>

        </div>
    );
}

export default Layout;