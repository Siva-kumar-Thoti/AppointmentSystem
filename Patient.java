package appointmentbooking;

import java.util.ArrayList;
import java.util.List;

// Patient Class
public class Patient extends User {
    private List<Booking> patientBookings;

    public Patient(String name) {
        super(name);
        this.patientBookings = new ArrayList<>();
    }

    public List<Booking> getPatientBookings() {
        return patientBookings;
    }

    public void addBooking(Booking booking) {
        patientBookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        patientBookings.remove(booking);
    }
}
