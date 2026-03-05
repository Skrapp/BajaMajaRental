package com.nilsson.service;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.repo.BajaMajaRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BajaMajaService {
    private final BajaMajaRepo bajaMajaRepo;

    public BajaMajaService(BajaMajaRepo bajaMajaRepo) {
        this.bajaMajaRepo = bajaMajaRepo;
    }

    public BajaMaja createBajaMaja(String name, double rentalRate, int numberOfStalls, boolean isHandicap){
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Namn krävs");
        if (rentalRate < 0) throw new IllegalArgumentException("Hyrpriset måste vara 0 eller mer ");
        if(numberOfStalls < 0) throw new IllegalArgumentException("Antal  måste vara 0 eller mer");

        BajaMaja bajaMaja = new BajaMaja(name.trim(), rentalRate, numberOfStalls, isHandicap);
        bajaMajaRepo.save(bajaMaja);
        return bajaMaja;
    }

    public BajaMaja update(BajaMaja bajaMaja){
        if (bajaMaja.getId() == null) throw new IllegalArgumentException("Denna BajaMaja har inte tidigare sparats, skapa en ny");
        if (bajaMaja.getName() == null || bajaMaja.getName().isBlank()) throw new IllegalArgumentException("Namn krävs");
        if (bajaMaja.getRentalRate() < 0) throw new IllegalArgumentException("Hyrpriset måste vara 0 eller mer ");
        if (bajaMaja.getNumberOfStalls() < 0) throw new IllegalArgumentException("Antal  måste vara 0 eller mer");

        bajaMajaRepo.save(bajaMaja);
        return bajaMaja;
    }

    public BajaMaja findById(Long id){
        if(id <= 0) throw new IllegalArgumentException("UthyrningsObjektID är inte godkänt.");

        Optional<BajaMaja> bajaMajaOptional = bajaMajaRepo.findById(id);
        if(bajaMajaOptional.isEmpty()) throw new RentalObjectNotFoundException(RentalObject.BAJAMAJA, id);
        return bajaMajaOptional.get();
    }

    public List<BajaMaja> findAll(){
        return bajaMajaRepo.findAll();
    }

    /**
     *Filtrerar enligt parametrar.
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param requireAvailableByDate Sätt true om det endast ska lista artiklar som inte är bokade ett specifikt datum
     * @param availableDate Datum för att se om artikel är tillgänglig, endast aktuell när requireAvailableByDate är true
     * @param minimumRate Minsta kostnaden av artikel. Sätt 0 för att inte filtrera enligt minsta
     * @param maximumRate Största kostnaden för en artikel. Sätt 0 för att inte filtrera enligt största
     * @param requireHandicap Sätt true för att endast inkludera handikappanpassade bajamajor
     * @return returnerar en lista av BajaMajas enligt filtreringen
     */
    public List<BajaMaja> findFiltered(String searchWord, boolean requireAvailableByDate, LocalDateTime availableDate,
                                       double minimumRate, double maximumRate, boolean requireHandicap) {
        if(minimumRate > maximumRate && maximumRate != 0) throw new IllegalArgumentException("Minimipriset får ej vara lägre än maximipriset");
        if(requireAvailableByDate && availableDate == null) throw new IllegalArgumentException("Datum får inte vara null om man ska söka efter datum");

        if(searchWord == null || searchWord.isBlank()) searchWord = "";

        //Om man inte söker efter nåt speciellt så vill man ha allt
        if(searchWord.isBlank() && !requireAvailableByDate && minimumRate <= 0 && maximumRate <= 0 && !requireHandicap)
            return findAll();

        //1 000 000 känns som ett tillräckligt högt tak
        if(maximumRate <= 0) maximumRate = 1000000.0;

        if(requireAvailableByDate){
            if(requireHandicap)
                return bajaMajaRepo.findFilteredAvailableByDateAndHandicap(availableDate, searchWord, minimumRate, maximumRate);
            return bajaMajaRepo.findFilteredAvailableByDate(availableDate, searchWord, minimumRate, maximumRate);
        }
        if(requireHandicap)
            return bajaMajaRepo.findFilteredHandicap(searchWord, minimumRate, maximumRate);

        return bajaMajaRepo.findFiltered(searchWord,minimumRate, maximumRate);
    }
}
