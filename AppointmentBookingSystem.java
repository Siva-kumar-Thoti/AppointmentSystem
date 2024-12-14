package appointmentbooking;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

// Appointment Booking System
public class AppointmentBookingSystem {
    private List<Doctor> doctors;
    private List<Patient> patients;
    private List<Booking> bookings;
    private Map<TimeSlot, List<Patient>> waitlist;
    private RankingStrategy rankingStrategy;

    public AppointmentBookingSystem() {
        this.doctors = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.waitlist = new HashMap<>();
        this.rankingStrategy = new DefaultRankingStrategy();
    }

    public void registerDoctor(String name, Speciality speciality) {
        Doctor doctor = new Doctor(name, speciality);
        doctors.add(doctor);
        System.out.println("Welcome Dr. " + name + " !!");
    }

    public void registerPatient(String name) {
        Patient patient = new Patient(name);
        patients.add(patient);
    }

    public void markDoctorAvailability(String doctorName, List<String> timeSlots) {
        Doctor doctor = findDoctor(doctorName);
        
        for (String slotStr : timeSlots) {
            String[] parts = slotStr.split("-");
            LocalTime startTime = LocalTime.parse(parts[0]);
            LocalTime endTime = LocalTime.parse(parts[1]);
            
            // Validate slot duration is exactly 30 mins
            long duration = java.time.Duration.between(startTime, endTime).toMinutes();
            if (duration != 30) {
                System.out.println("\nSorry Dr. " + doctorName + " slots are 30 mins only\n");
                return;
            }
            
            TimeSlot slot = new TimeSlot(startTime);
            doctor.addAvailableSlot(slot);
        }
        
        System.out.println("Done Doc!");
    }

    public List<Doctor> showAvailableBySpeciality(Speciality speciality) {
        return rankingStrategy.rankDoctors(
            doctors.stream()
                .filter(d -> d.getSpeciality() == speciality && !d.getAvailableSlots().isEmpty())
                .collect(Collectors.toList())
        );
    }

    public int bookAppointment(String patientName, String doctorName, String timeSlotStr) {
        Patient patient = findPatient(patientName);
        Doctor doctor = findDoctor(doctorName);
        LocalTime startTime = LocalTime.parse(timeSlotStr);
        
        // Check if patient already has a booking in this time slot
        boolean hasConflictingBooking = patient.getPatientBookings().stream()
                .anyMatch(b -> b.getTimeSlot().getStartTime().equals(startTime));

        if (hasConflictingBooking) {
            System.out.println("\nCannot book two appointments in the same time slot");
            return -1;
        }

        // Check if the slot is defined by the doctor
        TimeSlot slot = doctor.getDefinedSlotByStartTime(startTime);

        if (slot == null) {
            System.out.println("\nSlot not defined by the doctor.\n");
            return -1;
        }

        // Check if the slot is available
        if (!slot.isAvailable()) {
            // Add to waitlist
            waitlist.computeIfAbsent(slot, k -> new ArrayList<>()).add(patient);
            System.out.println("\nSlot not available. Added to waitlist.\n");
            return -1;
        }
        
        // Book the appointment
        Booking booking = new Booking(patient, doctor, slot);
        bookings.add(booking);
        patient.addBooking(booking);
        doctor.addBooking(booking);
        doctor.markSlotBooked(slot);
        
        System.out.println("\nBooked. Booking id: " + booking.getBookingId());
        return booking.getBookingId();
    }

    public void cancelBooking(int bookingId) {
        Booking bookingToCancel = bookings.stream()
            .filter(b -> b.getBookingId() == bookingId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("\nBooking not found\n"));
        
        Doctor doctor = bookingToCancel.getDoctor();
        Patient patient = bookingToCancel.getPatient();
        TimeSlot slot = bookingToCancel.getTimeSlot();
        
        // Remove booking
        bookings.remove(bookingToCancel);
        patient.removeBooking(bookingToCancel);
        doctor.removeBooking(bookingToCancel);
        doctor.markSlotAvailable(slot);
        
        // Check waitlist
        List<Patient> waitlistForSlot = waitlist.get(slot);
        if (waitlistForSlot != null && !waitlistForSlot.isEmpty()) {
            Patient nextPatient = waitlistForSlot.remove(0); // Promote the first patient in the waitlist
            System.out.println("Promoting from waitlist: " + nextPatient.getName());
            Booking newBooking = new Booking(nextPatient, doctor, slot);
            bookings.add(newBooking);
            nextPatient.addBooking(newBooking);
            doctor.addBooking(newBooking);
            doctor.markSlotBooked(slot); // Mark the slot as booked again
        }
        
        System.out.println("\nBooking Cancelled");
    }

    public Doctor getTrendingDoctor() {
        return doctors.stream()
            .max(Comparator.comparing(Doctor::getAppointmentCount))
            .orElse(null);
    }

    private Doctor findDoctor(String name) {
        return doctors.stream()
            .filter(d -> d.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("\nDoctor not found\n"));
    }

    public Patient findPatient(String name) {
        return patients.stream()
            .filter(p -> p.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("\nPatient not found\n"));
    }
}
