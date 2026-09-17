import {Outlet, Navigate } from "react-router-dom";
function ProtectedRoute(){
    const userEmail = localStorage.getItem("userEmail");
    return userEmail ? <Outlet /> : <Navigate to="/login" replace />
}
export default ProtectedRoute
