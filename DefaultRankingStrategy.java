package appointmentbooking;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Default Ranking Strategy (by start time)
public class DefaultRankingStrategy implements RankingStrategy {
    @Override
    public List<Doctor> rankDoctors(List<Doctor> doctors) {
        return doctors.stream()
                .sorted(Comparator.comparing(doctor -> 
                    doctor.getAvailableSlots().isEmpty() ? 
                    LocalTime.MAX : 
                    doctor.getAvailableSlots().get(0).getStartTime()))
                .collect(Collectors.toList());
    }
}
