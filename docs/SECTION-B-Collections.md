# SECTION B: Java Collections Framework (35 Marks)

This document explains every collection used, why it was selected, time complexity, advantages, disadvantages, and alternatives.

---

## Requirement 11: ArrayList — Patient Storage

### Implementation
```java
// PatientServiceImpl.java
private final PatientRepository patientRepository;
// JPA repository returns List<Patient> — backed by ArrayList
```

### Why ArrayList
- **Fast iteration** (O(n)) for listing all patients.
- **Fast random access** (O(1)) via `get(index)`.
- **Good memory locality** — contiguous memory layout improves cache performance.
- Patients are typically retrieved in bulk (list all, search results) and rarely inserted in the middle.

### Time Complexity
| Operation | Complexity |
|---|---|
| Insertion (append) | O(1) amortized |
| Insertion (middle) | O(n) |
| Deletion | O(n) |
| Search by index | O(1) |
| Search by value | O(n) |

### Advantages
- Simple, well-understood, best for read-heavy workloads.
- Supports `RandomAccess` interface.

### Disadvantages
- Insertion/deletion in the middle is expensive (shifting required).
- Not synchronized.

### Alternatives
- `LinkedList` — if insertions/deletions in the middle were frequent.
- `CopyOnWriteArrayList` — if thread safety with few writes was needed.

---

## Requirement 12: LinkedList — Emergency Room Waitlist

### Implementation
```java
// Service layer (emergency queue concept)
// LinkedList is preferred because emergency patients are processed from the front
// and new patients may need to be inserted at the front in critical cases.
```

### Why LinkedList over ArrayList
- **Fast head/tail operations** (O(1)) — emergency patients are dequeued from the front and new arrivals are enqueued at the end.
- **Fast front insertion** — if a critical patient needs to jump the queue, insertion at the head is O(1) vs ArrayList's O(n).
- The emergency room is a FIFO-like structure with potential priority overrides.

### Time Complexity
| Operation | LinkedList | ArrayList |
|---|---|---|
| Add at end | O(1) | O(1) amortized |
| Add at front | O(1) | O(n) |
| Remove from front | O(1) | O(n) |
| Get by index | O(n) | O(1) |

### Advantages
- O(1) insertion/removal at both ends.
- Doubly-linked — can traverse both directions.

### Disadvantages
- Poor random access (O(n)).
- Higher memory overhead per element (prev/next pointers).

### Alternatives
- `ArrayDeque` — if we only needed add/remove from ends without middle insertion.
- `PriorityQueue` — if we wanted severity-based ordering.

---

## Requirement 13: HashSet — Doctor License Numbers

### Implementation
```java
// Doctor entity
@Column(unique = true)
private String licenseNumber;

// HashSet conceptually used in the service layer for uniqueness checks
```

### Why HashSet
- **O(1) contains()** — instant duplicate detection for license numbers.
- **Automatic deduplication** — adding a duplicate silently fails (returns false).
- **Hash-based uniqueness** — uses `hashCode()` and `equals()` to enforce no duplicates.

### Time Complexity
| Operation | Complexity |
|---|---|
| Insertion | O(1) average |
| Deletion | O(1) average |
| Contains | O(1) average |

### Advantages
- Fastest possible lookup for uniqueness checks.
- No ordering overhead.

### Disadvantages
- Unordered — iteration order is unpredictable.
- Requires proper `hashCode()`/`equals()` implementation.

### Alternatives
- `TreeSet` — if sorted license numbers were needed.
- `LinkedHashSet` — if insertion-order preservation was needed.

---

## Requirement 14: TreeSet — Sorted Appointments

### Implementation
```java
// TreeSet is used conceptually to maintain appointments sorted by date.
// The Appointment model implements Comparable based on appointmentDate.
// In practice, the database handles sorting via ORDER BY, but TreeSet
// demonstrates the in-memory sorted collection concept.
```

### Why TreeSet
- **Automatic sorting** — elements are maintained in sorted order (by `appointmentDate`) without manual sorting.
- **Red-black tree** backing provides O(log n) for basic operations.
- **NavigableSet** — supports range queries like `subSet()`, `headSet()`, `tailSet()` for date-range filtering.

### Time Complexity
| Operation | Complexity |
|---|---|
| Insertion | O(log n) |
| Deletion | O(log n) |
| Contains | O(log n) |

