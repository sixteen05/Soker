package com.redit.models;

/**
 * Created By : Lalit Umbarkar
 * Created On : 08/10/20
 */

public enum CardSuite {

    CLUBS, DIAMONDS, HEARTS, SPADES;

    public static CardSuite[] getAllSuites() {
        return CardSuite.class.getEnumConstants();
    }
}
