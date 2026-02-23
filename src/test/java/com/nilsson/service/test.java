/*
package com.nilsson.service;

public class test {
    package com.nilsson.service;

import com.nilsson.entity.rentable.BajaMaja;
import com.nilsson.repo.BajaMajaRepo;
import com.nilsson.repo.RentalRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

    class BajaMajaServiceTest {
        private BajaMajaRepo bajaMajaRepo;
        private BajaMajaService bajaMajaService;

findAllFiltered kombinerar filter korrekt
        requireHandicap fungerar
        availableToday filtrerar bort uthyrda

        @BeforeEach
        void setUp(){
            bajaMajaRepo = mock(BajaMajaRepo.class);
            bajaMajaService = new BajaMajaService(bajaMajaRepo);
        }


        // Test to confirm createBajaMaja saves BajaMaja with correct values
        @Test
        void createBajaMaja_savesBajaMajaWithCorrectValues() {
            String name = "Luxury";
            double rentalRate = 500.0;
            int numberOfStalls = 4;

            BajaMaja createdBajaMaja = bajaMajaService.createBajaMaja(name, rentalRate, numberOfStalls);

            assertNotNull(createdBajaMaja);
            assertEquals(name, createdBajaMaja.getName());
            assertEquals(rentalRate, createdBajaMaja.getRentalRate(), 0.01);
            assertEquals(numberOfStalls, createdBajaMaja.getNumberOfStalls());
            verify(bajaMajaRepo).save(createdBajaMaja);
        }

            void createBajaMaja_throwsExceptionForInvalidName() {
                assertThrows(IllegalArgumentException.class, () -> bajaMajaService.createBajaMaja(null, 100, 1));
                assertThrows(IllegalArgumentException.class, () -> bajaMajaService.createBajaMaja(" ", 100, 1));
            }

            // Test to confirm createBajaMaja throws IllegalArgumentException for negative rental rate
            @Test
            void createBajaMaja_throwsExceptionForNegativeRentalRate() {
                assertThrows(IllegalArgumentException.class, () -> bajaMajaService.createBajaMaja("Luxury", -100, 1));
            }

            // Test to confirm createBajaMaja throws IllegalArgumentException for negative number of stalls
            @Test
            void createBajaMaja_throwsExceptionForNegativeNumberOfStalls() {
                assertThrows(IllegalArgumentException.class, () -> bajaMajaService.createBajaMaja("Luxury", 100, -1));
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
        }

    }
*/