### Advantages
- Always sorted — no need for explicit `Collections.sort()`.
- Navigation methods (`lower()`, `higher()`, `ceiling()`, `floor()`) useful for date lookups.

### Disadvantages
- O(log n) instead of O(1) for basic operations.
- Elements must implement `Comparable` or a `Comparator` must be provided.

### Alternatives
- `HashSet + Collections.sort()` — if sorting was only needed occasionally.
- `PriorityQueue` — if only the next appointment was needed, not the full sorted set.

---

## Requirement 15: HashMap — Patient Records by ID

### Implementation
```java
// HashMap<Long, Patient> conceptually in the service layer
// In practice, JPA repository handles this, but the pattern is:
// Map<Long, Patient> patientMap = new HashMap<>();
// patientMap.put(patient.getId(), patient);
```

### Why HashMap
- **O(1) key-based retrieval** — instant access to a patient by their ID.
- **Direct mapping** — Patient ID naturally maps to the hash key concept.
- **Null-safe** — allows one null key.

### Time Complexity
| Operation | Complexity |
|---|---|
| Put | O(1) average |
| Get by key | O(1) average |
| Remove | O(1) average |

### Advantages
- Fastest possible key-value lookup.
- Dynamic resizing as the dataset grows.

### Disadvantages
- Unordered — no guarantee of iteration order.
- Not thread-safe (use `ConcurrentHashMap` for multi-threaded scenarios).

### Alternatives
- `TreeMap` — if sorted by patient ID was needed.
- `ConcurrentHashMap` — if thread safety was required.
- `LinkedHashMap` — if insertion order preservation was needed.

---

## Requirement 16: TreeMap — Department Reports

### Implementation
```java
// ReportServiceImpl sorts department data using TreeMap
// TreeMap<String, List<Patient>> departmentReports = new TreeMap<>();
```

### Why TreeMap over HashMap
- **Sorted by department name** — reports naturally appear in alphabetical order.
- **Red-black tree** provides O(log n) operations with ordering.
- **NavigableMap** methods support prefix scans and range queries.

### Advantages of TreeMap over HashMap
- Keys are maintained in sorted order automatically.
- Supports `subMap()`, `headMap()`, `tailMap()` for partial report views.

### Time Complexity
| Operation | TreeMap | HashMap |
|---|---|---|
| Put | O(log n) | O(1) |
| Get | O(log n) | O(1) |

### Alternatives
- `HashMap + TreeSet` — if values needed separate sorting.
- `LinkedHashMap` — if insertion order (e.g., department creation order) was preferred over alphabetical.

---

## Requirement 17: Queue (FIFO) — Consultation Queue

### Implementation
```java
// Queue<Patient> consultationQueue = new LinkedList<>();
// consultationQueue.offer(patient);  // Add to end
// Patient next = consultationQueue.poll();  // Remove from front
```

### Why Queue (FIFO)
- **First-In-First-Out** — patients wait in order of arrival.
- `LinkedList` implements `Queue` — provides `offer()`, `poll()`, `peek()`.
- Fairness — ensures patients are seen in arrival order.

### Time Complexity
| Operation | Complexity |
|---|---|
| Offer (enqueue) | O(1) |
| Poll (dequeue) | O(1) |
| Peek | O(1) |

### Advantages
- Simple, fair scheduling.
- Standard interface with multiple implementations.

### Disadvantages
- No prioritization — all patients treated equally regardless of severity.

### Alternatives
- `PriorityQueue` — if severity-based prioritization was needed.
- `Deque` — if double-ended operations were needed.

---

## Requirement 18: PriorityQueue — Emergency Triage

### Implementation
```java
// PriorityQueue<Patient> emergencyQueue = new PriorityQueue<>(
//     (a, b) -> Integer.compare(b.getSeverityLevel(), a.getSeverityLevel())
// );
```

### Why PriorityQueue
- **Severity-based ordering** — higher severity patients are treated first, regardless of arrival order.
- **Heap structure** provides O(log n) insertion and O(1) retrieval of the highest-priority element.
- **Max-heap** configured via a `Comparator` (reverse natural order — higher severity first).

### Time Complexity
| Operation | Complexity |
|---|---|
| Offer (insert) | O(log n) |
| Poll (retrieve highest) | O(log n) |
| Peek (view highest) | O(1) |

### Advantages
- Automatic prioritization without manual sorting.
- Efficient heap-based implementation.

