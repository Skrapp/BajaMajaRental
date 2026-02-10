package com.nilsson.service;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.repo.BajaMajaRepo;

import java.util.List;
import java.util.Optional;

public class BajaMajaService {
    private final BajaMajaRepo bajaMajaRepo;

    public BajaMajaService(BajaMajaRepo bajaMajaRepo) {
        this.bajaMajaRepo = bajaMajaRepo;
    }

    public BajaMaja createBajaMaja(String name, double rentalRate, int numberOfStalls){
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Namn krävs");
        if (rentalRate < 0) throw new IllegalArgumentException("Hyrpriset måste vara 0 eller mer ");
        if(numberOfStalls < 0) throw new IllegalArgumentException("Antal  måste vara 0 eller mer");

        BajaMaja bajaMaja = new BajaMaja(name.trim(), rentalRate, numberOfStalls);
        bajaMajaRepo.save(bajaMaja);
        return bajaMaja;
    }

    public BajaMaja update(BajaMaja bajaMaja){
        bajaMajaRepo.save(bajaMaja);
        return bajaMaja;
    }

    public BajaMaja findById(Long id){
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
     * @param requireAvailableToday Sätt true om det endast ska lista artiklar som inte är bokade idag
     * @param minimumRate Minsta kostnaden av artikel. Sätt 0 för att inte filtrera enligt minsta
     * @param maximumRate Största kostnaden för en artikel. Sätt 0 för att inte filtrera enligt största
     * @param requireHandicap Sätt true för att endast inkludera handikappanpassade bajamajor
     * @return returnerar en lista av BajaMajas enligt filtreringen
     */
    public List<BajaMaja> findAllFiltered(String searchWord, boolean requireAvailableToday, double minimumRate, double maximumRate, boolean requireHandicap) {
        if(searchWord == null) throw new IllegalArgumentException("Sökordet får inte vara null");
        if(minimumRate > maximumRate && maximumRate != 0) throw new IllegalArgumentException("Minimumpriset får ej vara lägre än maximumpriset");

        //Om man inte söker efter nåt speciellt så vill man ha allt
        if(searchWord.isBlank() && !requireAvailableToday && minimumRate <= 0 && maximumRate <= 0 && !requireHandicap)return findAll();

        return bajaMajaRepo.findAllFiltered(searchWord, requireAvailableToday,minimumRate, maximumRate, requireHandicap);
    }
}
