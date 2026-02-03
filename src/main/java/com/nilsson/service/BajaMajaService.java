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
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name krävs");
        if (rentalRate < 0) throw new IllegalArgumentException("rentalrate måste vara över 0");
        if(numberOfStalls < 0) throw new IllegalArgumentException("NumberOfStalls måste vara mer än 0");

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

    public List<BajaMaja> findAllFiltered(String searchWord, boolean requireAvailable, double minimumRate, double maximumRate, boolean requireHandicap) {
        return bajaMajaRepo.findAllFiltered(searchWord, requireAvailable,minimumRate, maximumRate, requireHandicap);
    }
}
