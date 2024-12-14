package appointmentbooking;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Doctor Class
public class Doctor extends User {
    private Speciality speciality;
    private List<TimeSlot> availableSlots;
    private List<Booking> doctorBookings;
    private int appointmentCount;

    public Doctor(String name, Speciality speciality) {
        super(name);
        this.speciality = speciality;
        this.availableSlots = new ArrayList<>();
        this.doctorBookings = new ArrayList<>();
        this.appointmentCount = 0;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public List<TimeSlot> getAvailableSlots() {
        return availableSlots.stream()
                .filter(TimeSlot::isAvailable)
                .collect(Collectors.toList());
    }

    public void addAvailableSlot(TimeSlot slot) {
        if (!availableSlots.contains(slot)) {
            availableSlots.add(slot);
        }
    }

    public TimeSlot getDefinedSlotByStartTime(LocalTime startTime) {
        return availableSlots.stream()
                .filter(slot -> slot.getStartTime().equals(startTime))
                .findFirst()
                .orElse(null);
    }

    public void markSlotBooked(TimeSlot slot) {
        TimeSlot existingSlot = availableSlots.stream()
                .filter(s -> s.equals(slot))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));
        existingSlot.setAvailable(false);
        appointmentCount++;
    }

    public void markSlotAvailable(TimeSlot slot) {
        TimeSlot existingSlot = availableSlots.stream()
                .filter(s -> s.equals(slot))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));
        existingSlot.setAvailable(true);
        appointmentCount--;
    }

    public List<Booking> getDoctorBookings() {
        return doctorBookings;
    }

    public void addBooking(Booking booking) {
        doctorBookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        doctorBookings.remove(booking);
    }

    public int getAppointmentCount() {
        return appointmentCount;
    }
}
