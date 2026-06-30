# SECTION F: Enterprise Business Rules (20 Marks)

## Implemented Rules

### Rule 1: National Identification Number must be unique

**Implementation**: `@Column(unique = true)` on `Patient.nationalId`

```java
// Patient.java
@Column(unique = true)
private String nationalId;
```

**Why**: Database-level unique constraint ensures data integrity even if application logic fails. The constraint is enforced at the schema level — any duplicate insertion throws `DataIntegrityViolationException`, which is caught by `GlobalExceptionHandler`.

---

### Rule 2: Patient email addresses must be unique

**Implementation**: `@Column(unique = true)` on `Person.email`

```java
// Person.java
@Column(unique = true)
private String email;
```

**Why**: Same as Rule 1 — database-level uniqueness. Additionally, `PatientRepository.findByEmail()` returns `Optional<Patient>`, allowing the service layer to check for duplicates before persisting.

---

### Rule 3: A doctor cannot have overlapping appointments

**Implementation**: `AppointmentServiceImpl.createAppointment()`

```java
public Appointment createAppointment(Appointment appointment) {
    LocalDateTime start = appointment.getAppointmentDate();
    LocalDateTime end = start.plusHours(1);

    List<Appointment> doctorApps = appointmentRepository
        .findDoctorAppointmentsInRange(doctor.getId(), start, end);
    if (!doctorApps.isEmpty()) {
        throw new AppointmentConflictException(
            "Doctor has an overlapping appointment at this time");
    }
    // ... proceed with creation
}
```

**Why**: Business logic check before persistence prevents scheduling conflicts. The `findDoctorAppointmentsInRange` JPQL query checks if any appointment exists for the doctor within the requested time window. Throws a custom `AppointmentConflictException` which results in a `409 Conflict` response.

---

### Rule 4: A patient cannot book more than one appointment at the same time

**Implementation**: Same method, separate check:

```java
long patientCount = appointmentRepository
    .countPatientAppointmentsOnDate(patient.getId(), start);
if (patientCount > 0) {
    throw new AppointmentConflictException(
        "Patient already has an appointment at this time");
}
```

**Why**: Prevents double-booking. The `countPatientAppointmentsOnDate` JPQL query returns a count of existing appointments for the patient at the given time.

---

### Rule 5: Emergency patients must always receive higher priority

**Implementation**: `PriorityQueue<Patient>` in the service layer (triage queue):

```java
PriorityQueue<Patient> emergencyQueue = new PriorityQueue<>(
    (a, b) -> Integer.compare(b.getSeverityLevel(), a.getSeverityLevel())
);
```

Additionally, `EmergencyAdmission` (Template Method pattern) provides a specialized emergency workflow that prioritizes immediate treatment.

**Why**: The `PriorityQueue` orders patients by severity (higher severity = higher priority). The comparator reverses natural order so the most critical patient is at the head. Combined with the `EmergencyAdmission` workflow, emergency patients bypass normal wait times.

---

### Rule 6: Bills cannot be generated until consultation is completed

**Implementation**: `BillingServiceImpl.generateInvoice()`

```java
public Invoice generateInvoice(Long appointmentId) {
    Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
    if (appointment == null || !"COMPLETED".equals(appointment.getStatus())) {
        return null; // Cannot generate bill for non-completed appointments
    }
    // ... create invoice
}
```

**Why**: The check verifies the appointment status is `COMPLETED` before allowing invoice generation. Returns `400 Bad Request` if the condition is not met.

---

### Rule 7: Prescriptions cannot be issued before a diagnosis is recorded

**Implementation**: `PrescriptionServiceImpl.createPrescription()`

```java
public Prescription createPrescription(Prescription prescription) {
    List<Appointment> completed = appointmentRepository
        .findByPatientId(prescription.getPatient().getId())
        .stream()
        .filter(a -> "COMPLETED".equals(a.getStatus()) || "DIAGNOSED".equals(a.getStatus()))
        .toList();
    if (completed.isEmpty()) {
        throw new IllegalStateException(
            "Cannot issue prescription: no diagnosis recorded for patient");
    }
    // ... create prescription
}
```

**Why**: Ensures a diagnosis (represented by a completed appointment) exists before medication can be prescribed. Prevents premature or unauthorized prescription issuance.

---

### Rule 8: Laboratory tests cannot be performed without a doctor's request

**Implementation**: `LaboratoryTestServiceImpl.requestTest()`

```java
public LaboratoryTest requestTest(LaboratoryTest test) {
    if (test.getRequestedByDoctor() == null || test.getRequestedByDoctor().isBlank()) {
        throw new IllegalArgumentException(
            "Laboratory tests require a doctor's request");
    }
    // ... create lab test
}
```

**Why**: Validates that every lab test has a requesting doctor. The `requestedByDoctor` field stores the doctor's name or ID. Combined with the `LaboratoryTestDTO` validation, this ensures the request is always associated with a physician.

---

### Rule 9: Only administrators may delete patient records

**Implementation**: Spring Security role-based access

```java
// SecurityConfig.java
.requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
```

Additionally, controller-level `@PreAuthorize` annotations can be used for fine-grained control:

```java
@PreAuthorize("hasRole('ADMIN')")
public void delete(Long id) {
    patientService.delete(id);
}
```

**Why**: Security enforced at the framework level — non-ADMIN users receive a `403 Forbidden` response when attempting any DELETE operation.

---

### Rule 10: Patient records must be soft deleted rather than permanently removed

**Implementation**: Hibernate `@SQLDelete` + `@SQLRestriction`

```java
// Person.java
@SQLDelete(sql = "UPDATE person SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
private boolean deleted;
```

**How it works**:
1. `@SQLDelete` overrides the default DELETE SQL with an UPDATE that sets `deleted = true`
2. `@SQLRestriction` adds `WHERE deleted = false` to all SELECT queries, automatically filtering out soft-deleted records
3. The `deleted` boolean field tracks the soft-delete status

**Why soft delete**:
- **Data preservation**: Medical records must be retained for legal and audit purposes
- **Recoverability**: Accidentally deleted records can be restored by setting `deleted = false`
- **Audit trail**: Maintains a complete history of all patients who have ever been registered
- **Referential integrity**: Prevents orphaned records in related tables (appointments, prescriptions, etc.)

---

## Summary of Enforcement Approaches

| Rule | Enforcement Layer | Mechanism |
|---|---|---|
| National ID unique | Database | `@Column(unique=true)` |
| Email unique | Database | `@Column(unique=true)` |
| No overlapping appointments | Service | JPQL overlap check |
| No patient double-booking | Service | JPQL count check |
| Emergency priority | Service + Algorithms | `PriorityQueue` comparator |
| Bills after consultation | Service | Status check |
| Prescriptions after diagnosis | Service | Appointment status check |
| Lab tests require doctor | Service | Field validation |
| Only ADMIN delete | Security | Spring Security matcher |
| Soft delete | Database (ORM) | `@SQLDelete` + `@SQLRestriction` |
