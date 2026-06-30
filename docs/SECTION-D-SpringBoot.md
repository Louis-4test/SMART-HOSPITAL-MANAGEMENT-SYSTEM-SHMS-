# SECTION D: Spring Boot Enterprise Development (80 Marks)

## Spring Boot Core

### Spring Initializr Project
Generated at `start.spring.io` with:
- **Java 21** — Latest LTS with records, pattern matching, sealed classes
- **Spring Boot 3.2.5** — Latest stable 3.2.x release
- **Dependencies**: Spring Web, Spring Data JPA, Spring Security, Validation, H2, PostgreSQL, Lombok

### Maven Dependency Management
`pom.xml` manages all dependencies with Spring Boot's BOM (Bill of Materials) ensuring compatible versions:
- `spring-boot-starter-web` — REST controllers, embedded Tomcat
- `spring-boot-starter-data-jpa` — Hibernate ORM, JPA repositories
- `spring-boot-starter-validation` — Jakarta Bean Validation
- `spring-boot-starter-security` — Authentication, authorization
- `springdoc-openapi-starter-webmvc-ui` — Swagger/OpenAPI
- `modelmapper` — Entity ↔ DTO mapping
- `h2` / `postgresql` — Database drivers
- `lombok` — Boilerplate reduction (used minimally to maintain explicit code)

### Dependency Injection
All beans use **constructor injection** (implicit via single constructor — Spring auto-wires):

```java
@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
}
```

Why: Immutable dependencies, testable (no reflection), clear lifecycle.

### Bean Management
Spring-managed beans annotated with:
- `@Service` — Service layer beans
- `@Repository` — Repository layer beans
- `@Controller` / `@RestController` — Controller beans
- `@Component` — Generic beans (HospitalFacade, BillingContext, etc.)
- `@Configuration` — Configuration classes
- `@Bean` — Factory methods within configuration classes

### Component Scanning
Default Spring Boot scanning from `com.hms` base package — automatically discovers all `@Component`, `@Service`, `@Repository`, `@Controller` in subpackages.

### Configuration Files
`application.yml` with multi-document structure:
- **Default profile**: H2 in-memory, SQL logging enabled, dev settings
- **prod profile**: PostgreSQL, SQL logging disabled, externalized credentials via `${DB_USERNAME}` / `${DB_PASSWORD}`

### Spring Profiles
Active profile set via `spring.profiles.active`:
```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## REST API Development

### HTTP Methods
Every endpoint uses the correct HTTP verb:

| Method | Usage | Example |
|---|---|---|
| `POST` | Create resources | `POST /api/v1/patients` |
| `GET` | Retrieve resources | `GET /api/v1/patients/{id}` |
| `PUT` | Full update | `PUT /api/v1/patients/{id}` |
| `PATCH` | Partial update | `PATCH /api/v1/patients/{id}` |
| `DELETE` | Remove resources | `DELETE /api/v1/patients/{id}` |

### URL Convention
```
/api/v1/{resource}[/{id}][/action]
```
- Versioned (`v1`) for future API evolution
- Plural nouns (`/patients`, `/doctors`, `/appointments`)
- Actions as sub-resources (`/appointments/{id}/cancel`)

### Key Endpoints

**PatientController** — `/api/v1/patients`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/` | Register patient |
| GET | `/` | List all patients |
| GET | `/paged` | Paginated patient list |
| GET | `/{id}` | Get by ID |
| PUT | `/{id}` | Full update |
| PATCH | `/{id}` | Partial update |
| DELETE | `/{id}` | Delete (hard or soft) |
| GET | `/search` | Search by id/name/email/phone |

