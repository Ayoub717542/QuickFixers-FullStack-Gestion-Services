import { Bell, Search,  } from "lucide-react";

function Navbar() {

    const userEmail = localStorage.getItem("userEmail");

    const firstLetter = userEmail ? userEmail.charAt(0).toUpperCase() : "U";

    return (
        <nav className="sticky top-0 z-10 h-16 bg-white border-b flex items-center justify-between px-6">
            <div className="flex items-center gap-2 border rounded-lg px-3 w-80">
                <Search size={18} className="text-gray-400" />
                <input
                    type="text"
                    placeholder="Rechercher..."
                    className="w-full py-2 outline-none"
                />
            </div>

            <div className="flex items-center gap-4">
                <button className="text-gray-500 hover:text-gray-700">
                    <Bell size={21} />
                </button>
                <div className="flex items-center gap-2">
                    <div className="w-9 h-9 bg-orange-500 rounded-full flex items-center justify-center text-white font-bold">
                        {firstLetter}
                    </div>
                    <div className="hidden md:block">
                        <p className="text-sm font-medium text-gray-700">
                            {userEmail}
                        </p>
                    </div>

                </div>

            </div>

        </nav>
    );
}

export default Navbar;