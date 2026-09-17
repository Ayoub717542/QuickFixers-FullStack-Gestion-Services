import { Navigate, Outlet } from "react-router-dom";
import { getUserRole } from "../utils/auth";

function RoleGuard({ allowedRoles }) {

    const role = getUserRole();

    if (!role) {
        return <Navigate to="/login" replace />;
    }

    if (!allowedRoles.includes(role)) {
        return <Navigate to="/" replace />;
    }

    return <Outlet />;
}

export default RoleGuard;