**DoctorController** — `/api/v1/doctors`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/` | Register doctor |
| GET | `/` | List all doctors |
| GET | `/{id}` | Get by ID |
| PUT | `/{id}` | Full update |
| PATCH | `/{id}` | Partial update |
| DELETE | `/{id}` | Delete |
| GET | `/specialization/{spec}` | Filter by specialization |
| GET | `/busiest` | Top 10 busiest doctors |

**AppointmentController** — `/api/v1/appointments`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/` | Create appointment |
| GET | `/` | List all |
| GET | `/{id}` | Get by ID |
| PUT | `/{id}` | Update |
| PATCH | `/{id}/cancel` | Cancel |
| PATCH | `/undo-cancel` | Undo cancellation |
| DELETE | `/{id}` | Cancel appointment |

**BillingController** — `/api/v1/billing`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/invoices/generate/{apptId}` | Generate invoice |
| GET | `/invoices/{id}` | Get invoice |
| POST | `/invoices/{id}/pay` | Process payment |

**ReportController** — `/api/v1/reports` (10 GET endpoints for various reports)

---

## Data Validation

Annotations applied on DTOs to validate all user inputs:

| Annotation | Used On | Purpose |
|---|---|---|
| `@NotBlank` | firstName, lastName, email, etc. | Required string fields |
| `@NotNull` | appointmentDate, patientId, doctorId | Required reference fields |
| `@Email` | email | Email format validation |
| `@Pattern` | phone (`^\\+?[0-9\\-\\s]{10,15}$`) | Phone number format |
| `@Size` | firstName, lastName (min=2, max=50) | String length constraints |
| `@Positive` | yearsOfExperience, amount, tax | Positive numeric values |
| `@Past` | dateOfBirth | Date must be in the past |
| `@Future` | appointmentDate | Appointment must be in the future |

Validation errors return a structured 400 response via `GlobalExceptionHandler`:
```json
{
  "timestamp": "2026-06-30T...",
  "status": 400,
  "errors": { "email": "Invalid email format" }
}
```

---

## DTO Pattern

Entity classes are never exposed through the API. DTOs (`com.hms.dto`) serve as the request/response objects.

**Why DTOs:**
- **Security**: Internal entity fields (e.g., `deleted`, `createdBy`) are never leaked to API consumers
- **Decoupling**: API contract is independent of database schema changes
- **Validation**: DTOs carry validation annotations; entities stay clean
- **Selective exposure**: Only needed fields are included in DTOs
- **Nested objects**: DTOs can flatten or restructure nested entity relationships (e.g., `appointmentId` instead of the full `Appointment` object in `InvoiceDTO`)

**Mapping**: `ModelMapper` bean automatically converts between entities and DTOs.

---

## Repository Pattern

All repositories extend `JpaRepository<T, Long>`:

```java
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);           // Derived query
    @Query("SELECT p FROM Patient p WHERE p.firstName LIKE %:keyword%")  // JPQL
    List<Patient> searchByName(@Param("keyword") String keyword);
    @Query(value = "SELECT * FROM person WHERE ...", nativeQuery = true)  // Native SQL
    Optional<Patient> findByEmailNative(@Param("email") String email);
}
```

Three query types demonstrated:
1. **Derived Query Methods** — Spring Data JPA parses method names (e.g., `findByFirstNameContainingIgnoreCase`)
2. **JPQL** — Object-oriented query language (`@Query("SELECT p FROM Patient p WHERE ...")`)
3. **Native SQL** — Raw database queries (`@Query(value = "...", nativeQuery = true)`)

---

## Design Patterns

### Factory Pattern — `HospitalFactory`
Static factory methods for creating domain objects with sensible defaults:
```java
Patient patient = HospitalFactory.createPatient("John", "Doe", "john@test.com", "+1234567890");
```
Why: Encapsulates object creation logic, centralizes default values (e.g., `patient.setPatientStatus("REGISTERED")`).

### Builder Pattern — `PatientBuilder`, `DoctorBuilder`
Fluent API for constructing complex objects with optional fields:
```java
Patient patient = new PatientBuilder()
    .withFirstName("John")
    .withLastName("Doe")
    .withEmail("john@test.com")
    .withGender("Male")
    .withMedicalRecord(medicalRecord)
    .build();
