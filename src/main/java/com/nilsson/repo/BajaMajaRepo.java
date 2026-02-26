package com.nilsson.repo;

import com.nilsson.entity.rentable.BajaMaja;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BajaMajaRepo {
    void save(BajaMaja bajaMaja);

    Optional<BajaMaja> findById(long id);

    List<BajaMaja> findAll();

    List<BajaMaja> findFilteredAvailableByDateAndHandicap(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate);
    List<BajaMaja> findFilteredAvailableByDate(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate);
    List<BajaMaja> findFilteredHandicap(String searchWord, double minimumRate, double maximumRate);
    List<BajaMaja> findFiltered(String searchWord, double minimumRate, double maximumRate);


}
