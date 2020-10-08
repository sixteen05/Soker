package com.redit.models;

import java.util.Objects;

/**
 * Created By : Lalit Umbarkar
 * Created On : 08/10/2020
 */

public class Card {

    CardSuite suite;
    CardType type;

    public Card(CardSuite suite, CardType type) {
        this.suite = suite;
        this.type = type;
    }

    public int getValue() {
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass())
            return false;
        Card otherCard = (Card) other;
        return suite == otherCard.suite && type == otherCard.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suite, type);
    }
}