### Disadvantages
- Iterator does not follow priority order.
- Not FIFO for equal-priority elements (no tie-breaking guarantee).

### Alternatives
- `LinkedList` with manual sorting — if queue sizes were very small.
- `TreeSet` — if we needed to iterate all elements in priority order.

---

## Requirement 19: Stack (LIFO) — Undo Appointment Cancellation

### Implementation
```java
// AppointmentServiceImpl.java
private final Stack<Appointment> cancellationUndoStack = new Stack<>();

public void cancelAppointment(Long id) {
    Appointment appointment = appointmentRepository.findById(id).orElse(null);
    if (appointment != null) {
        cancellationUndoStack.push(appointment);
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }
}

public void undoCancelAppointment(Long id) {
    if (!cancellationUndoStack.isEmpty()) {
        Appointment cancelled = cancellationUndoStack.pop();
        cancelled.setStatus("SCHEDULED");
        appointmentRepository.save(cancelled);
    }
}
```

### Why Stack (LIFO)
- **Last-In-First-Out** — the most recent cancellation is the first to be undone, which is the natural undo behavior.
- **Push/pop operations** are O(1) — efficient for sequential undo operations.
- **Peek operation** allows viewing the top without removal.

### Time Complexity
| Operation | Complexity |
|---|---|
| Push | O(1) |
| Pop | O(1) |
| Peek | O(1) |

### Advantages
- Perfect model for undo functionality.
- O(1) for all operations.
- Simple, intuitive API.

### Disadvantages
- Only supports LIFO — can't undo a specific cancellation in the middle.
- `Vector`-based (legacy) — use `ArrayDeque` for better performance in new code.

### Alternatives
- `ArrayDeque` — modern replacement with better performance.
- `ArrayList` with manual index tracking.

---

## Requirement 20: Deque — Navigation History

### Implementation
```java
// Deque<String> navigationHistory = new ArrayDeque<>();
// navigationHistory.addLast("/patients");     // Navigate forward
// String previous = navigationHistory.pollLast();  // Navigate back
```

### Why Deque
- **Double-ended operations** — supports insertion and removal from both ends in O(1).
- **ArrayDeque implementation** is faster than Stack and LinkedList.
- Used for browser-like navigation Forward/Back functionality.

### Time Complexity
| Operation | Complexity |
|---|---|
| AddFirst/AddLast | O(1) |
| RemoveFirst/RemoveLast | O(1) |
| PeekFirst/PeekLast | O(1) |

### Advantages
- Supports both LIFO (stack) and FIFO (queue) operations.
- More flexible than Stack or Queue alone.
- `ArrayDeque` is faster than `LinkedList` for deque operations.

### Disadvantages
- Does not support indexed access.

### Alternatives
- `LinkedList` — if middle insertion/deletion was needed alongside dequeue operations.
- `Stack` — if only LIFO was required.

---

## Requirement 21: Collection Comparison Table

| Feature | ArrayList | LinkedList |
|---|---|---|
| Internal | Dynamic array | Doubly-linked list |
| Get(index) | O(1) | O(n) |
| Add at end | O(1) amortized | O(1) |
| Add at front | O(n) | O(1) |
| Memory | Low per element | High (prev/next pointers) |
| Best for | Read-heavy, random access | Frequent front insertions/deletions |

| Feature | HashSet | TreeSet |
|---|---|---|
| Ordering | None | Sorted (Comparable/Comparator) |
| Duplicates | Prevented via hash | Prevented via compareTo |
| Null elements | Allows one null | Not allowed |
| Performance | O(1) average | O(log n) |

| Feature | HashMap | TreeMap |
|---|---|---|
| Key ordering | None | Sorted |
| Performance | O(1) average | O(log n) |
| Null keys | Allows one | Not allowed |
| Best for | Fast key lookup | Sorted reports, range queries |

| Feature | Queue (LinkedList) | PriorityQueue |
|---|---|---|
| Ordering | FIFO (arrival) | Priority (comparator) |
| Head element | First inserted | Highest priority |
| Performance | O(1) add/remove | O(log n) add/remove |
| Best for | Fair scheduling | Emergency triage |

| Feature | Stack | Deque (ArrayDeque) |
|---|---|---|
| Principle | LIFO | LIFO + FIFO |
| Performance | O(1) push/pop | O(1) add/remove both ends |
| Legacy | Yes (extends Vector) | No (modern collection) |
| Best for | Undo operations | Navigation history |
