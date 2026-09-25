# QuickFixers — Frontend (React + Vite)

Frontend of the **QuickFixers** repair-service application.
Three role-based dashboards (CLIENT / Support / Admin) to create tickets, manage repairs, and handle payments.

The app talks to the Spring Boot backend at **`http://localhost:8081/api`**.

---

## 🧰 Tech Stack

| Tool | Version / Details |
|---|---|
| React | 19 |
| Build tool | Vite 8 |
| Styling | Tailwind CSS 4 (`@tailwindcss/vite`) |
| Routing | React Router 7 (protected routes + role guards) |
| HTTP | Axios (JWT interceptor) |
| Forms | react-hook-form |
| Notifications | react-toastify |
| Charts | Chart.js + react-chartjs-2 |
| Auth tokens | jwt-decode + browser storage |
| Icons | lucide-react |
| Linter | oxlint |

---

## ✅ Prerequisites

- **Node.js** (18 or newer) — check with `node -v`
- The **backend running on `http://localhost:8081`** (see `QuickFixersBackend/README.md`)

The backend already allows this origin via CORS: `http://localhost:5173`.

---

## 🚀 Run the Frontend

From the `QuickFixersFrontend` folder:

```bash
npm install     # first time only
npm run dev
```

Open **`http://localhost:5173`** in your browser.

## 📜 Scripts

| Command | What it does |
|---|---|
| `npm install` | Install dependencies |
| `npm run dev` | Start the dev server (hot reload) |
| `npm run build` | Production build into `dist/` |
| `npm run preview` | Preview the production build locally |
| `npm run lint` | Run the oxlint linter |

---

## ⚙️ Configuration

The API base URL is defined once in `src/api/axiosApi.js`:

```js
export const axiosApi = axios.create({
    baseURL: "http://localhost:8081/api",
});
```

- **JWT handling is automatic:** the request interceptor reads the token from storage and adds
  `Authorization: Bearer <token>` to every call.
- **401 handling is automatic too:** expired/invalid token → storage cleared → back to `/login`.
- Tokens are stored under `token` and `CLIENTEmail` after login.

> Multiple CLIENTs in the same browser share the same `localStorage`, so the last login wins across tabs.
> To test two CLIENTs at once, use two browsers (or an incognito window).

---

## 🗺️ Routes (role-based)

Protected routes are wrapped in `RoleGuard`, so each role only sees its own area.

### Public
| Route | Page |
|---|---|
| `/login` | Login |
| `/register` | Create a CLIENT account |

### CLIENT (`/CLIENT/...`)
| Route | Page |
|---|---|
| `/CLIENT/dashboard` | Dashboard (tickets récents + payments) |
| `/CLIENT/tickets` | My tickets |
| `/CLIENT/tickets/:id` | Ticket details — **pay button lives here** |
| `/CLIENT/tickets/create` | Pick a service |
| `/CLIENT/create-ticket/:serviceId` | Create a ticket for a service |
| `/CLIENT/payments` | "Mes paiements" |
| `/CLIENT/services` | Browse services |
| `/CLIENT/services/:id` | Service details |
| `/CLIENT/profile` | Profile |

### Support (`/support/...`)
| Route | Page |
|---|---|
| `/support/dashboard` | Dashboard (assigned tickets) |
| `/support/tickets` | Assigned tickets |
| `/support/tickets/:id` | Ticket details + status changer |
| `/support/payments` | Payments of assigned tickets |
| `/support/profile` | Profile |

### Admin (`/admin/...`)
| Route | Page |
|---|---|
| `/admin/dashboard` | Dashboard (tickets, revenue, stats) |
| `/admin/tickets` | All tickets |
| `/admin/tickets/:id` | Ticket details + status changer |
| `/admin/CLIENTs` | Manage CLIENTs & roles |
| `/admin/payments` | All payments |
| `/admin/services` | Manage services (with prices) |
| `/admin/services/:id` | Service details |
| `/admin/profile` | Profile |

---

## 💳 How the payment works

The **`Payer … DH`** button appears on **ticket details** (`/CLIENT/tickets/:id`) only for the **CLIENT** role, and only when all of these are true:

```jsx
!canManage && ticket.statut !== "FERME" && ticket.prix != null
```

1. The CLIENT clicks **Payer {prix} DH** → confirmation dialog.
2. `createPayment(ticket.id, ticket.prix)` → `POST /paiements/effectuerPaiement`.
3. Backend marks the payment **TERMINE** and closes the ticket (**statut = FERME**).
4. The page reloads → the button disappears (no double payment).

> The price comes **live from the service** (`ticket.prix`). If a service has no price, the button is hidden and the price shows "—" on the details page.

After paying, the transaction appears under **"Mes paiements"** (`/CLIENT/payments`).

---

## 📁 Project Structure

```
src/
├── api/          # Axios instance + API helpers (ticket, paiement, service, CLIENT...)
├── components/   # Reusable UI (tables, cards, forms, sidebars, Layout, Loader...)
├── context/      # React context (if used)
├── pages/        # One folder per area: admin / support / CLIENT / services / auth
│   ├── CLIENT/     # Tickets, Dashboard, Payments, CreateTicket, PickService, Profile
│   ├── support/  # Dashboard, AssignedTickets, Payments, Profile
│   ├── admin/    # Dashboard, Tickets, CLIENTs, Payments, Services
│   ├── services/ # ServiceList, ServiceDetails
│   └── auth/     # Login, Register
├── routes/       # ProtectedRoute (logged in?) + RoleGuard (allowed role?)
├── utils/        # auth helpers (getCLIENTRole via JWT decode)
├── App.jsx       # All routes & guards
└── main.jsx      # Entry point
```

---

## 💡 Notes

- The backend **must** be running, otherwise requests fail with "Cannot connect to the server."
- The chat feature is stubbed (`api/websocket.js` + `components/chat/ChatWindow.jsx`) — the backend WebSocket exists and the page can be added later.
- Dockerfiles exist in both projects but are still being filled in — not required to run locally.
