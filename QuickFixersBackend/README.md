# QuickFixers — Backend (Spring Boot)

## Run with Docker Compose

Docker Compose starts two things for you: the MySQL database and the Spring Boot app.

1. Open Docker Desktop and wait until it is running.
2. Open a terminal in the `QuickFixersBackend` folder.
3. Keep your existing `.env` file in this folder. It needs `DB_PASSWORD`,
   `JWT_SECRET`, `MAIL_USERNAME`, and `MAIL_APP_PASSWORD`.
   The JWT secret must be a Base64-encoded random key of at least 32 bytes.
   For email, use your Gmail address and Gmail app password.
4. Start everything with this command:

```bash
docker compose up --build -d
```

`--build` builds your app. `-d` keeps it running in the background.
The first start may take a few minutes.

5. Open `http://localhost:8081/swagger-ui/index.html` to try the API.

To see the app's messages:

```bash
docker compose logs -f backend
```

Press `Ctrl+C` to stop watching messages. The app keeps running.

The backend runs at `http://localhost:8081`. MySQL is available on
`localhost:3307` with database `QuickFixers_db`, user `quickfixers`, and your
`DB_PASSWORD`. Compose sets `DB_URL` and `DB_USERNAME` for the backend itself,
so their values in `.env` can stay configured for running without Docker.
The first start builds the app and waits for MySQL to be ready.

To stop everything:

```bash
docker compose down
```

Database data stays in the
`mysql_data` volume. Database passwords are applied when that volume is first
created; changing `.env` later does not change an existing database password.
These settings are intended for local development.

REST API for the **QuickFixers** repair-service application.
Clients create tickets, support agents fix them, admins manage everything — secured with JWT and role-based access.

---

## 🧰 Tech Stack

| Tool | Version / Details |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.4 |
| Spring Security + JWT | `jjwt 0.12.6` |
| Spring Data JPA | Hibernate |
| Database | MySQL 8 (`localhost:3307`) |
| Migrations | Flyway (`db/migration/V1…V5`) |
| Mapping | MapStruct 1.5.5 + Lombok |
| PDF receipts | OpenPDF (`com.github.librepdf:openpdf`) |
| Real-time chat | WebSocket (`@MessageMapping("/chat")`) |
| API docs | Springdoc OpenAPI (Swagger UI) 2.8.5 |
| Config | `spring-dotenv` (values from `.env`) |
| Build tool | Maven (wrapped: `mvnw.cmd`) |

---

## ✅ Prerequisites

- **Java 21** installed (`java -version`)
- **MySQL 8** running on `localhost:3307`
- No manual database setup needed — the app creates `QuickFixers_db` automatically
  (`createDatabaseIfNotExist=true`) and Flyway runs the migrations on startup.

---

## ⚙️ Configuration

`src/main/resources/application.properties` reads everything from environment variables, loaded from a `.env` file (via `spring-dotenv`) in `QuickFixersBackend/`:

| Variable | What it is |
|---|---|
| `DB_URL` | Full JDBC URL of the MySQL database |
| `DB_USERNAME` | MySQL user |
| `DB_PASSWORD` | MySQL password |
| `JWT_SECRET` | Secret used to sign JWTs |
| `MAIL_USERNAME` | Gmail address used to send emails |
| `MAIL_APP_PASSWORD` | Gmail app password (not the account password) |

Example `.env`:

```dotenv
DB_URL=jdbc:mysql://localhost:3307/QuickFixers_db?createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=yourpassword
JWT_SECRET=some-long-secret
MAIL_USERNAME=yourmail@gmail.com
MAIL_APP_PASSWORD=your-app-password
```

Ask a teammate for the actual values if you don't have them. Default API port: **8081**.

---

## 🚀 Run the Backend

From the `QuickFixersBackend` folder:

