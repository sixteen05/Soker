package com.redit.models;

import com.redit.util.CardComparators;
import com.redit.util.CoreUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class CardDeal {

    public List<Card> cards = new ArrayList<>();

    public CardDeal(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public CardDeal(Card card1, Card card2, Card card3) {
        this.cards.add(card1);
        this.cards.add(card2);
        this.cards.add(card3);
    }

    /**
     * Creates a randomly generated card deal of given size, after draw the size would be 1,
     * Before Draw it would be card size as per game
     */
    public static CardDeal createDeal(int size, List<Card> cardDeck) {
        List<Card> cardsFetched = new ArrayList<>();
        for (int cardFetch = 0; cardFetch < size; cardFetch++) {
            cardsFetched.add(CoreUtils.randomChoose(cardDeck));
        }
        return new CardDeal(cardsFetched);
    }

    /**
     * Score is determined by the rules in the game, assuming game has not reached draw stage yet.
     * The more the score, more likely that deal is winner.
     * <p>
     * Below are the rules
     * - A trail (three cards of the same number) is the highest possible combination.
     * - The next highest is a sequence (numbers in order, e.g., 4,5,6. A is considered to have a value of 1).
     * - The next highest is a pair of cards (e.g.: two Kings or two 10s).
     * - If all else fails, the top card (by number value wins).
     * <p>
     * Scoring
     * - Trail score ranges: 1001 - 1013
     * - Sequence of 3 score ranges: 501 - 511
     * - Sequence of 2 score ranges: 101 - 112
     * - Top card ranges: 1 - 13
     **/
    public int getScore() {
        // Check for trail
        boolean areAllCardsSameFace = true;
        Card firstCard = cards.get(0);
        for (Card eachCard : cards) {
            if (firstCard.getFaceValue() != eachCard.getFaceValue()) {
                areAllCardsSameFace = false;
                break;
            }
        }
        if (areAllCardsSameFace)
            return firstCard.getFaceValue() + 1000;
        //
        // Check for 3 sequence
        List<Card> numberSortedCards = this.cards
                .stream()
                .sorted(new CardComparators.CardNumberValueCompare())
                .collect(Collectors.toList());
        boolean areAllCardsInSequence = true;
        int cardNumberValue = numberSortedCards.get(0).getNumberValue();
        for (Card eachCard : numberSortedCards.subList(1, numberSortedCards.size())) {
            if (eachCard.getNumberValue() == cardNumberValue + 1) {
                cardNumberValue += 1;
            } else {
                areAllCardsInSequence = false;
                break;
            }
        }
        if (areAllCardsInSequence)
            return numberSortedCards.get(0).getNumberValue() + 500;
        //
        // Check for 2 pair
        List<Card> faceSortedCards = this.cards
                .stream()
                .sorted(new CardComparators.CardFaceValueCompare())
                .collect(Collectors.toList());
        boolean doPairsExist = false;
        int pairFaceValue = -1;
        for (int cardIndex = 0; cardIndex < faceSortedCards.size() - 1; cardIndex++) {
            if (faceSortedCards.get(cardIndex).getFaceValue() == faceSortedCards.get(cardIndex + 1).getFaceValue()) {
                doPairsExist = true;
                pairFaceValue = faceSortedCards.get(cardIndex).getFaceValue();
                break;
            }
        }
        if (doPairsExist)
            return pairFaceValue + 10;
        //
        // else score is same as top card
        return numberSortedCards.get(numberSortedCards.size() - 1).getNumberValue();
    }

    /**
     * <b>Assumes that deal only has one card as Draw deals only compare one card</b><br/>
     * Score is determined by the rules in the game after a draw has occurred.
     * The more the score, more likely that deal is winner.
     * <p>
     * Scoring:
     * - Top card ranges: 1 - 13
     **/
    public int getDrawScore() {
        return this.cards.get(0).getNumberValue();
    }

    @Override
    public String toString() {
        return "CardDeal{ cards=" + cards + "}";
    }
}
