package com.nilsson.service;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.Platform;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.repo.BajaMajaRepo;
import com.nilsson.repo.PlatformRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PlatformService {
    private final PlatformRepo platformRepo;
    private final BajaMajaRepo bajaMajaRepo;

    public PlatformService(PlatformRepo platformRepo, BajaMajaRepo bajaMajaRepo) {
        this.platformRepo = platformRepo;
        this.bajaMajaRepo = bajaMajaRepo;
    }

    public Platform createPlatform(String name, String description, double rentalRate) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Namn krävs");
        if (rentalRate < 0) throw new IllegalArgumentException("Hyrpriset måste vara 0 eller mer ");

        Platform platform = new Platform(name.trim(), description, rentalRate);

        platformRepo.save(platform);
        return platform;
    }

    public Platform update(Platform platform){
        if (platform.getName() == null || platform.getName().isBlank()) throw new IllegalArgumentException("Namn krävs");
        if (platform.getRentalRate() < 0) throw new IllegalArgumentException("Hyrpriset måste vara 0 eller mer ");

        platformRepo.save(platform);
        return platform;
    }

    public Platform findById(Long id){
        if(id <= 0) throw new IllegalArgumentException("ID är inte godkänt.");

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
     * @param requireAvailableByDate Sätt true om det endast ska lista artiklar som inte är bokade idag
     * @param availableDate Datum för att se om artikel är tillgänglig, endast aktuell när requireAvailableByDate är true
     * @param minimumRate Minsta kostnaden av artikel. Sätt 0 för att inte filtrera enligt minsta
     * @param maximumRate Största kostnaden för en artikel. Sätt 0 för att inte filtrera enligt största
     * @param bajaMajaSuitableId BajaMaja som artikeln passar till. Sätt 0 eller mindre för att inte filtrera enligt passande
     * @return returnerar en lista av Platforms enligt filtreringen
     */
    public List<Platform> findFiltered(String searchWord, boolean requireAvailableByDate, LocalDateTime availableDate, double minimumRate, double maximumRate, long bajaMajaSuitableId) {
        if(minimumRate > maximumRate && maximumRate != 0) throw new IllegalArgumentException("Minimipriset får ej vara lägre än maximipriset");
        if(requireAvailableByDate && availableDate == null) throw new IllegalArgumentException("Datum får inte vara null om man ska söka efter datum");

        if(searchWord == null || searchWord.isBlank()) searchWord = "";

        //Om man inte söker efter nåt speciellt så vill man ha allt
        if(searchWord.isBlank() && !requireAvailableByDate && minimumRate <= 0 && maximumRate <= 0 && bajaMajaSuitableId <= 0)
            return findAll();

        //1 000 000 känns som ett tillräckligt högt tak
        if(maximumRate <= 0) maximumRate = 1000000.0;

        if(requireAvailableByDate){
            if(bajaMajaSuitableId > 0)
                return platformRepo.findFilteredAvailableByDateAndByBajaMaja(availableDate, searchWord, minimumRate, maximumRate, bajaMajaSuitableId);
            return platformRepo.findFilteredAvailableByDate(availableDate, searchWord, minimumRate, maximumRate);
        }
        if(bajaMajaSuitableId > 0)
            return platformRepo.findFilteredByBajaMaja(searchWord, minimumRate, maximumRate, bajaMajaSuitableId);

        return platformRepo.findFiltered(searchWord,minimumRate, maximumRate);
    }

    public void addBajaMaja(Long platformId, long bajaMajaId) {
        Platform platform = platformRepo.findById(platformId)
                .orElseThrow(() -> new RentalObjectNotFoundException("Platform finns inte"));

        BajaMaja bajaMaja = bajaMajaRepo.findById(bajaMajaId)
                .orElseThrow(() -> new RentalObjectNotFoundException("BajaMaja finns inte"));

        platform.addBajaMaja(bajaMaja);

        platformRepo.save(platform);

    }
}
