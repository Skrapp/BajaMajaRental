package com.nilsson.repo;

import com.nilsson.entity.rentable.BajaMaja;

import java.util.List;
import java.util.Optional;

public interface BajaMajaRepo {
    void save(BajaMaja bajaMaja);

    Optional<BajaMaja> findById(long id);

    List<BajaMaja> findAll();

    List<BajaMaja> findAllFiltered(String searchWord, boolean requireAvailable, double minimumRate, double maximumRate, boolean requireHandicap);
}
