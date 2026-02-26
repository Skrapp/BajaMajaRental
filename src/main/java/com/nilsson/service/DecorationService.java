package com.nilsson.service;

import com.nilsson.entity.rentable.Color;
import com.nilsson.entity.rentable.Decoration;
import com.nilsson.entity.rentable.RentalObject;
import com.nilsson.exception.RentalObjectNotFoundException;
import com.nilsson.repo.DecorationRepo;

import java.time.LocalDateTime;
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
        if (decoration.getName() == null || decoration.getName().isBlank()) throw new IllegalArgumentException("Namn krävs");
        if (decoration.getRentalRate() < 0) throw new IllegalArgumentException("Hyrpriset måste vara 0 eller mer ");

        decorationRepo.save(decoration);
        return decoration;
    }

    public Decoration findById(Long id){
        if(id <= 0) throw new IllegalArgumentException("ID är inte godkänt.");

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
     * @param requireAvailableByDate Sätt true om det endast ska lista artiklar som inte är bokade ett specifikt datum
     * @param minimumRate Minsta kostnaden av artikel. Sätt 0 eller mindre för att inte filtrera enligt minsta
     * @param maximumRate Största kostnaden för en artikel. Sätt 0 eller mindre för att inte filtrera enligt största
     * @param colors De färger som ska inkluderas i filtreringen. Sätt alla färger, tom lista eller null för att inte filtrera bort
     * @return returnerar en lista av Decorations enligt filtreringen
     */
    public List<Decoration> findFiltered(String searchWord, boolean requireAvailableByDate, LocalDateTime availableDate, double minimumRate, double maximumRate, List<Color> colors) {
        if(minimumRate > maximumRate && maximumRate != 0) throw new IllegalArgumentException("Minimipriset får ej vara lägre än maximipriset");
        if(requireAvailableByDate && availableDate == null) throw new IllegalArgumentException("Datum får inte vara null om man ska söka efter datum");

        if(searchWord == null || searchWord.isBlank()) searchWord = "";
        if(colors == null || colors.isEmpty())  colors = List.of(Color.values());

        //Om man inte söker efter nåt speciellt så vill man ha allt
        if(searchWord.isBlank() &&minimumRate <= 0 && maximumRate <= 0 && !requireAvailableByDate && colors.containsAll(List.of(Color.values()))) {
            return findAll();
        }

        //1 000 000 känns som ett tillräckligt högt tak
        if(maximumRate <= 0) maximumRate = 1000000.0;

        if(requireAvailableByDate){
            return decorationRepo.findFilteredAvailableByDate(availableDate,searchWord,minimumRate,maximumRate,colors);
        }

        return decorationRepo.findFiltered(searchWord,minimumRate, maximumRate, colors);
    }
}