```
Why: Avoids telescoping constructors, improves readability for objects with many optional fields.

### Singleton Pattern — Spring Beans
All `@Service`, `@Repository`, `@Controller` beans are **Singleton** by default (one instance per Spring context).
- **Identified singletons**: `PatientServiceImpl`, `DoctorServiceImpl`, `BillingContext`, `HospitalFacade`
- Why: Stateless services are thread-safe when shared. Reduces memory overhead.

### Strategy Pattern — Billing
Interface `BillingStrategy` with 4 implementations:
| Strategy | Multiplier | Business Case |
|---|---|---|
| `GeneralConsultationBilling` | 1.0x | Standard doctor visit |
| `SurgeryBilling` | 3.5x | Surgical procedures |
| `LaboratoryBilling` | 1.2x | Lab tests |
| `PharmacyBilling` | 1.15x | Medications |

`BillingContext` selects the appropriate strategy at runtime based on the billing type.

### Observer Pattern — Patient Admission
Spring's `ApplicationEvent`/`@EventListener` mechanism:
- **Event**: `PatientAdmittedEvent`
- **Listeners**: `DoctorNotifier`, `BillingNotifier`, `HospitalStatisticsUpdater`, `AuditLogger`
- When a patient is admitted via `HospitalFacade.admitPatient()`, all 4 listeners fire automatically.

### Adapter Pattern — Payment Gateway
```java
// Incompatible external service
public class ExternalPaymentService {
    public String sendPayment(String txId, double value, String currencyCode) { ... }
}

// Our interface
public interface PaymentGateway {
    boolean processPayment(String invoiceNumber, double amount, String currency);
}

// Adapter making them compatible
@Component
public class ExternalPaymentGatewayAdapter implements PaymentGateway { ... }
```

### Facade Pattern — `HospitalFacade`
Coordinates complex workflows:
```java
hospitalFacade.admitPatient(patient, doctorId);     // Register + notify observers
hospitalFacade.requestLabTest(patientId, apptId, test); // Validate + create
hospitalFacade.generateBill(appointmentId);          // Check status + create invoice
hospitalFacade.dischargePatient(patientId);          // Update status
```

### Template Method Pattern — Admission Workflow
Abstract `AdmissionWorkflow` defines the skeleton:
```
processAdmission(): void [FINAL]
    ├── registerPatient()   [ABSTRACT]
    ├── assignRoom()         [ABSTRACT]
    ├── assignDoctor()       [ABSTRACT]
    ├── createMedicalRecord()[ABSTRACT]
    └── notifyDepartments()  [ABSTRACT]
