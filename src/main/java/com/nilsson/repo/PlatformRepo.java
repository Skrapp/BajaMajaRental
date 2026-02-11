package com.nilsson.repo;

import com.nilsson.entity.rentable.Platform;

import java.util.List;
import java.util.Optional;

public interface PlatformRepo {
    void save(Platform platform);
    Optional<Platform> findById(Long id);
    List<Platform> findAll();

    List<Platform> findAllFiltered(String searchWord, boolean requireAvailable, double minimumRate, double maximumRate, Long bajaMajaId);
}
