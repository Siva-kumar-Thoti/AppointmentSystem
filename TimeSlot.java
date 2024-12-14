package appointmentbooking;

import java.time.LocalTime;
import java.util.Objects;

// Class to represent a Time Slot
public class TimeSlot {
    private LocalTime startTime;
    private boolean isAvailable;

    public TimeSlot(LocalTime startTime) {
        this.startTime = startTime;
        this.isAvailable = true;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return startTime.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(startTime, timeSlot.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime);
    }
}
