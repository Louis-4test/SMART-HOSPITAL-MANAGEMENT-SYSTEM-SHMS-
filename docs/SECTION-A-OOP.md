# SECTION A: Object-Oriented Programming (40 Marks)

## Requirement 1: Encapsulation

### What was done
All domain classes use `private` attributes with public getters and setters:

| Class | File |
|---|---|
| `Person` | `entity/Person.java` |
| `Patient` | `entity/Patient.java` |
| `Doctor` | `entity/Doctor.java` |
| `Nurse` | `entity/Nurse.java` |
| `Department` | `entity/Department.java` |
| `Appointment` | `entity/Appointment.java` |
| `Prescription` | `entity/Prescription.java` |
| `LaboratoryTest` | `entity/LaboratoryTest.java` |
| `Invoice` | `entity/Invoice.java` |

### Why encapsulation
- **Data hiding**: Private fields prevent external classes from directly modifying internal state. Access is only through controlled getter/setter methods.
- **Validation gateway**: Setters can include validation logic before allowing state changes.
- **Maintainability**: Internal representation can change without affecting external code as long as the public interface stays consistent.
- **Security**: Prevents unauthorized or accidental corruption of object data.

---

## Requirement 2: Inheritance

### What was done
Created `Person` as an abstract superclass with common attributes: `id`, `firstName`, `lastName`, `email`, `phone`, `address`, `createdAt`, `updatedAt`.

Subclasses inheriting from `Person`:
- `Patient` (discriminator: `PATIENT`)
- `Doctor` (discriminator: `DOCTOR`)
- `Nurse` (discriminator: `NURSE`)
- `Receptionist` (discriminator: `RECEPTIONIST`)

Inheritance strategy: `SINGLE_TABLE` — all subclasses stored in one `person` table with a `person_type` discriminator column.

### Why inheritance
- **Code reuse**: Common attributes (`firstName`, `lastName`, `email`, etc.) are defined once in `Person` and inherited by all 4 subclasses, eliminating duplication.
- **Polymorphism**: A `Person` reference can point to any subclass instance, enabling polymorphic behavior.
- **Extensibility**: New person types (e.g., `Pharmacist`, `Technician`) can be added by extending `Person` without modifying existing code.
- **DRY principle**: Shared logic like auditing timestamps (`createdAt`, `updatedAt`) is centralized in the base class.

---

## Requirement 3: Abstraction

### What was done
Created interfaces for all major service components:

| Interface | Implementation(s) |
|---|---|
| `PersonService` | `PersonServiceImpl` |
| `PatientService` | `PatientServiceImpl` |
| `DoctorService` | `DoctorServiceImpl` |
| `AppointmentService` | `AppointmentServiceImpl` |
| `BillingService` | `BillingServiceImpl` |
| `ReportService` | `ReportServiceImpl` |

Additional interfaces: `NurseService`, `DepartmentService`, `PrescriptionService`, `LaboratoryTestService`.

### Why abstraction
- **Loose coupling**: Controllers depend on interfaces, not concrete implementations. Implementations can be swapped without changing the controller.
- **Testability**: Interfaces enable easy mocking with Mockito for unit tests.
- **Contract enforcement**: Interfaces define a clear contract for what each service provides.
- **Spring DI compatibility**: Spring's dependency injection works naturally with interfaces — `@Service` implementations are injected where the interface is declared.

---

## Requirement 4: Polymorphism

### What was done
Runtime polymorphism demonstrated via `Person` references:

```java
List<Person> persons = Arrays.asList(patient, doctor, nurse);
for (Person p : persons) {
    p.displayProfile(); // Each subclass has its own override
}
```

Spring Dependency Injection relies on this principle — a `PatientService` reference can point to any `PatientServiceImpl` (or mock in tests).

### Why polymorphism
- **Flexibility**: Methods operate on the abstract type (`Person`) and automatically invoke the correct subclass behavior at runtime.
- **Open/Closed principle**: New types can be introduced without changing existing polymorphic code.
- **Spring DI**: Spring beans are injected through interface references, relying on polymorphism to provide the correct implementation at runtime.

---

## Requirement 5: Method Overloading

### What was done
Five overloaded `searchPatient()` methods in `PatientService`:

