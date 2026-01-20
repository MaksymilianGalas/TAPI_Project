# Microservices Application

A complete microservices-based application demonstrating modern architecture patterns with Spring Boot, MongoDB, Keycloak authentication, API Gateway, and React frontend.

## 📋 Requirements Overview

This application fulfills all specified requirements:

### 1. ✅ Podział na Mikroserwisy (Microservices Division)
- **User Service** (port 8081) - User management with CRUD operations
- **Order Service** (port 8082) - Order management with CRUD operations  
- **Document Service** (port 8083) - PDF and Excel document generation
- Each service runs as an independent process with its own codebase

**Location**: 
- `user-service/` - Complete Spring Boot microservice
- `order-service/` - Complete Spring Boot microservice
- `document-service/` - Complete Spring Boot microservice

### 2. ✅ Izolacja Danych (Data Isolation)
- **Database per Service** pattern strictly enforced
- Each microservice has its own isolated MongoDB database:
  - User Service → `userdb` (MongoDB on port 27017)
  - Order Service → `orderdb` (MongoDB on port 27018)
  - Document Service → `documentdb` (MongoDB on port 27019)
- No shared databases or cross-service data access

**Location**: See `docker-compose.yml` - separate MongoDB instances for each service

### 3. ✅ Infrastruktura (Infrastructure)
- Complete Docker Compose orchestration
- Single command startup: `docker-compose up`
- Includes:
  - 3 microservices
  - 3 MongoDB databases
  - Keycloak + PostgreSQL
  - API Gateway
  - React Frontend

**Location**: `docker-compose.yml` - complete infrastructure definition

### 4. ✅ Funkcjonalność Podstawowa (Basic Functionality)
- **Keycloak Authentication** - Full login/logout flow
- **CRUD Operations** implemented in all services:
  - Users: Create, Read, Update, Delete
  - Orders: Create, Read, Update, Delete  
  - Documents: Generate, List, Delete
- **REST API** endpoints for all operations

**Location**: 
- Keycloak: `keycloak/realm-export.json`
- User CRUD: `user-service/src/main/java/com/microservices/userservice/controller/UserController.java`
- Order CRUD: `order-service/src/main/java/com/microservices/orderservice/controller/OrderController.java`

### 5. ✅ Frontend (Frontend Application)
- **React 18** application with modern UI
- Communicates with backend through API Gateway only
- Features:
  - Keycloak integration for authentication
  - User management interface
  - Order management interface
  - Document generation interface
- Runs on port 3000

**Location**: `frontend/` - Complete React application

### 6. ✅ API Gateway
- **Spring Cloud Gateway** configured and running
- All frontend requests route through Gateway (port 8080)
- Frontend does NOT connect directly to microservice ports
- Route configuration:
  - `/api/users/**` → User Service
  - `/api/orders/**` → Order Service
  - `/api/documents/**` → Document Service

**Location**: `gateway/src/main/resources/application.yml` - route definitions

### 7. ✅ Bezpieczeństwo JWT (JWT Security)
- **JWT tokens** issued by Keycloak
- Token flow:
  1. Frontend authenticates with Keycloak
  2. Keycloak issues JWT token
  3. Frontend sends token in Authorization header
  4. API Gateway validates JWT
  5. Each microservice validates JWT independently
- OAuth2 Resource Server configuration in all services

**Location**: 
- Gateway validation: `gateway/src/main/java/com/microservices/gateway/config/SecurityConfig.java`
- Service validation: `*-service/src/main/java/com/microservices/*/config/SecurityConfig.java`
- Frontend token handling: `frontend/src/api.js`

### 8. ✅ Generowanie Dokumentów (Document Generation)
- **PDF Generation** using iText library:
  - Invoice PDF
  - Business Report PDF
- **Excel Generation** using Apache POI:
  - Order Report Excel
  - User Report Excel
- Documents downloadable from frontend
- Metadata stored in MongoDB

**Location**: 
- PDF Generator: `document-service/src/main/java/com/microservices/documentservice/generator/PdfGenerator.java`
- Excel Generator: `document-service/src/main/java/com/microservices/documentservice/generator/ExcelGenerator.java`
- Frontend UI: `frontend/src/components/DocumentGeneration.js`

## 🚀 Quick Start

### Prerequisites
- Docker Desktop installed and running
- Ports available: 3000, 8080, 8081, 8082, 8083, 8180, 27017, 27018, 27019

### Start the Application

```bash
# Navigate to project directory
cd TAPI_Project

# Start all services with one command
docker-compose up --build
```

Wait for all services to start (approximately 2-3 minutes). You'll see:
- ✅ PostgreSQL ready
- ✅ Keycloak ready
- ✅ MongoDB instances ready (3x)
- ✅ User Service ready
- ✅ Order Service ready
- ✅ Document Service ready
- ✅ API Gateway ready
- ✅ Frontend ready

### Access the Application

1. **Frontend**: http://localhost:3000
2. **Login Credentials**:
   - Admin: `admin` / `admin123`
   - User: `user` / `user123`
3. **Keycloak Admin**: http://localhost:8180 (`admin` / `admin`)
4. **API Gateway**: http://localhost:8080

## 📖 Usage Guide

### 1. Login
- Open http://localhost:3000
- Click "Login"
- Enter credentials (admin/admin123 or user/user123)

### 2. User Management
- Navigate to "Users" tab
- Create, edit, or delete users
- All operations go through API Gateway → User Service → MongoDB

