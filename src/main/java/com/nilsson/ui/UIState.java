package com.nilsson.ui;

public enum UIState {
    /*Välj från menyn:
                    [1] Lista uthyrningsobjekt
                    [3] Uppdatera uthyrningsobjekt
                    [2] Boka uthyrning
                    [4] Avsluta uthyrning
                    [3] Lista kunder
                    [4] Skapa ny kund
                    [5] Uppdatera befintlig kund*/
    MAIN_MENU,
    LIST_RENTAL_OBJECTS,
    CREATE_RENTAL_OBJECT,
    UPDATE_RENTAL_OBJECT,
    RENT_RENTAL_OBJECT,
    END_RENTAL,
    LIST_CUSTOMERS,
    CREATE_NEW_CUSTOMER,
    UPDATE_CUSTOMER,
    QUIT
}
