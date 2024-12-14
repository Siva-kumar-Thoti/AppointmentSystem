package appointmentbooking;

import java.util.List;

// Ranking Strategy Interface
public interface RankingStrategy {
    List<Doctor> rankDoctors(List<Doctor> doctors);
}
