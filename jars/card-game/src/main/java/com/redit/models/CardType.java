package com.redit.models;

/**
 * Created By : Lalit Umbarkar
 * Created On : 08/10/20
 */

public enum CardType {

    ACE(1, 14), KING(13), QUEEN(12), JACK(11),
    TENS(10), NINE(9), EIGHT(8), SEVEN(7), SIX(6),
    FIVE(5), FOUR(4), THREE(3), TWO(2);

    int numberValue, faceValue;

    CardType(int numberValue, int faceValue) {
        this.numberValue = numberValue;
        this.faceValue = faceValue;
    }

    CardType(int numberValue) {
        this.numberValue = numberValue;
        this.faceValue = numberValue;
    }

    public static CardType[] getAllTypes() {
        return CardType.class.getEnumConstants();
    }

}
