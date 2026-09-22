# QuickFixers

Repair-service platform: **users** create tickets for a service, **support** agents fix them, and the **admin** manages everything (users, services, payments, stats).

Monorepo containing two independent projects:

| Project | Stack | Port | Docs |
|---|---|---|---|
| 📦 `QuickFixersBackend` | Spring Boot REST API (Java 21, MySQL, JWT, Flyway) | 8081 | [Backend README](QuickFixersBackend/README.md) |
| 📦 `QuickFixersFrontend` | React + Vite SPA (Tailwind 4, React Router 7, Axios) | 5173 | [Frontend README](QuickFixersFrontend/README.md) |

---

## 🚀 Quick start

> Detailed steps live in each project's own README — this is just the order of operations.

1. **Start MySQL** on `localhost:3307` (the app creates `QuickFixers_db` and runs the Flyway migrations automatically).
2. **Start the backend** — from `QuickFixersBackend/`:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```
   API ready at `http://localhost:8081/api` (Swagger UI: `http://localhost:8081/swagger-ui/index.html`).
3. **Start the frontend** — from `QuickFixersFrontend/`:
   ```bash
   npm install
   npm run dev
   ```
   Open `http://localhost:5173`, register a USER account or log in with an existing one.

---

## 👥 Roles

| Role | What they do |
|---|---|
| **USER** | Create tickets, view details, **pay** their tickets, browse services |
| **SUPPORT** | Fix tickets assigned to them, change ticket status, view related payments |
| **ADMIN** | Everything: users, roles, services, tickets, payments, revenue stats |

---

## 💳 Payments at a glance

A user pays a ticket from its **details page** (`/user/tickets/:id`). The button only appears when the ticket is not yet `FERME` and its service has a price. Paying marks the payment `TERMINE` and closes the ticket.

---

## 📚 Full documentation

- [Backend README](QuickFixersBackend/README.md) — config, endpoints, tests, Swagger
- [Frontend README](QuickFixersFrontend/README.md) — routes, structure, payment flow

> Dockerfiles exist in both projects but are still placeholders — not needed to run locally.