```

Concrete implementations:
- `EmergencyAdmission` — Immediate room, on-call doctor, emergency team notification
- `OutpatientAdmission` — Consultation room, available specialist, pharmacy notification

---

## Entity Relationships

| Relationship | Type | Owner | Cascade | Fetch |
|---|---|---|---|---|
| Department → Doctor | @OneToMany | Department | None (aggregation) | LAZY |
| Doctor → Appointment | @OneToMany | Doctor | ALL | LAZY |
| Patient → Appointment | @OneToMany | Patient | ALL | LAZY |
| Patient → Prescription | @OneToMany | Patient | ALL | LAZY |
| Appointment → Invoice | @OneToOne | Appointment | ALL | LAZY |
| Patient ↔ LaboratoryTest | @ManyToMany | LaboratoryTest (join table) | — | LAZY |
| Patient → MedicalRecord | @OneToOne | Patient | ALL + orphanRemoval (composition) | EAGER |
| Patient → EmergencyContact | @OneToOne | Patient | ALL + orphanRemoval (composition) | EAGER |
| Patient → Address | @OneToOne | Patient | ALL + orphanRemoval (composition) | EAGER |

### Cascade Operations Demonstrated
- `CascadeType.ALL` — Patient → MedicalRecord/EmergencyContact/Address
- `CascadeType.PERSIST` — Doctor → Appointments
- `CascadeType.MERGE` — Standard cascade for updates
- `CascadeType.REMOVE` — Via `orphanRemoval = true` on composition entities

### Fetch Strategies
- **LAZY Loading**: Default for `@OneToMany` and `@ManyToMany` — collections loaded on demand to avoid performance overhead
- **EAGER Loading**: `@OneToOne` for composition entities — these are small, always-needed objects

### Transaction Management
`@Transactional` applied at the class level on all service implementations. This ensures:
- Database operations within a method run in a single transaction
- Automatic rollback on unchecked exceptions
- Consistency across multiple repository calls (e.g., create appointment + check for conflicts)

### Logging
SLF4J with `LoggerFactory` in every service class:
```java
private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);
```
All public methods log entry at `info` level and exit at `debug` level.

### Pagination and Sorting
Built into every "list all" endpoint via Spring Data `Pageable`:
```java
@GetMapping("/paged")
public ResponseEntity<Page<PatientDTO>> getAllPatientsPaged(Pageable pageable) {
    return ResponseEntity.ok(patientService.findAll(pageable)
            .map(p -> modelMapper.map(p, PatientDTO.class)));
}
```
Callers use: `GET /api/v1/patients/paged?page=0&size=10&sort=lastName,asc`

### Filtering and Searching
- **Filtering**: By specialization (`/doctors/specialization/{spec}`), by patient (`/prescriptions/patient/{patientId}`)
- **Derived queries**: `findByStatus()`, `findByGender()`, `findByDepartmentId()`
- **JPQL**: `@Query("SELECT d FROM Doctor d ORDER BY SIZE(d.appointments) DESC")` — busiest doctors
- **Native SQL**: `@Query(value = "SELECT * FROM person WHERE person_type = 'DOCTOR' AND department_id = :deptId", nativeQuery = true)`

### Auditing
Spring Data JPA Auditing automatically populates:
- `@CreatedDate` → `createdAt` on entity creation
- `@LastModifiedDate` → `updatedAt` on every update
- `@CreatedBy` → `createdBy` (set via `AuditorAware<String>` bean)

### Security
Spring Security with role-based access:

| Endpoint Pattern | Allowed Roles |
|---|---|
| `POST/PUT /api/v1/patients` | ADMIN, RECEPTIONIST |
| `POST /api/v1/doctors` | ADMIN |
| `POST /api/v1/nurses` | ADMIN |
| `DELETE /api/v1/**` | ADMIN |
| `GET /api/v1/reports/**` | ADMIN, DOCTOR |
| `GET /api/v1/**` | All authenticated users |
| `/h2-console/**`, `/swagger-ui/**` | Permit all |

4 in-memory users: `admin`, `doctor`, `nurse`, `receptionist` with BCrypt-encoded passwords.

### API Documentation
Swagger/OpenAPI configured via `OpenApiConfig`:
- UI available at: `http://localhost:8080/swagger-ui.html`
- JSON spec: `http://localhost:8080/v3/api-docs`
- Security scheme: Basic Auth

### Custom Exceptions

| Exception | HTTP Status | Trigger |
|---|---|---|
| `PatientNotFoundException` | 404 | Patient not found |
| `DuplicatePatientException` | 409 | Duplicate email/National ID |
| `DoctorUnavailableException` | 503 | Doctor not available |
| `AppointmentConflictException` | 409 | Overlapping appointment |
| `PaymentFailedException` | 402 | Payment processing failure |

### Global Exception Handling
`@ControllerAdvice` + `@ExceptionHandler` catches all exceptions:
- Custom exceptions → appropriate HTTP status codes
- `MethodArgumentNotValidException` → structured 400 with field-level errors
- Generic `Exception` → 500 Internal Server Error

---

## Testing

### Unit Tests (JUnit 5 + Mockito)
- `PatientServiceTest` — 7 tests: create, find, search, delete
- `AppointmentServiceTest` — 4 tests: creation with/without conflicts, cancel/undo

### Integration Tests (MockMvc)
- `PatientControllerTest` — 6 tests: CRUD with auth, 404 handling, unauthorized access

### Test Results
```
Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
```
