package appointmentbooking;

// Class to represent a Booking
public class Booking {
    private static int bookingIdCounter = 1;
    private int bookingId;
    private Patient patient;
    private Doctor doctor;
    private TimeSlot timeSlot;

    public Booking(Patient patient, Doctor doctor, TimeSlot timeSlot) {
        this.bookingId = bookingIdCounter++;
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }
}
