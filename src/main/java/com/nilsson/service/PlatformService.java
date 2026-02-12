package com.nilsson.service;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Platform;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.repo.PlatformRepo;

import java.util.List;
import java.util.Optional;

public class PlatformService {
    private final PlatformRepo platformRepo;
    private final BajaMajaService bajaMajaService;

    public PlatformService(PlatformRepo platformRepo, BajaMajaService bajaMajaService) {
        this.platformRepo = platformRepo;
        this.bajaMajaService = bajaMajaService;
    }

    public Platform createPlatform(String name, String description, double rentalRate, List<Long> bajaMajaIds) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Namn krävs");
        if (rentalRate < 0) throw new IllegalArgumentException("Hyrpriset måste vara 0 eller mer ");

        Platform platform = new Platform(name.trim(), description, rentalRate);
        for(Long bajaMajaId : bajaMajaIds){
            platform.addBajaMaja(bajaMajaService.findById(bajaMajaId));
        }
        platformRepo.save(platform);
        return platform;
    }

    public Platform update(Platform platform){
        platformRepo.save(platform);
        return platform;
    }

    public Platform findById(Long id){
        Optional<Platform> platformOptional = platformRepo.findById(id);

        if(platformOptional.isEmpty()) throw new RentalObjectNotFoundException(RentalObject.BAJAMAJA, id);

        return platformOptional.get();
    }

    public List<Platform> findAll(){
        return platformRepo.findAll();
    }

    /**
     *Filtrerar enligt parametrar.
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param requireAvailableToday Sätt true om det endast ska lista artiklar som inte är bokade idag
     * @param minimumRate Minsta kostnaden av artikel. Sätt 0 för att inte filtrera enligt minsta
     * @param maximumRate Största kostnaden för en artikel. Sätt 0 för att inte filtrera enligt största
     * @param bajaMajaSuitableId BajaMaja som artikeln passar till. Sätt 0 eller mindre för att inte filtrera enligt passande
     * @return returnerar en lista av Platforms enligt filtreringen
     */
    public List<Platform> findAllFiltered(String searchWord, boolean requireAvailableToday, double minimumRate, double maximumRate, Long bajaMajaSuitableId) {
        if(searchWord == null) throw new IllegalArgumentException("Sökordet får inte vara null");
        if(minimumRate > maximumRate && maximumRate != 0) throw new IllegalArgumentException("Minimumpriset får ej vara lägre än maximumpriset");

        //Om man inte söker efter nåt speciellt så vill man ha allt
        if(searchWord.isBlank() && !requireAvailableToday && minimumRate <= 0 && maximumRate <= 0 && bajaMajaSuitableId <= 0)return findAll();

        return platformRepo.findAllFiltered(searchWord, requireAvailableToday,minimumRate, maximumRate, bajaMajaSuitableId);
    }
}