```bash
# Run the app (port 8081)
./mvnw spring-boot:run        # Linux / macOS
.\mvnw.cmd spring-boot:run    # Windows

# Build a jar
.\mvnw.cmd clean package

# Run the jar
java -jar target/QuickFixersBackend-0.0.1-SNAPSHOT.jar
```

The API is now at: **`http://localhost:8081/api`**

---

## 🧪 Tests

Mockito unit tests for the service layer (no database needed):

```bash
.\mvnw.cmd test -Dtest="UserImplTest,TicketImplTest,PaiementImplTest"
```

| Test class | Tests |
|---|---|
| `UserImplTest` | `creerUtilisateur_ok`, `emailExistant_erreur` |
| `TicketImplTest` | `creerTicket_ok` |
| `PaiementImplTest` | `payerTicket_ok` |

Note: Mockito services return real instances from `repository.save(...)` so the mappers receive a non-null result.

---

## 🔐 Authentication & Roles

Three roles, implemented as JPA entities inheriting from **`Person`** (`SINGLE_TABLE` inheritance):

| Role | What they can do |
|---|---|
| **CLIENT** | Create tickets, view/pay their own tickets, view their payments |
| **SUPPORT** | See & update tickets assigned to them, see related payments |
| **ADMIN** | Full control: users, roles, services, tickets, payments, stats |

Public endpoints:

| Method | Endpoint | Body |
|---|---|---|
| POST | `/api/auth/register` | `{ nom, prenom, email, password }` — creates a **CLIENT** |
| POST | `/api/auth/login` | `{ email, password }` |
| POST | `/api/auth/reset-password` | Reset a forgotten password |

Both register/login return the **JWT + user data**.
Every other request must send the token:

```
Authorization: Bearer <your-token>
```

> When a CLIENT registers, or when the admin adds a user/support, the account credentials (role + plain password) are emailed to the address. The email runs in a `try/catch` — if it fails, account creation still succeeds.

---

## 📡 API Endpoints

All routes are under `/api` and require a valid JWT unless marked **public**.

### Tickets — `/api/ticket`
| Method | Endpoint | Roles | Description |
|---|---|---|---|
| POST | `/{serviceId}/tickets` | CLIENT | Create a ticket for a service |
| GET | `/tickets?pageNumber=&pageSize=&sortBy=&sortDir=` | all | Paginated tickets (role-filtered) |
| GET | `/{id}` | all | Ticket details (`prix` = live service price) |
| PATCH | `/statut/{ticketId}/{statut}` | ADMIN, SUPPORT | Change ticket status |
| GET | `/statut/{statut}` | all | Filter by status |
| GET | `/recherche?recherche=` | all | Search tickets |
| GET | `/countTickets` | ADMIN, CLIENT | Ticket counts |
| PUT | `/modifier/{id}` | ADMIN, CLIENT | Edit a ticket |

### Services — `/api/service`
| Method | Endpoint | Roles | Description |
|---|---|---|---|
| POST | `/ajouterService` | ADMIN | Create a service (with price) |
| GET | `/listerServices` | ADMIN, CLIENT | List services |
| GET | `/consulterUnService/{id}` | ADMIN, CLIENT | One service |
| PATCH | `/modefieStatut/{id}` | ADMIN | Activate/deactivate |
| DELETE | `/supprimerService/{id}` | ADMIN | Delete a service |
| GET | `/countServices` | ADMIN | Service counts |

### Payments — `/api/paiements`
| Method | Endpoint | Roles | Description |
|---|---|---|---|
| POST | `/effectuerPaiement` | CLIENT | Pay a ticket (`{ ticketId, montant }`) — marks payment `TERMINE` and closes the ticket |
| GET | `/paimentHistorique?pageNumber=&pageSize=` | all | Payments history (role-filtered) |
| GET | `/paiements` | ADMIN, CLIENT | Payments count |
| GET | `/incomeByDay` | ADMIN, SUPPORT | Revenue stats for charts |
| GET | `/recu/{id}` | ADMIN, CLIENT, SUPPORT | **Download a small PDF receipt** (A6) for a payment |

