package appointmentbooking;

import java.util.Arrays;
import java.util.stream.Collectors;

// Driver class to demonstrate functionality
public class AppointmentBookingDemo {
    public static void main(String[] args) {
        AppointmentBookingSystem system = new AppointmentBookingSystem();

        // Register Doctors
        system.registerDoctor("Curious", Speciality.CARDIOLOGIST);
        system.registerDoctor("Dreadful", Speciality.DERMATOLOGIST);
        system.registerDoctor("Daring", Speciality.DERMATOLOGIST);
        system.registerDoctor("Siva kumar", Speciality.GENERAL_PHYSICIAN);

        // Mark Doctor Availability
        system.markDoctorAvailability("Curious",
                Arrays.asList("09:30-10:00", "12:30-13:00", "16:00-16:30"));
        system.markDoctorAvailability("Dreadful",
                Arrays.asList("09:30-10:00", "12:30-13:00", "16:00-16:30"));
        system.markDoctorAvailability("Daring",
                Arrays.asList("11:30-12:00", "14:00-14:30"));

        system.markDoctorAvailability("Siva kumar",
                Arrays.asList("09:30-10:30", "12:30-13:00", "16:00-16:30"));


        // Register Patients
        system.registerPatient("PatientA");
        system.registerPatient("PatientB");
        system.registerPatient("PatientC");

        // Show Available Doctors by Speciality
        System.out.println("\nAvailable Cardiologists:");
        system.showAvailableBySpeciality(Speciality.CARDIOLOGIST)
                .forEach(doc -> {
                    System.out.println("Dr. " + doc.getName() + ": " +
                            doc.getAvailableSlots().stream()
                                    .map(slot -> "(" + slot.getStartTime() + ")")
                                    .collect(Collectors.toList()));
                });

        // Book Appointments
        int bookingId1 = system.bookAppointment("PatientA", "Curious", "12:30");
        System.out.println("Booked first appointment");

        System.out.println("\nAvailable Cardiologists after first booking:");
        system.showAvailableBySpeciality(Speciality.CARDIOLOGIST)
                .forEach(doc -> {
                    System.out.println("Dr. " + doc.getName() + ": " +
                            doc.getAvailableSlots().stream()
                                    .map(slot -> "(" + slot.getStartTime() + ")")
                                    .collect(Collectors.toList()));
                });

        // Cancel Booking
        system.cancelBooking(bookingId1);

        System.out.println("\nAvailable Cardiologists after cancellation:");
        system.showAvailableBySpeciality(Speciality.CARDIOLOGIST)
                .forEach(doc -> {
                    System.out.println("Dr. " + doc.getName() + ": " +
                            doc.getAvailableSlots().stream()
                                    .map(slot -> "(" + slot.getStartTime() + ")")
                                    .collect(Collectors.toList()));
                });

        // Book another appointment
        system.bookAppointment("PatientB", "Curious", "12:30");

        // Demonstrate Trending Doctor (Bonus Feature)
        System.out.println("\nTrending Doctor:");
        Doctor trendingDoctor = system.getTrendingDoctor();
        if (trendingDoctor != null) {
            System.out.println("Dr. " + trendingDoctor.getName() + " with "
                    + trendingDoctor.getAppointmentCount() + " appointments");
        }

        // Test Waitlist Functionality
        System.out.println("\nTesting Waitlist:");
        // Book multiple appointments to same slot to trigger waitlist
        int firstBookingId=system.bookAppointment("PatientA", "Daring", "14:00");

        int waitlistBooking = system.bookAppointment("PatientC", "Daring", "14:00");
        System.out.println("\nBookings for PatientC:");
        Patient patientC = system.findPatient("PatientC");
        patientC.getPatientBookings().forEach(booking -> {
            System.out.println("Booking ID: " + booking.getBookingId() +
                    ", Doctor: " + booking.getDoctor().getName() +
                    ", Time Slot: " + booking.getTimeSlot().getStartTime());
        });

        // Cancel original booking to test waitlist
        system.cancelBooking(firstBookingId);
        System.out.println("\nBookings for PatientC:");
        patientC.getPatientBookings().forEach(booking -> {
            System.out.println("Booking ID: " + booking.getBookingId() +
                    ", Doctor: " + booking.getDoctor().getName() +
                    ", Time Slot: " + booking.getTimeSlot().getStartTime());
        });

    }
}