# QuickFixers — Backend (Spring Boot)

REST API for the **QuickFixers** repair-service application.
CLIENTs create tickets, support agents fix them, admins manage everything — secure with JWT and role-based access.

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
| Real-time chat | WebSocket (`@MessageMapping("/chat")`) |
| API docs | Springdoc OpenAPI (Swagger UI) 2.8.5 |
| Build tool | Maven (wrapped: `mvnw.cmd`) |

---

## ✅ Prerequisites

- **Java 21** installed (`java -version`)
- **MySQL 8** running on `localhost:3307`
- No manual database setup needed — the app creates `QuickFixers_db` automatically
  (`createDatabaseIfNotExist=true`) and Flyway runs the migrations on startup.

---

## ⚙️ Configuration

Everything lives in `src/main/resources/application.properties`:

```properties
spring.application.name=QuickFixers
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3307/QuickFixers_db?createDatabaseIfNotExist=true
spring.datasource.CLIENTname=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

Adjust **CLIENTname / password** to match your local MySQL. Default port: **8081**.

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
.\mvnw.cmd test -Dtest="CLIENTImplTest,TicketImplTest,PaiementImplTest"
```

| Test class | Tests |
|---|---|
| `CLIENTImplTest` | `creerUtilisateur_ok`, `emailExistant_erreur` |
| `TicketImplTest` | `creerTicket_ok` |
| `PaiementImplTest` | `payerTicket_ok` |

Note: Mockito services return real instances from `repository.save(...)` so the mappers receive a non-null result.

---

## 🔐 Authentication & Roles

Three roles:

| Role | What they can do |
|---|---|
| **CLIENT** | Create tickets, view/pay their own tickets, view their payments |
| **SUPPORT** | See & update tickets assigned to them, see related payments |
| **ADMIN** | Full control: CLIENTs, services, tickets, payments, stats |

Public endpoints:

| Method | Endpoint | Body |
|---|---|---|
| POST | `/api/auth/register` | `{ nom, prenom, email, password }` |
| POST | `/api/auth/login` | `{ email, password }` |

Both return the JWT + CLIENT data.
Every other request must send the token:

```
Authorization: Bearer <your-token>
```

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

### CLIENTs — `/api/CLIENTs`
| Method | Endpoint | Roles | Description |
|---|---|---|---|
| GET | `/me` | all | Current profile |
| PUT | `/me` | all | Update profile |
| GET | `/listerCLIENTs` | ADMIN | List CLIENTs |
| POST | `/ajouterCLIENT` | ADMIN | Add a CLIENT |
| POST | `/Ajoutersupport` | ADMIN | Add a support agent |
| PATCH | `/changeRole/{id}` | ADMIN | Change a CLIENT role |
| DELETE | `/supprimerCLIENT/{id}` | ADMIN | Delete a CLIENT |
| GET | `/countCLIENTs` | ADMIN | CLIENT counts |

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
├── auth/          # Login & register (JWT + AuthenticationService)
├── controller/    # REST endpoints (per resource)
├── dto/           # Request / response objects
├── entity/        # JPA entities (CLIENT, Ticket, ServiceEntity, Paiement, Message)
├── enums/         # Role, Statut, PaiementStatut, ServiceType...
├── mapper/        # MapStruct mappers (e.g. ticket.prix ← service.prix)
├── repository/    # Spring Data repositories
├── security/      # JWT filter, SecurityConfig (CORS + rules)
├── services/      # Business logic (CLIENTImpl, TicketImpl, PaiementImpl...)
├── swaggerConfig/ # OpenAPI config
└── webSocket/     # Chat WebSocket config

src/main/resources/
└── db/migration/  # Flyway scripts V1 → V5
```

---

## 💡 Key business rules

- **Ticket price is live:** a ticket's `prix` comes from its service (`service.prix`), not stored on the ticket.
- **Auto-assignment:** when a CLIENT creates a ticket, the backend assigns the SUPPORT agent with the fewest open tickets.
- **Payment closes the ticket:** paying marks the payment `TERMINE` and sets the ticket statut to `FERME`. Already-paid tickets are rejected ("Ce ticket est déjà payé").
- **Ownership:** CLIENTs only see their own tickets/payments; support only their assigned ones; admin sees everything.
