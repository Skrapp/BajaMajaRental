package com.nilsson.entity.rentable;

public enum Color {
    RED ("röd"),
    BLUE ("blå"),
    GREEN ("grön"),
    YELLOW ("gul"),
    ORANGE ("orange"),
    PURPLE ("lila"),
    BLACK ("svart"),
    WHITE ("vit"),
    PINK ("rosa"),
    BROWN ("brun"),
    GOLD("guld"),
    SILVER("silver");

    private final String swedish;

    Color(String swedish) {
        this.swedish = swedish;
    }

    public String getSwedish() {
        return swedish;
    }
}
