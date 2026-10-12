import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, Menu } from "lucide-react";
import { useUnread } from "../context/UnreadContext";
import getUserRole from "../utils/auth";

function Navbar({ onMenuClick }) {
    const [showNotifications, setShowNotifications] = useState(false);

    const { total, notifications, clearTicket, clearAll } = useUnread();
    const navigate = useNavigate();

    const userEmail = localStorage.getItem("userEmail");
    const firstLetter = userEmail ? userEmail.charAt(0).toUpperCase() : "U";
    const basePath = getUserRole() === "SUPPORT" ? "/support/tickets" : "/user/tickets";

    function openNotification(notification) {
        clearTicket(notification.ticketId);
        setShowNotifications(false);
        navigate(`${basePath}/${notification.ticketId}`);
    }

    return (
        <nav className="sticky top-0 z-10 flex h-16 items-center border-b bg-white px-6">
            <Menu size={22} onClick={onMenuClick}
                  className="lg:hidden p-2 box-content cursor-pointer text-gray-500" />

            <div className="ml-auto flex items-center gap-4">
                <div className="relative">
                    <button
                        type="button"
                        onClick={() => setShowNotifications(!showNotifications)}
                        className="relative flex cursor-pointer text-gray-500 hover:text-gray-700"
                        title="Voir les notifications"
                        aria-label="Voir les notifications"
                    >
                        <Bell size={21} />
                        {total > 0 && (
                            <span className="absolute -right-2 -top-2 flex h-4 min-w-4 items-center justify-center rounded-full bg-red-500 px-1 text-[10px] font-bold leading-none text-white">
                                {total}
                            </span>
                        )}
                    </button>

                    {showNotifications && (
                        <div className="absolute right-0 mt-3 w-72 rounded-xl border bg-white shadow-xl">
                            <div className="flex items-center justify-between border-b px-4 py-2">
                                <span className="text-sm font-semibold text-gray-700">Notifications</span>
                                {notifications.length > 0 && (
                                    <button
                                        type="button"
                                        onClick={clearAll}
                                        className="cursor-pointer text-xs text-orange-500 hover:underline"
                                    >
                                        Tout effacer
                                    </button>
                                )}
                            </div>

                            {notifications.length === 0 ? (
                                <p className="px-4 py-6 text-center text-sm text-gray-400">
                                    Aucune nouvelle notification
                                </p>
                            ) : (
                                <ul className="max-h-80 overflow-y-auto">
                                    {notifications.map((notification) => (
                                        <li key={notification.id}>
                                            <button
                                                type="button"
                                                onClick={() => openNotification(notification)}
                                                className="w-full cursor-pointer border-b px-4 py-3 text-left hover:bg-gray-50"
                                            >
                                                <p className="text-sm text-gray-800">
                                                    Nouveau message de {notification.senderNom}
                                                </p>
                                                <p className="text-xs text-gray-400">
                                                    Ticket #{notification.ticketId}
                                                </p>
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>
                    )}
                </div>

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
