# 🎫 SmartQueue — Queue Management System

> A full-stack queue management system inspired by real-world service environments — banks, hospitals, and public offices. Customers issue tickets from a self-service totem, while attendants manage counters and call the next in line with built-in priority rules.

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Angular](https://img.shields.io/badge/Angular_20-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge)
![STOMP](https://img.shields.io/badge/STOMP-Messaging-blue?style=for-the-badge)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)

---

## 📸 Overview

SmartQueue simulates the queue management systems found in service-heavy environments. The project was built as a learning exercise to explore priority data structures, REST API design, Angular PWA development, and real-world domain modeling.

The system is intentionally split into two interfaces: a customer-facing totem for issuing tickets and an attendant-facing panel for managing the queue.

---

## ✨ Features

- **Self-service totem** — customers choose their service type and receive a numbered ticket
- **Three-tier priority queue** — Special (AE) → Priority (AP) → Normal (AN), ordered by issuance time within each tier
- **Anti-starvation protection** — any ticket waiting over 30 minutes is automatically promoted to the front of the queue
- **Attendant panel** — counters go online/offline, call the next ticket, and finish service
- **Multiple counters** — each counter independently manages its own session
- **Ticket persistence** — issued tickets survive page refresh via localStorage
- **PWA-ready** — both interfaces are accessible from any browser, including mobile

---

## 🎫 Ticket Types

| Code | Type | Criteria |
|------|------|----------|
| `AE` | Special Attendance | 80+ years old or special needs |
| `AP` | Priority Attendance | 60–79 years old |
| `AN` | Normal Attendance | No priority criteria |

---

## 🏗️ Architecture

```
┌─────────────────────┐       HTTP REST        ┌──────────────────────┐
│                     │──── POST /tickets ────▶│                      │
│  Angular 20 PWA     │                        │  Spring Boot 3       │
│                     │◀─── GET  /tickets ─────│  Backend             │
│  /totem             │                        │  :8080               │
│  /attendant         │──── POST /counter ─────│                      │
│  :4200              │                        └──────────┬───────────┘
│                     │                                   │ JPA/Hibernate
└─────────────────────┘                                   ▼
                                              ┌──────────────────────┐
                                              │   PostgreSQL         │
                                              │   smart_queue        │
                                              └──────────────────────┘
```

> **Architectural note:** The attendant panel uses a manual refresh button to update the queue — a deliberate MVP decision. See [v1.1](#roadmap) for the WebSocket upgrade and the reasoning behind starting with polling.

---

## 🛠️ Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 21 | Language |
| Spring Boot 3 | Application framework |
| Spring Data JPA + Hibernate | ORM and database access |
| PostgreSQL | Relational database |
| SpringDoc OpenAPI | API documentation (Swagger UI) |
| Lombok | Boilerplate reduction |
| JUnit 5 + Mockito | Unit testing |

### Frontend
| Technology | Purpose |
|---|---|
| Angular 20 | SPA / PWA framework |
| RxJS | Reactive HTTP communication |
| TypeScript | Language |
| date-fns | Wait time formatting |
| nginx | Static file serving in Docker |

### Infrastructure
| Technology | Purpose |
|---|---|
| Docker + Docker Compose | Containerization |
| GitHub Actions | CI/CD pipeline |

---

## 📁 Project Structure

```
smart-queue/
├── smart-queue-backend/
│   └── src/
│       ├── main/java/com/queue/smart_queue/
│       │   ├── ticket/         # Ticket domain (entity, service, repository, controller)
│       │   ├── counter/        # Counter domain (entity, service, repository, controller)
│       │   └── exception/      # Custom exceptions + global handler
│       └── test/               # Unit tests (JUnit 5 + Mockito)
│
├── smart-queue-frontend/
│   └── src/app/
│       ├── components/
│       │   ├── totem/          # Customer-facing: issue a ticket
│       │   └── attendant/      # Attendant-facing: manage counter and queue
│       ├── services/
│       │   ├── ticket/         # Ticket HTTP service + DTOs
│       │   └── counter/        # Counter HTTP service + DTOs
│       └── pipes/
│           └── wait-time/      # Formats issuedAt timestamp as "X min ago"
│
├── docker-compose.yml
├── .env.example
└── .github/workflows/
    └── ci.yml                  # Runs on every push (test + build)
```

---

## 🚀 Getting Started

### Prerequisites
- Docker and Docker Compose

### Running with Docker Compose

```bash
# Clone the repository
git clone https://github.com/feelipemarques/smart-queue.git
cd smart-queue

# Configure environment variables
cp .env.example .env
# Edit .env with your values

# Start all services
docker compose up --build
```

Access the totem at `http://localhost:4200/totem` and the attendant panel at `http://localhost:4200/attendant`.

### Running locally (development)

**Backend:**
```bash
cd smart-queue-backend
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd smart-queue-frontend
npm install
ng serve
```

---

## 📖 API Documentation

With the backend running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Tests

```bash
cd smart-queue-backend
./mvnw test
```

### What's tested
- `TicketService` — ticket issuance and number formatting, empty queue exception, counter already in service exception
- `CounterService` — offline counter validation, duplicate active ticket prevention, ticket ownership validation on finish

---

## 🔄 CI/CD Pipeline

Every push triggers the CI pipeline:
1. Run backend unit tests
2. Build backend JAR
3. Build frontend

---

## 🗺️ Roadmap

- [x] **v1.0** — MVP: totem, priority queue, attendant panel, multiple counters, Docker
- [x] **v1.1** — Replace manual refresh with **WebSocket (STOMP)** real-time updates — the attendant panel and totem update automatically when queue state changes
- [x] **v1.2** — **NPS via email** — after service ends, publish an event to **RabbitMQ**; a notification service consumes it asynchronously and sends a satisfaction survey
- [ ] **v1.3** — Unit test coverage expansion + integration tests for the priority queue JPQL query
- [ ] **v2.0** — Counter login (attendants select from a fixed list), multiple service types (Cashier, Manager, etc.), native mobile app (Flutter), telemetry with Micrometer + Prometheus/Grafana

---

## 💡 Key Learnings

- **Priority queue with anti-starvation** — ordering by priority tier + issuance time in JPQL, with a 30-minute threshold to prevent normal tickets from waiting indefinitely
- **Domain-Driven Design** — organizing code by domain (ticket, counter) instead of by layer
- **JPA relationships** — `@ManyToOne`, `@OneToMany`, cascade operations, lazy loading
- **Angular PWA** — routing, lifecycle hooks, localStorage persistence, impure pipes, HTTP client with RxJS
- **Unit testing with Mockito** — mocking repositories, `any()` vs `eq()` matchers, testing exception paths
- **Docker multi-stage builds** — separate build and runtime stages for both Java and Angular

---

## 👤 Author

**Felipe Marques**
- LinkedIn: [Felipe Marques](https://linkedin.com/in/feelipe-maarquees)
- GitHub: [Felipe Marques](https://github.com/feelipemarques)