### 3. Order Management
- Navigate to "Orders" tab
- Create orders with multiple items
- Edit order status (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- Delete orders
- All operations go through API Gateway → Order Service → MongoDB

### 4. Document Generation
- Navigate to "Documents" tab
- Generate PDF invoices or reports
- Generate Excel order or user reports
- Documents download automatically
- Metadata saved in Document Service MongoDB

## 🏗️ Architecture

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │ http://localhost:3000
       ▼
┌─────────────────┐
│ React Frontend  │ (Port 3000)
│   + Keycloak    │
└────────┬────────┘
         │ JWT Token
         ▼
┌─────────────────┐
│  API Gateway    │ (Port 8080)
│ Spring Cloud GW │
│  JWT Validation │
└────────┬────────┘
         │
    ┌────┴────┬────────────┐
    ▼         ▼            ▼
┌────────┐ ┌────────┐ ┌──────────┐
│  User  │ │ Order  │ │ Document │
│Service │ │Service │ │ Service  │
│ :8081  │ │ :8082  │ │  :8083   │
└───┬────┘ └───┬────┘ └────┬─────┘
    │          │           │
    ▼          ▼           ▼
┌────────┐ ┌────────┐ ┌────────┐
│MongoDB │ │MongoDB │ │MongoDB │
│ userdb │ │orderdb │ │ docdb  │
│ :27017 │ │ :27018 │ │ :27019 │
└────────┘ └────────┘ └────────┘

         ┌──────────┐
         │ Keycloak │ (Port 8180)
         │   Auth   │
         └──────────┘
```

## 🔒 Security Flow

1. User logs in via Frontend
2. Frontend redirects to Keycloak
3. Keycloak authenticates and issues JWT token
4. Frontend stores token and includes in all API requests
5. API Gateway validates JWT before routing
6. Each microservice independently validates JWT
7. Only authenticated requests with valid tokens are processed

## 📁 Project Structure

```
TAPI_Project/
├── docker-compose.yml          # Infrastructure orchestration
├── .env                        # Environment variables
├── keycloak/
│   └── realm-export.json      # Keycloak configuration
├── user-service/              # User microservice
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── order-service/             # Order microservice
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── document-service/          # Document microservice
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── gateway/                   # API Gateway
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
└── frontend/                  # React application
    ├── src/
    ├── package.json
    └── Dockerfile
```

## 🛠️ Technology Stack

### Backend
- **Spring Boot 3.2.1** - Microservices framework
- **Spring Cloud Gateway 2023.0.0** - API Gateway
- **Spring Security + OAuth2** - JWT validation
- **Spring Data MongoDB** - Database access
- **iText 8.0.2** - PDF generation
- **Apache POI 5.2.5** - Excel generation
- **Java 17** - Programming language

### Frontend
- **React 18** - UI framework
- **Keycloak JS** - Authentication client
- **Axios** - HTTP client
- **React Router** - Navigation

### Infrastructure
- **Docker & Docker Compose** - Containerization
- **MongoDB 7.0** - NoSQL database (3 instances)
- **Keycloak 23.0** - Identity and access management
- **PostgreSQL 15** - Keycloak database
- **Nginx** - Frontend web server

## 🧪 Testing the Application

### Test User CRUD
```bash
# Via Frontend: Users tab
# Or via API Gateway:
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/users
```

### Test Order CRUD
```bash
# Via Frontend: Orders tab
# Or via API Gateway:
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/orders
```

### Test Document Generation
```bash
# Via Frontend: Documents tab
# Or via API Gateway:
curl -X POST \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"invoiceNumber":"INV-001","customerName":"Test"}' \
  http://localhost:8080/api/documents/generate/pdf/invoice \
  --output invoice.pdf
```

## 🔍 Monitoring

### Health Checks
- User Service: http://localhost:8081/actuator/health
- Order Service: http://localhost:8082/actuator/health
- Document Service: http://localhost:8083/actuator/health
- API Gateway: http://localhost:8080/actuator/health

### Logs
```bash
# View all logs
docker-compose logs -f

# View specific service
docker-compose logs -f user-service
docker-compose logs -f order-service
docker-compose logs -f document-service
docker-compose logs -f api-gateway
```

## 🛑 Stopping the Application

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

## 🎯 Key Features Demonstrated

1. **Microservices Architecture** - Independent, scalable services
2. **Database per Service** - Complete data isolation
3. **API Gateway Pattern** - Single entry point for all requests
4. **JWT Security** - Token-based authentication and authorization
5. **Service Discovery** - Docker networking for service communication
6. **Document Generation** - PDF and Excel creation
7. **Modern Frontend** - React with Keycloak integration
8. **Containerization** - Docker for all components
9. **Infrastructure as Code** - Docker Compose orchestration

## 📝 Default Users

| Username | Password  | Role  |
|----------|-----------|-------|
| admin    | admin123  | Admin |
| user     | user123   | User  |

## 🔧 Configuration

All services are configured via environment variables in `docker-compose.yml`:
- Database connections
- Keycloak URLs
- Service URLs
- JWT validation settings

## 📚 API Documentation

### User Service
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user (Admin only)
- `PUT /api/users/{id}` - Update user (Admin only)
- `DELETE /api/users/{id}` - Delete user (Admin only)

### Order Service
- `GET /api/orders` - List all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create order
- `PUT /api/orders/{id}` - Update order
- `DELETE /api/orders/{id}` - Delete order (Admin only)

### Document Service
- `GET /api/documents` - List document metadata
- `POST /api/documents/generate/pdf/invoice` - Generate invoice PDF
- `POST /api/documents/generate/pdf/report` - Generate report PDF
- `POST /api/documents/generate/excel/orders` - Generate order Excel
- `POST /api/documents/generate/excel/users` - Generate user Excel

## 🎓 Learning Resources

This project demonstrates:
- Microservices design patterns
- RESTful API design
- JWT authentication flow
- Docker containerization
- React frontend development
- Spring Boot best practices
- MongoDB database design
- API Gateway implementation

---

**Built with ❤️ using Spring Boot, React, and Docker**
