package com.nilsson.repo;


import com.nilsson.entity.rentable.Color;
import com.nilsson.entity.rentable.Decoration;

import java.util.List;
import java.util.Optional;

public interface DecorationRepo {
    void save(Decoration decoration);

    Optional<Decoration> findById(long id);

    List<Decoration> findAll();

    List<Decoration> findAllFiltered(String searchWord, boolean requireAvailableToday, double minimumRate, double maximumRate, List<Color> colors);
}
