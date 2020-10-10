package com.redit.util;

import com.redit.models.Card;

import java.util.Comparator;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 */

public class CardComparators {

    public static class CardFaceValueCompare implements Comparator<Card> {

        @Override
        public int compare(Card card1, Card card2) {
            if (card1.getFaceValue() < card2.getFaceValue()) return -1;
            else if (card1.getFaceValue() > card2.getFaceValue()) return 1;
            return 0;
        }
    }

    public static class CardNumberValueCompare implements Comparator<Card> {

        @Override
        public int compare(Card card1, Card card2) {
            if (card1.getNumberValue() < card2.getNumberValue()) return -1;
            else if (card1.getNumberValue() > card2.getNumberValue()) return 1;
            return 0;
        }
    }

}
