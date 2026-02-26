package com.nilsson.repo;

import com.nilsson.entity.rentable.Platform;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlatformRepo {
    void save(Platform platform);
    Optional<Platform> findById(Long id);
    List<Platform> findAll();


    List<Platform> findFilteredAvailableByDateAndByBajaMaja(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate, Long bajaMajaId);
    List<Platform> findFilteredAvailableByDate(LocalDateTime availableDate, String searchWord, double minimumRate, double maximumRate);
    List<Platform> findFilteredByBajaMaja(String searchWord,double minimumRate, double maximumRate, Long bajaMajaId);
    List<Platform> findFiltered(String searchWord,double minimumRate, double maximumRate);

}