```java
List<Patient> searchPatient();                          // No args - returns all
List<Patient> searchPatient(Long id);                   // By ID
List<Patient> searchPatient(String name);               // By name keyword
List<Patient> searchPatient(String name, String phone); // By name + phone
Patient findByEmail(String email);                      // By email (separate due to Java type erasure)
```

Note: `searchPatient(String name)` and `searchPatient(String email)` would have identical signatures in Java (same parameter type), so email search is provided via the distinct `findByEmail` method.

### Why method overloading
- **Convenience**: Callers can search using whichever criteria are available (ID, name, or name+phone) without needing separate method names.
- **Readability**: Related operations share a common name, making the API intuitive.
- **Compile-time safety**: The compiler selects the correct overload based on argument types.

---

## Requirement 6: Method Overriding

### What was done
Overridden methods in subclasses:

| Method | Base (Person) | Overrides |
|---|---|---|
| `displayProfile()` | Empty implementation | `Patient` — prints name + status; `Doctor` — prints name + specialization; `Nurse` — prints name + shift; `Receptionist` — prints name + code |
| `calculateCharges()` | Returns 0.0 | `Patient` — returns 0 (base); `Doctor` — returns 0 (base); `Nurse` — returns 0 (base) |
| `generateReport()` | Used via `ReportService` | Implemented per-report in `ReportServiceImpl` |

### Why overriding
- **Specialized behavior**: Each subclass provides its own implementation of a common method, ensuring the correct behavior runs for the actual type.
- **Polymorphic dispatch**: When `displayProfile()` is called on a `Person` reference, Java's runtime dispatch runs the correct subclass version.
- **Template Method pattern support**: Abstract workflow methods are overridden by concrete subclasses (`EmergencyAdmission`, `OutpatientAdmission`).

---

## Requirement 7: Composition

### What was done
`Patient` contains three owned objects via `@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)`:

```java
public class Patient extends Person {
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private MedicalRecord medicalRecord;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private EmergencyContact emergencyContact;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address patientAddress;
}
```

### Why composition
- **Ownership**: `MedicalRecord`, `EmergencyContact`, and `Address` are part of the `Patient` — they have no independent existence outside a patient context.
- **Lifecycle coupling**: Deleting a `Patient` automatically deletes its `MedicalRecord`, `EmergencyContact`, and `Address` (via `CascadeType.ALL` + `orphanRemoval = true`).
- **Strong relationship**: Composition models "has-a" where the part cannot exist without the whole.

---

## Requirement 8: Aggregation

### What was done
`Department` contains a `List<Doctor>` with no cascade operations:

```java
public class Department {
    @OneToMany(mappedBy = "department")
    private List<Doctor> doctors = new ArrayList<>();
}
```

### Why aggregation
- **Independent lifecycle**: Doctors exist independently of departments. Deleting a department must NOT delete its doctors — they can be reassigned.
- **Weak ownership**: The `@OneToMany` has no cascade type, meaning operations on `Department` do not cascade to `Doctor`.
- **Real-world mapping**: A doctor can belong to a department, leave it, or join a different one without being deleted.

---

## Requirement 9: Association

### What was done
Bidirectional `@ManyToOne` / `@OneToMany` between `Patient` ↔ `Appointment` ↔ `Doctor`:

```java
// Appointment links Patient and Doctor
public class Appointment {
    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;
}
```

### Why association
- **Real-world mapping**: A patient consults a doctor. A doctor treats many patients. An appointment represents this consultation.
- **Navigability**: From any appointment, you can access both the patient and the doctor.
- **Flexibility**: The association is independent — patients and doctors can exist without appointments.

---

## Requirement 10: Constructors

### What was done
Every entity implements three constructor types:

| Constructor | Purpose |
|---|---|
| **Default** (no-arg) | Required by JPA for entity instantiation |
| **Parameterized** | Convenient creation with essential fields |
| **Copy constructor** | Creates a defensive copy of an object |

Example from `Patient`:
```java
public Patient() {}                                    // Default (JPA)
public Patient(Long id, String fn, String ln, ...) {}  // Parameterized
public Patient(Patient other) { super(other); ... }    // Copy
```

### Why each constructor
- **Default**: Mandated by JPA specification — Hibernate uses it to create entities before populating fields via reflection.
- **Parameterized**: Allows concise, readable object creation in business code (e.g., `new Patient(1L, "John", "Doe", ...)`).
- **Copy**: Defensive programming — creates an independent copy to prevent external mutation of internal state.
