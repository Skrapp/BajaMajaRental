package com.nilsson.repo;


import com.nilsson.entity.rentable.Color;
import com.nilsson.entity.rentable.Decoration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DecorationRepo {
    void save(Decoration decoration);

    Optional<Decoration> findById(long id);

    List<Decoration> findAll();

    List<Decoration> findFilteredAvailableByDate(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate, List<Color> colors);

    List<Decoration> findFilteredDecorations(String searchWord, double minimumRate, double maximumRate, List<Color> colors);

    List<Decoration> findAllFiltered(String searchWord, Boolean requireAvailableToday, Double minimumRate, Double maximumRate, List<Color> colors);
}
