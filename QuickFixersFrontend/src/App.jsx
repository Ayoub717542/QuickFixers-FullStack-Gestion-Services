import { ToastContainer } from "react-toastify";
import ProtectedRoute from "./routes/ProtectedRoute";
import RoleGuard from "./routes/RoleRoute.jsx";
import Layout from "./components/Layout";
import { Routes, Route } from "react-router-dom";
import "react-toastify/dist/ReactToastify.css";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";

import AdminDashboard from "./pages/admin/AdminDashboard";
import Tickets from "./pages/admin/Tickets.jsx";
import Users from "./pages/admin/Users.jsx";
import Payments from "./pages/admin/Payments.jsx";
import TicketDetails from "./pages/admin/TicketDetails.jsx";
import AdminSidebar from "./components/sidebars/AdminSidebar.jsx";

import Services from "./pages/services/Servicelist";
import ServiceDetails from "./pages/services/ServiceDetails";

import Dashboard from "./pages/support/Dashboard.jsx";
import SupportProfile from "./pages/support/Profile";
import SupportTicketDetails from "./pages/support/TicketDetails.jsx";
import SupportTickets from "./pages/support/AssignedTickets";
import SupportSidebar from "./components/sidebars/SupportSidebar.jsx";

import UserProfile from "./pages/user/Profile";
import UserTickets from "./pages/user/Tickets.jsx";
import UserDashboard from "./pages/user/Dashboard.jsx";
import UserPayments from "./pages/user/Payments.jsx";
import UserTicketDetails from "./pages/user/TicketDetails.jsx";
import UserSidebar from "./components/sidebars/UserSidebar.jsx";

function App() {
    return (
        <Route>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                <Route element={<ProtectedRoute />}>

                    <Route element={<RoleGuard allowedRoles={["ADMIN"]} />}>
                        <Route element={<Layout sidebar={<AdminSidebar />} />}>
                            <Route path="/admin/dashboard" element={<AdminDashboard />} />
                            <Route path="/admin/tickets" element={<Tickets />} />
                            <Route path="/admin/tickets/:id" element={<TicketDetails />} />
                            <Route path="/admin/users" element={<Users />} />
                            <Route path="/admin/payments" element={<Payments />} />
                            <Route path="/admin/services" element={<Services />} />
                            <Route path="/admin/services/:id" element={<ServiceDetails />} />
                        </Route>
                    </Route>

                    <Route element={<RoleGuard allowedRoles={["USER"]} />}>
                        <Route element={<Layout sidebar={<UserSidebar />} />}>
                            <Route path="/user/dashboard" element={<UserDashboard />} />
                            <Route path="/user/tickets" element={<UserTickets />} />
                            <Route path="/user/tickets/:id" element={<UserTicketDetails />} />
                            <Route path="/user/payments" element={<UserPayments />} />
                            <Route path="/user/profile" element={<UserProfile />} />
                            <Route path="/user/services" element={<Services />} />
                            <Route path="/user/services/:id" element={<ServiceDetails />} />
                        </Route>
                    </Route>

                    <Route element={<RoleGuard allowedRoles={["SUPPORT"]} />}>
                        <Route element={<Layout sidebar={<SupportSidebar />} />}>
                            <Route path="/support/dashboard" element={<Dashboard />} />
                            <Route path="/support/tickets" element={<SupportTickets />} />
                            <Route path="/support/tickets/:id" element={<SupportTicketDetails />} />
                            <Route path="/support/profile" element={<SupportProfile />} />
                        </Route>
                    </Route>
                </Route>
            </Routes>

            <ToastContainer />
        </>
    );
}

export default App;