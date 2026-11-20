# WorkPulse HRMS — Backend

The RESTful API backend for **WorkPulse HRMS**, built with Java and Spring Boot. It handles JWT authentication, role-based access control, attendance logging, leave approval workflows, and employee/department management backed by PostgreSQL.

## Tech Stack

- **Language & Runtime:** Java 21
- **Framework:** Spring Boot, Spring Web, Spring Data JPA, Spring Security
- **Database:** PostgreSQL 15 (`hrms_db`)
- **Security:** Stateless JWT Authentication & BCrypt password hashing
- **Build Tool:** Maven
- **Containerization:** Docker (multi-stage Maven build -> Eclipse Temurin JRE Alpine)

## Core Modules & Security

- **Authentication (`/api/auth`):** Validates credentials using BCrypt and issues signed JWT tokens containing user email and role claims.
- **Employee Management (`/api/employees`):** Full CRUD with DTO validation (`@Valid`). Enforces API-level security guards that prevent users from deleting their own accounts and restrict role modifications exclusively to `ADMIN` users.
- **Department Management (`/api/departments`):** Admin-controlled CRUD for company departments.
- **Leave Requests (`/api/leave-requests`):** Submission, filtering, and HR approval/rejection workflows with server-side self-approval prevention.
- **Attendance & HR Stats (`/api/attendance`, `/api/dashboard/hr-stats`):** Daily check-in/check-out logging and real-time aggregated metrics for the HR dashboard.

## Database Schema

Main tables managed via JPA/Hibernate in `hrms_db`:
- `users` — Authentication credentials and access roles
- `departments` — Organizational departments
- `employees` — Employee profiles, job roles, salary, employment type, and photo URLs
- `leave_requests` — Leave applications, durations, and approval status
- `attendance_logs` — Daily check-in/check-out records

## Project Structure

```text
workpulse/
├── src/
│   └── main/
│       ├── java/com/workpulse/hrms/
│       │   ├── config/        # SecurityConfig, CORS, JWT filters
│       │   ├── controllers/   # REST API endpoints
│       │   ├── dto/           # Request/Response DTOs with validation
│       │   ├── entities/      # JPA entities
│       │   ├── repositories/  # Spring Data JPA repositories
│       │   └── services/      # Business logic & security guards
│       └── resources/
│           └── application.properties
├── Dockerfile                 # Multi-stage Maven -> JRE build
└── pom.xml
```

## Configuration

Database connection properties can be configured in `src/main/resources/application.properties` or overridden via environment variables (used in Docker Compose):

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5435/hrms_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_db_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

---

## Running Instructions

### Option A: Run Full Stack with Docker (Recommended)

Ensure your workspace folder is organized as follows:

```text
hrms/
├── docker-compose.yml
├── frontend/          # Frontend repository
└── workpulse/         # This repository
```

From the parent `hrms/` directory:

```bash
# 1. Build and start PostgreSQL, Spring Boot backend, and Nginx frontend
docker compose up -d --build

# 2. Check container status
docker compose ps

# 3. View live backend logs (Ctrl + C to exit logs)
docker compose logs -f backend

# 4. Stop and remove containers (preserves database volume)
docker compose down

# 5. Clean start (removes containers & volumes, then rebuilds)
docker compose down -v && docker compose up --build
```

- **Frontend (Nginx):** `http://localhost:3000`
- **Backend API:** `http://localhost:8080`
- **PostgreSQL:** `localhost:5435` (`hrms_db`)

### Option B: Run Locally (Development Mode)

1. **Start PostgreSQL** and ensure the `hrms_db` database is running.
2. **Start the Backend** (from this `workpulse/` directory):
   ```bash
   ./mvnw spring-boot:run
   ```
   *(On Windows Command Prompt, run `mvnw.cmd spring-boot:run`.)*
3. **Start the Frontend** (from the `frontend/` directory):
   ```bash
   npm install
   npm run dev
   ```
4. Access the API at `http://localhost:8080` and the frontend at `http://localhost:5173`.