import { ToastContainer } from "react-toastify";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import AdminDashboard from "./pages/admin/AdminDashboard";
import Layout from "./components/Layout";
import UserDashboard from "./pages/user/UserDashboard";
import Tickets from "./pages/admin/Tickets.jsx";
import Services from "./pages/admin/Services.jsx";
import ProtectedRoute from "./routes/ProtectedRoute";

import SupportDashboard from "./pages/support/SupportDashboard";
import {Routes, Route } from "react-router-dom";
import "react-toastify/dist/ReactToastify.css";

function App() {
    return (
        <>
        <Routes>

                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

            <Route
                element={<ProtectedRoute allowedRoles={["admin"]} />
            }
            >
            <Route element={<Layout />}>

                <Route path="/admin-dashboard" element={<AdminDashboard />} />
                <Route path="/admin-tickets" element={<Tickets />} />

            </Route>
            </Route>

                <Route path="/services" element={<Services />} />
                <Route path="/user-dashboard" element={<UserDashboard />} />
                <Route path="/support-dashboard" element={<SupportDashboard />} />
        </Routes>

            <ToastContainer />
        </>
    );
}

export default App;