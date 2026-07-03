# SECTION E: Reports (25 Marks)

## Report Endpoints

All reports are exposed as REST endpoints via `ReportController` at `/api/v1/reports`:

| # | Endpoint | Return Type | Description |
|---|---|---|---|
| 1 | `GET /api/v1/reports/total-patients` | `Long` | Total number of registered patients |
| 2 | `GET /api/v1/reports/patients-per-department` | `Map<String, Long>` | Patient count grouped by department |
| 3 | `GET /api/v1/reports/admitted-today` | `Long` | Number of patients admitted today |
| 4 | `GET /api/v1/reports/doctor-workload` | `Map<String, Long>` | Appointment count per doctor |
| 5 | `GET /api/v1/reports/common-diseases` | `Map<String, Long>` | Most frequently diagnosed conditions |
| 6 | `GET /api/v1/reports/lab-statistics` | `Map<String, Long>` | Lab test counts by test name |
| 7 | `GET /api/v1/reports/revenue-by-department` | `Map<String, Double>` | Revenue aggregated by department |
| 8 | `GET /api/v1/reports/monthly-revenue` | `Map<String, Double>` | Revenue aggregated by month |
| 9 | `GET /api/v1/reports/age-distribution` | `Map<String, Long>` | Patient count by age bracket |
| 10 | `GET /api/v1/reports/busiest-doctors` | `Map<String, Long>` | Top 10 doctors by appointment count |
| 11 | `GET /api/v1/reports/collection-comparison` | `List<CollectionComparisonDTO>` | Java Collections Framework comparison table from Section B |

## Implementation Details

### ReportService Interface
```java
public interface ReportService {
    long getTotalPatients();
    Map<String, Long> getPatientsPerDepartment();
    long getPatientsAdmittedToday();
    Map<String, Long> getDoctorWorkload();
    Map<String, Long> getMostCommonDiseases();
    Map<String, Long> getLabTestStatistics();
    Map<String, Double> getRevenueByDepartment();
    Map<String, Double> getMonthlyRevenue();
    Map<String, Long> getPatientAgeDistribution();
    Map<String, Long> getTopBusiestDoctors();
    List<CollectionComparisonDTO> getCollectionComparisonTable();
}
```

### Data Sources

| Report | Data Source | Implementation |
|---|---|---|---|
| Total patients | `PatientRepository.count()` | JPA built-in method |
| Patients per department | Department → Doctor → Appointment → Patient | `TreeMap` via stream aggregation |
| Admitted today | `Patient.createdAt` | In-memory filter by `LocalDate.now()` |
| Doctor workload | `Doctor.getAppointments()` | Stream count per doctor |
| Most common diseases | `Appointment.reason` | `Collectors.groupingBy` with `TreeMap` |
| Lab test statistics | `LaboratoryTest.testName` | In-memory `HashMap` merge |
| Revenue by department | `Invoice.totalAmount` → Appointment → Doctor → Department | `TreeMap` with null-safe traversal |
| Monthly revenue | `Invoice.issueDate` | `TreeMap` grouped by month/year |
| Age distribution | `Patient.dateOfBirth` | Age calculation with 6 bucket ranges |
| Top 10 busiest doctors | `DoctorRepository.findBusiestDoctors()` | JPQL `ORDER BY SIZE(appointments)` |
| Collection comparison | Static comparison data | DTO rows grouped by collection pair |

### Why Map-based responses
- **Flexible**: No rigid DTO needed — new metrics can be added without schema changes
- **Self-describing**: Each map entry is `{ "label": value }` — the key names document the data
- **Lightweight**: Simple JSON without wrapper objects

### UML Diagrams
Generated using **PlantUML** — source files in `docs/diagrams/`:

| Diagram | File | Description |
|---|---|---|
| **Class Diagram** | `docs/diagrams/class-diagram.png` | All 15 entities with inheritance, composition, aggregation, and associations |
| **ER Diagram** | `docs/diagrams/er-diagram.png` | Database tables with columns, keys, and relationships |
| **Layer Diagram** | `docs/diagrams/layer-diagram.png` | Controller → Service → Repository → Database + design patterns |
| **Design Patterns** | `docs/diagrams/design-patterns.png` | Factory, Builder, Strategy, Observer, Adapter, Facade, Template Method |
