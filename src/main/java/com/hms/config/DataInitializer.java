package com.hms.config;

import com.hms.entity.*;
import com.hms.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final ReceptionistRepository receptionistRepository;
    private final DepartmentRepository departmentRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           PatientRepository patientRepository, DoctorRepository doctorRepository,
                           NurseRepository nurseRepository, ReceptionistRepository receptionistRepository,
                           DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        this.receptionistRepository = receptionistRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            log.info("Data already exists, skipping initialization");
            return;
        }

        if (userRepository.count() == 0) {
            log.info("Initializing default users...");
            userRepository.save(new User("admin", passwordEncoder.encode("admin123"), "ADMIN"));
            userRepository.save(new User("doctor", passwordEncoder.encode("doctor123"), "DOCTOR"));
            userRepository.save(new User("nurse", passwordEncoder.encode("nurse123"), "NURSE"));
            userRepository.save(new User("receptionist", passwordEncoder.encode("reception123"), "RECEPTIONIST"));
            log.info("Default users created successfully");
        }

        Department cardiology = new Department();
        cardiology.setName("Cardiology");
        cardiology.setDescription("Heart and cardiovascular services");
        cardiology.setLocation("Building A, 2nd Floor");
        departmentRepository.save(cardiology);

        Department pediatrics = new Department();
        pediatrics.setName("Pediatrics");
        pediatrics.setDescription("Child healthcare services");
        pediatrics.setLocation("Building B, 1st Floor");
        departmentRepository.save(pediatrics);
        log.info("Departments created");

        Doctor doctor1 = new Doctor();
        doctor1.setFirstName("John");
        doctor1.setLastName("Smith");
        doctor1.setEmail("john.smith@hospital.com");
        doctor1.setPhone("+1234567890");
        doctor1.setAddress("123 Main St, Cityville");
        doctor1.setLicenseNumber("LIC-001");
        doctor1.setSpecialization("Cardiology");
        doctor1.setYearsOfExperience(15);
        doctor1.setDepartment(cardiology);
        doctorRepository.save(doctor1);

        Doctor doctor2 = new Doctor();
        doctor2.setFirstName("Sarah");
        doctor2.setLastName("Johnson");
        doctor2.setEmail("sarah.johnson@hospital.com");
        doctor2.setPhone("+1234567891");
        doctor2.setAddress("456 Oak Ave, Townsville");
        doctor2.setLicenseNumber("LIC-002");
        doctor2.setSpecialization("Pediatrics");
        doctor2.setYearsOfExperience(10);
        doctor2.setDepartment(pediatrics);
        doctorRepository.save(doctor2);
        log.info("Doctors created");

        Nurse nurse = new Nurse();
        nurse.setFirstName("Emily");
        nurse.setLastName("Davis");
        nurse.setEmail("emily.davis@hospital.com");
        nurse.setPhone("+1234567892");
        nurse.setAddress("789 Pine Rd, Villageburg");
        nurse.setLicenseNumber("RN-001");
        nurse.setShift("NIGHT");
        nurse.setDepartment("Cardiology");
        nurseRepository.save(nurse);
        log.info("Nurse created");

        Receptionist receptionist = new Receptionist();
        receptionist.setFirstName("Alice");
        receptionist.setLastName("Brown");
        receptionist.setEmail("alice.brown@hospital.com");
        receptionist.setPhone("+1234567893");
        receptionist.setAddress("321 Elm St, Hamletown");
        receptionist.setEmployeeCode("EMP-001");
        receptionist.setShift("MORNING");
        receptionistRepository.save(receptionist);
        log.info("Receptionist created");

        Patient patient1 = new Patient();
        patient1.setFirstName("Michael");
        patient1.setLastName("Wilson");
        patient1.setEmail("michael.wilson@email.com");
        patient1.setPhone("+9876543210");
        patient1.setAddress("555 Cedar Ln, Metropolis");
        patient1.setDateOfBirth(LocalDate.of(1985, 6, 15));
        patient1.setGender("Male");
        patient1.setBloodType("O+");
        patient1.setNationalId("NID-001");
        patient1.setPatientStatus("REGISTERED");
        patientRepository.save(patient1);

        Patient patient2 = new Patient();
        patient2.setFirstName("Kanjo");
        patient2.setLastName("Mwesigwa");
        patient2.setEmail("kanjo.mwesigwa@email.com");
        patient2.setPhone("+9876543211");
        patient2.setAddress("222 Birch Dr, Coastcity");
        patient2.setDateOfBirth(LocalDate.of(1992, 3, 22));
        patient2.setGender("Male");
        patient2.setBloodType("A+");
        patient2.setNationalId("NID-002");
        patient2.setPatientStatus("REGISTERED");
        patientRepository.save(patient2);

        Patient patient3 = new Patient();
        patient3.setFirstName("Jane");
        patient3.setLastName("Doe");
        patient3.setEmail("jane.doe@email.com");
        patient3.setPhone("+9876543212");
        patient3.setAddress("777 Walnut Ave, Bigcity");
        patient3.setDateOfBirth(LocalDate.of(1990, 11, 8));
        patient3.setGender("Female");
        patient3.setBloodType("B-");
        patient3.setNationalId("NID-003");
        patient3.setPatientStatus("REGISTERED");
        patientRepository.save(patient3);
        log.info("Patients created");
    }
}
