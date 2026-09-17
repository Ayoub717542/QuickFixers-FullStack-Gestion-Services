import { ToastContainer } from "react-toastify";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import AdminDashboard from "./pages/admin/AdminDashboard";
import UserDashboard from "./pages/user/UserDashboard";
import SupportDashboard from "./pages/support/SupportDashboard";
import {Routes, Route } from "react-router-dom";
import "react-toastify/dist/ReactToastify.css";

function App() {
    return (
        <>
        <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/admin-dashboard" element={<AdminDashboard />} />
                <Route path="/user-dashboard" element={<UserDashboard />} />
                <Route path="/support-dashboard" element={<SupportDashboard />} />
        </Routes>

            <ToastContainer />
        </>
    );
}

export default App;