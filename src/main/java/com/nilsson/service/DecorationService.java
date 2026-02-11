package com.nilsson.service;

import com.nilsson.entity.rentable.Color;
import com.nilsson.entity.rentable.Decoration;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.repo.DecorationRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DecorationService {
    private final DecorationRepo decorationRepo;

    public DecorationService(DecorationRepo decorationRepo) {
        this.decorationRepo = decorationRepo;
    }

    public Decoration createDecoration(String name, String description, double rentalRate, Color color){
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Namn krävs");
        if (rentalRate < 0) throw new IllegalArgumentException("Hyrpriset måste vara 0 eller mer ");

        Decoration decoration = new Decoration(name.trim(), description, rentalRate, color);
        decorationRepo.save(decoration);
        return decoration;
    }

    public Decoration update(Decoration decoration){
        decorationRepo.save(decoration);
        return decoration;
    }

    public Decoration findById(Long id){
        Optional<Decoration> decorationOptional = decorationRepo.findById(id);

        if(decorationOptional.isEmpty()) throw new RentalObjectNotFoundException(RentalObject.DECORATION, id);

        return decorationOptional.get();
    }

    public List<Decoration> findAll(){
        return decorationRepo.findAll();
    }

    /**
     *Filtrerar enligt parametrar.
     * @param searchWord Söker i beskrivning och mail. Skriv "" för att inte filtrera enligt sökord
     * @param requireAvailableToday Sätt true om det endast ska lista artiklar som inte är bokade idag
     * @param minimumRate Minsta kostnaden av artikel. Sätt 0 eller mindre för att inte filtrera enligt minsta
     * @param maximumRate Största kostnaden för en artikel. Sätt 0 eller mindre för att inte filtrera enligt största
     * @param colors De färger som ska inkluderas i filtreringen. Sätt alla färger, tom lista eller null för att inte filtrera bort
     * @return returnerar en lista av Decorations enligt filtreringen
     */
    public List<Decoration> findAllFiltered(String searchWord, boolean requireAvailableToday, double minimumRate, double maximumRate, List<Color> colors) {
        if(searchWord == null) throw new IllegalArgumentException("Sökordet får inte vara null");
        if(minimumRate > maximumRate && maximumRate != 0) throw new IllegalArgumentException("Minimumpriset får ej vara lägre än maximumpriset");

        //Om man inte söker efter nåt speciellt så vill man ha allt
        if(searchWord.isBlank()
                && !requireAvailableToday
                && minimumRate <= 0
                && maximumRate <= 0
                && (colors == null || colors.isEmpty() || colors.equals(Arrays.stream(Color.values()).toList())))
            return findAll();

        return decorationRepo.findAllFiltered(searchWord, requireAvailableToday,minimumRate, maximumRate, colors);
    }
}