### Users — `/api/users`
| Method | Endpoint | Roles | Description |
|---|---|---|---|
| GET | `/me` | all | Current profile |
| PUT | `/me` | all | Update profile |
| GET | `/listerUsers` | ADMIN | List all users |
| GET | `/listerClients` | ADMIN | List clients |
| GET | `/listerSupports` | ADMIN | List support agents |
| GET | `/rechercherUsers?recherche=` | ADMIN | Search users (name/email) |
| GET | `/rechercherClients` / `/rechercherSupports` | ADMIN | Search by category |
| GET | `/filtrerUsers?role=` | ADMIN | Filter users by role |
| GET | `/consulterUser/{id}` | ADMIN | One user |
| POST | `/ajouterUser` | ADMIN | Add a user (CLIENT) |
| POST | `/Ajoutersupport` | ADMIN | Add a support agent |
| PUT | `/modifierUser/{id}` | ADMIN | Edit a user's name/email |
| PATCH | `/changeRole/{id}?role=&serviceType=` | ADMIN | Change role (**CLIENT ⇄ SUPPORT** only; SUPPORT needs a `serviceType`) |
| DELETE | `/supprimerUser/{id}` | ADMIN | Delete a user |
| GET | `/countUsers` | ADMIN | User counts |

### Chat — WebSocket
| Path | Description |
|---|---|
| `/api/message` + `/chat` | Real-time messages (feature in progress) |

---

## 📖 Swagger UI

Interactive API documentation:

```
http://localhost:8081/swagger-ui/index.html
```

`springdoc.swagger-ui.persistAuthorization=true` keeps your JWT across page reloads — paste the token in **Authorize**.

---

## 📁 Project Structure

```
src/main/java/com/example/QuickFixersBackend/
├── auth/          # Login, register, reset-password (JWT + AuthenticationService)
├── controller/    # REST endpoints (per resource)
├── dto/           # Request / response objects
├── entity/        # JPA entities — Person + Admin / Support / Client (inheritance), Ticket, ServiceEntity, Paiement, Message
├── enums/         # Role (CLIENT/SUPPORT/ADMIN), Statut, PaiementStatut, ServiceType...
├── mapper/        # MapStruct mappers (e.g. ticket.prix ← service.prix)
├── repository/    # Spring Data repositories
├── security/      # JWT filter, SecurityConfig (CORS + rules)
├── services/      # Business logic (UserImpl, TicketImpl, PaiementImpl, EmailService...)
├── swaggerConfig/ # OpenAPI config
└── webSocket/     # Chat WebSocket config

src/main/resources/
├── application.properties
└── db/migration/  # Flyway scripts V1 → V5
```

---

## 💡 Key business rules

- **Ticket price is live:** a ticket's `prix` comes from its service (`service.prix`), not stored on the ticket.
- **Auto-assignment:** when a client creates a ticket, the backend assigns the SUPPORT agent with the fewest open tickets.
- **Payment closes the ticket:** paying marks the payment `TERMINE` and sets the ticket statut to `FERME`. Already-paid tickets are rejected ("Ce ticket est déjà payé").
- **PDF receipt:** any payment can be downloaded as a small A6 PDF (`/api/paiements/recu/{id}`), generated with OpenPDF — contains receipt n°, date, client, ticket, amount and status.
- **Account-creation email:** registering or having the admin create an account sends the credentials by email (never blocks creation if it fails).
- **Role changes:** only ADMIN can change roles, only **CLIENT ⇄ SUPPORT** — `ADMIN` is protected, and going to `SUPPORT` requires a `serviceType`.
- **Ownership:** clients only see their own tickets/payments; support only their assigned ones; admin sees everything.

> 🗄️ Legacy data: accounts created before the inheritance refactor may still have role `USER` in the database — run `UPDATE users SET role='CLIENT' WHERE role='USER';` (then users re-login so their JWT has the new role).
