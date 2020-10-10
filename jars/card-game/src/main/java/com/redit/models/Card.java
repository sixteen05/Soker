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

    public int getFaceValue() {
        return this.type.faceValue;
    }

    public int getNumberValue() {
        return this.type.numberValue;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) return false;
        Card otherCard = (Card) other;
        return suite == otherCard.suite && type == otherCard.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suite, type);
    }

    @Override
    public String toString() {
        return "Card{" + suite + "," + type + '}';
    }
}

