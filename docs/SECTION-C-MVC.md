# SECTION C: MVC Architecture (20 Marks)

## Architecture Overview

The application follows the Model-View-Controller (MVC) pattern using Spring Boot's implementation. Since this is a REST API, the "View" layer is replaced by JSON responses.

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Controller  │────▶│   Service    │────▶│  Repository  │────▶│  Database    │
│  (@RestCont- │     │   (@Service) │     │  (@Repos-    │     │  (H2/Post-   │
│   roller)    │◀────│              │◀────│   itory)     │◀────│   greSQL)    │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
       │                     │                     │
       ▼                     ▼                     ▼
      DTOs                Entities              JPA/Hibernate
```

## Layer Responsibilities

### 1. Entity (Model) Layer — `com.hms.entity`

**Purpose**: Represent database tables as Java objects (JPA entities).

**Files**: `Person.java`, `Patient.java`, `Doctor.java`, `Nurse.java`, `Department.java`, `Appointment.java`, `Prescription.java`, `LaboratoryTest.java`, `Invoice.java`, `MedicalRecord.java`, `EmergencyContact.java`, `Address.java`

**Responsibility**:
- Define table structure via `@Entity`, `@Table`, `@Column`
- Map relationships via `@OneToMany`, `@ManyToOne`, `@OneToOne`, `@ManyToMany`
- Manage lifecycle callbacks (`@PrePersist`, `@PreUpdate`)
- Contain no business logic

### 2. Repository Layer — `com.hms.repository`

**Purpose**: Provide data access abstraction (DAO pattern).

**Files**: `PatientRepository.java`, `DoctorRepository.java`, etc. (12 repositories)

**Responsibility**:
- Extend `JpaRepository<T, Long>` to inherit CRUD operations
- Define derived query methods (e.g., `findByEmail(String email)`)
- Write custom JPQL and native SQL queries
- Support pagination and sorting via `Pageable` parameter

### 3. Service Layer — `com.hms.service`

**Purpose**: Contain all business logic (Service pattern).

**Files**: 10 interfaces + 10 implementations in `com.hms.service.impl`

**Responsibility**:
- Validate business rules (e.g., no overlapping appointments)
- Orchestrate multiple repository calls
- Apply transaction management (`@Transactional`)
- Convert entities to DTOs via ModelMapper
- Log all operations with SLF4J
- **No HTTP concerns** — services know nothing about web requests

### 4. Controller Layer — `com.hms.controller`

**Purpose**: Handle HTTP requests and return responses (thin layer).

**Files**: `PatientController.java`, `DoctorController.java`, etc. (9 controllers)

**Responsibility**:
- Map URLs via `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc.
- Validate request bodies with `@Valid`
- Accept DTOs, delegate to services, return DTOs
- Return appropriate HTTP status codes (`201 Created`, `404 Not Found`, etc.)
- **No business logic** — controllers only delegate

### 5. Database — H2 / PostgreSQL

**Purpose**: Persistent storage of all application data.

**Configuration**:
- **Default (dev)**: H2 in-memory database — zero configuration, fast startup, ideal for development
- **Profile `prod`**: PostgreSQL — production-grade ACID compliance, concurrent access

---

## Business Logic Rule

> "Business logic must not be implemented inside controllers."

This rule is strictly followed. Every controller method is a thin delegate:

```java
// AppointmentController.java — Thin controller
@PostMapping
public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO dto) {
    Appointment appointment = modelMapper.map(dto, Appointment.class);
    appointment.setPatient(patientService.findById(dto.getPatientId()));
    appointment.setDoctor(doctorService.findById(dto.getDoctorId()));
    Appointment saved = appointmentService.createAppointment(appointment);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(modelMapper.map(saved, AppointmentDTO.class));
}
```

The business logic (overlapping appointment check, transaction boundaries) lives entirely in `AppointmentServiceImpl`.

## Data Flow

```
HTTP Request
    │
    ▼
SecurityFilterChain ───▶ Authentication/Authorization
    │
    ▼
Controller (@Valid) ───▶ Validation errors → 400 Bad Request
    │
    ▼
Service (@Transactional) ───▶ Business rules (overlap check, status validation)
    │
    ▼
Repository (JpaRepository) ───▶ JPA/Hibernate
    │
    ▼
Database (H2/PostgreSQL)
    │
    ▼
Response (JSON DTO) ◀─── Controller returns mapped DTO
```

## Why This Architecture?

- **Separation of concerns**: Each layer has a single, well-defined responsibility.
- **Testability**: Each layer can be tested independently (unit tests for services, MockMvc for controllers, `@DataJpaTest` for repositories).
- **Maintainability**: Changes to persistence (e.g., switching databases) only affect the repository layer.
- **Flexibility**: The service layer can be reused across different controllers or even non-HTTP interfaces (e.g., scheduled tasks, message listeners).
