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

| Report | Data Source | Query Approach |
|---|---|---|
| Total patients | `PatientRepository.count()` | JPA built-in |
| Patients per department | Join Patient → Appointment → Doctor → Department | Aggregate query |
| Admitted today | Filter patients by admission date | JPQL date comparison |
| Doctor workload | Count appointments per doctor | `DoctorRepository.findBusiestDoctors()` |
| Most common diseases | Aggregate diagnosis records | JPQL GROUP BY |
| Lab test statistics | Group by test name | In-memory aggregation |
| Revenue by department | Join Invoice → Appointment → Doctor → Department | Sum group by department |
| Monthly revenue | Group by invoice issue date month | JPQL date extraction |
| Age distribution | Calculate age from dateOfBirth | Case-based bucketing |
| Top 10 busiest doctors | Sort by appointment count | `ORDER BY SIZE(d.appointments) DESC` |
| Collection comparison | Static Section B collection analysis | DTO rows grouped by compared collection pair |

### Why Map-based responses
- **Flexible**: No rigid DTO needed — new metrics can be added without schema changes
- **Self-describing**: Each map entry is `{ "label": value }` — the key names document the data
- **Lightweight**: Simple JSON without wrapper objects

### UML Diagrams
UML class diagrams should be generated separately using a tool like:
- **PlantUML** — Text-based UML generation
- **IntelliJ UML** — Built-in diagram generation from source
- **draw.io** — Manual diagram creation

Key diagrams to produce:
1. **Class diagram** — All entities, inheritance, relationships
2. **Layer diagram** — Controller → Service → Repository → Database
3. **Design pattern diagrams** — Strategy, Observer, Adapter, Template Method
4. **ER diagram** — Entity relationships with cardinalities
