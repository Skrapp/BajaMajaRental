package com.nilsson.service;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.repo.BajaMajaRepo;
import com.nilsson.repo.RentalRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BajaMajaServiceTest {
    private BajaMajaRepo bajaMajaRepo;
    private BajaMajaService bajaMajaService;

    /*findAllFiltered kombinerar filter korrekt
    requireHandicap fungerar
    availableToday filtrerar bort uthyrda*/
    @BeforeEach
    void setUp(){
        bajaMajaRepo = mock(BajaMajaRepo.class);
        bajaMajaService = new BajaMajaService(bajaMajaRepo);
    }

    @Test
    void createBajaMaja_SavesCorrectValues(){
        String name = "Dubbeldäckarn";
        double rentalRate = 300;
        int numOfStalls = 2;

        BajaMaja bajaMaja = bajaMajaService.createBajaMaja(name, rentalRate, numOfStalls);

        assertNotNull(bajaMaja);
        assertEquals(name, bajaMaja.getName());
        assertEquals(rentalRate, bajaMaja.getRentalRate());
        assertEquals(numOfStalls, bajaMaja.getNumberOfStalls());
        verify(bajaMajaRepo).save(bajaMaja);
    }

    @Test
    void updateBajaMaja_SavesCorrectValues(){
        String name = "Dubbeldäckarn";
        double rentalRate = 300;
        int numOfStalls = 2;
        BajaMaja bajaMaja = new BajaMaja(name, rentalRate, numOfStalls);
        setIdViaReflection(bajaMaja, 12L);

        BajaMaja updatedBajaMaja = bajaMajaService.update(bajaMaja);

        assertNotNull(updatedBajaMaja);
        assertEquals(name, updatedBajaMaja.getName());
        assertEquals(rentalRate, updatedBajaMaja.getRentalRate());
        assertEquals(numOfStalls, updatedBajaMaja.getNumberOfStalls());
        assertEquals(12L, updatedBajaMaja.getId());
        verify(bajaMajaRepo).save(bajaMaja);
    }

    @Test
    void createBajaMaja_ThrowsExceptionIfNameIsInvalid(){
        assertThrows(IllegalArgumentException.class, () -> bajaMajaService.createBajaMaja(null, 100, 1));
        assertThrows(IllegalArgumentException.class, () -> bajaMajaService.createBajaMaja("  ", 100, 1));
        verify(bajaMajaRepo, never()).save(any());
    }

    @Test
    void updateBajaMaja_ThrowsExceptionIfNameIsInvalid(){
        String name = null;
        double rentalRate = 300;
        int numOfStalls = 2;
        BajaMaja bajaMaja = new BajaMaja(name, rentalRate, numOfStalls);
        setIdViaReflection(bajaMaja, 12L);

        assertThrows(IllegalArgumentException.class, () -> bajaMajaService.update(bajaMaja));

        bajaMaja.setName("  ");
        assertThrows(IllegalArgumentException.class, () -> bajaMajaService.update(bajaMaja));

        verify(bajaMajaRepo, never()).save(any());
    }

    //kör metoden findAll om inget specifik ska filtreras
    @Test
    void findAllFiltered_usesFindAllIfNoFilterIsNeeded() {
        BajaMaja b1 = new BajaMaja("Classic", 100, 1);
        BajaMaja b2 = new BajaMaja("Deluxe", 200, 2);
        List<BajaMaja> bajaMajaList = new ArrayList<>();
        bajaMajaList.add(b1);
        bajaMajaList.add(b2);

        when(bajaMajaRepo
                .findAll())
                .thenReturn(bajaMajaList);

        List<BajaMaja> bajaMajaFilterList = bajaMajaService
                .findAllFiltered(" ", false, 0, 0, false);

        assertNotNull(bajaMajaFilterList);
        assertEquals(bajaMajaList, bajaMajaFilterList);

        //Se till så att findAll kördes och inte findAllByFiltered
        verify(bajaMajaRepo).findAll();
        verify(bajaMajaRepo, never()).findAllFiltered(anyString(), anyBoolean(), anyDouble(), anyDouble(), anyBoolean());
    }

    private static void setIdViaReflection(Object entity, Long id) {
        try {
            // Hämtar fältet med namnet "id" även om det är private
            var field = entity.getClass().getDeclaredField("id");

            // Tillåter åtkomst trots att fältet är private
            field.setAccessible(true);

            // Sätter värdet på fältet "id" i objektet
            field.set(entity, id);
        } catch (Exception e) {
            // Om det går fel vill vi faila snabbt och tydligt i testet
            throw new RuntimeException("Kunde inte sätta id via reflection", e);
        }
    }
}