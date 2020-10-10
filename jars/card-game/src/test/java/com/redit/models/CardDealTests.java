package com.redit.models;

import com.redit.exceptions.InvalidStateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class CardDealTests {

    private static Random random;
    private static Iterator<Integer> suiteGen, typeGen;

    @BeforeAll
    public static void setupRandom() {
        random = new Random(System.currentTimeMillis());
        suiteGen = random.ints(0, CardSuite.getAllSuites().length).iterator();
        typeGen = random.ints(0, CardType.getAllTypes().length).iterator();
    }

    public static Card generateRandomCard() {
        CardSuite suite = CardSuite.getAllSuites()[suiteGen.next()];
        CardType type = CardType.getAllTypes()[typeGen.next()];
        return new Card(suite, type);
    }

    public static Card getCardFromNumericValue(int value) {
        CardSuite[] allSuites = CardSuite.getAllSuites();
        for (CardType eachType : CardType.getAllTypes()) {
            if (eachType.numberValue == value) {
                return new Card(allSuites[suiteGen.next()], eachType);
            }
        }
        return null;
    }

    public static List<Card> getUniqueCards(int numCards) {
        Iterator<Integer> valueGen = random.ints(2, 14).iterator();
        // Unique numberValues
        Set<Integer> randomNumVal = new HashSet<>();
        while (randomNumVal.size() < numCards)
            randomNumVal.add(valueGen.next());
        return randomNumVal.stream().map(CardDealTests::getCardFromNumericValue).collect(Collectors.toList());
    }

    public static List<Card> getNonSeqUniqueCards(int numCards) {
        List<Card> randomNumVal = getUniqueCards(numCards);
        // Handles cards being in sequence
        List<Integer> randomNumValSorted = randomNumVal
                .stream()
                .map(Card::getNumberValue)
                .sorted()
                .collect(Collectors.toList());
        int minimum = randomNumValSorted.get(0);
        int maximum = randomNumValSorted.get(randomNumValSorted.size() - 1);
        if (numCards > 1 && minimum == maximum - numCards + 1)
            return getNonSeqUniqueCards(numCards);
        return randomNumVal;
    }

    public static Card duplicateCard(Card org) {
        return new Card(org.suite, org.type);
    }

    public static List<Card> getCardsInSequence(int numCards) {
        /// Gen numbers from [1, (13 - (numCards - 1))]
        int minNumericVal = random.nextInt(13 - (numCards - 1)) + 1;
        // Unique cards
        Set<Integer> numVal = new HashSet<>();
        while (numVal.size() < numCards)
            numVal.add(minNumericVal++);
        return numVal.stream().map(CardDealTests::getCardFromNumericValue).collect(Collectors.toList());
    }

    @Test
    public void createDealTest() {

        List<Card> existingDeck = new ArrayList<>();
        Card randomCard = generateRandomCard();
        existingDeck.add(randomCard);
        CardDeal deal = CardDeal.createDeal(1, existingDeck);
        assertThat(existingDeck)
                .as("Make sure deck is empty after creating a deal from equal sized deck" +
                        "\nDeck %s\nDeal %s", existingDeck, deal)
                .isEmpty();
        assertThat(deal).isEqualTo(new CardDeal(new ArrayList<>(Collections.singletonList(randomCard))));
    }

    @Test
    public void cardsConstructorsTest() {

        List<Card> threeCardDeck = getUniqueCards(3);
        CardDeal threeDeal = new CardDeal(threeCardDeck.get(0), threeCardDeck.get(1), threeCardDeck.get(2));
        CardDeal bulkDeal = new CardDeal(threeCardDeck);
        CardDeal createDeal = CardDeal.createDeal(3, threeCardDeck);
        assertThat(threeDeal)
                .as("Compare all three CardDeal instance creators" +
                                "\nThreeDeal %s\nBulkDeal %s\nCreateDeal %s\nthreeCardDeck %s",
                        threeDeal, bulkDeal, createDeal, threeCardDeck)
                .isEqualTo(bulkDeal)
                .isEqualTo(createDeal);
        assertThat(threeCardDeck)
                .as("Make sure deck is empty after creating deck" +
                        "\nthreeCardDeck %s", threeCardDeck)
                .isEmpty();
    }

    @Test
    public void randomDealTest() {

        int dealSize = 3, completeDeckSize = 5;
        List<Card> completeDeck = IntStream.range(0, completeDeckSize)
                .mapToObj(_i -> generateRandomCard())
                .collect(Collectors.toList());
        List<Card> deck = new ArrayList<>(completeDeck);
        CardDeal deal = CardDeal.createDeal(dealSize, deck);
        assertThat(deck)
                .as("Check random deal is correctly dealt from deck" +
                        "\nCompleteDeck %s\nDeal %s\nDeck %s", completeDeck, deal, deck)
                .hasSize(completeDeck.size() - dealSize)
                .containsAnyOf(completeDeck.toArray(new Card[2]));
        assertThat(deal.showDealtCards())
                .hasSize(dealSize)
                .containsAnyOf(completeDeck.toArray(new Card[3]));
    }

    @RepeatedTest(20)
    public void checkDrawScore() {
        List<Card> deck = getUniqueCards(1);
        CardDeal deal = new CardDeal(deck);
        try {
            assertThat(deal.getDrawScore())
                    .as("Compare Draw score with deal having one card" +
                            "\ndeck %s\ndeal%s", deck, deal)
                    .isEqualTo(deck.get(0).getNumberValue());
        } catch (InvalidStateException e) {
            assertThat(e).doesNotThrowAnyException();
        }
    }

    @Test
    public void compareCardDealTest() {

        int deckSize = 5;
        List<Card> cards = getUniqueCards(deckSize);
        CardDeal cardDeal1 = new CardDeal(cards);
        /// Shuffle deck, so that we compare correctly
        for (int repInt = deckSize - 1; repInt > 0; repInt--) {
            int index = random.nextInt(repInt + 1);
            Card c1 = cards.get(index);
            cards.set(index, cards.get(repInt));
            cards.set(repInt, c1);
        }
        CardDeal cardDeal2 = new CardDeal(cards);
        assertThat(cardDeal1)
                .as("Compare two cards deals are same if the same deck is given" +
                        "\ncards %s\nc1 %s\nc2 %s", cards, cardDeal1, cardDeal2)
                .isEqualTo(cardDeal2);
        Set<CardDeal> dealSet = new HashSet<>();
        dealSet.add(cardDeal1);
        dealSet.add(cardDeal2);
        assertThat(dealSet)
                .as("Also make sure that the hash is different as and they are not same deals" +
                        "\ndealSet\nc1 %s\nc2 %s", dealSet, cardDeal1, cardDeal2)
                .hasSize(2);
    }

    @Test
    public void checkDrawScoreMultipleCards() {
        List<Card> deck = getUniqueCards(5);
        CardDeal deal = new CardDeal(deck);
        try {
            assertThat(deal.getDrawScore())
                    .as("Compare Draw score with deal having more than one card" +
                            "\ndeck %s\ndeal%s", deck, deal)
                    .isNull();
        } catch (InvalidStateException e) {
            assertThat(e).hasMessage("Deal has more than one card. Cannot check draw score");
        }
    }


    @Test
    public void dealShowCardsTest() {

        int genCardsSize = 5;
        List<Card> genCards = IntStream.range(0, genCardsSize)
                .mapToObj(_i -> generateRandomCard())
                .collect(Collectors.toList());
        CardDeal cardDeal = new CardDeal(genCards);
        assertThat(cardDeal.showDealtCards())
                .as("Check random deal is correctly dealt without deck\nDeal %s\nDeck %s", cardDeal, genCards)
                .hasSize(genCards.size())
                .containsExactlyInAnyOrder(genCards.toArray(new Card[genCardsSize]));
    }

    @RepeatedTest(50)
    public void singleCardVsSingleCardTest() {

        List<Card> uniqueCard1 = getNonSeqUniqueCards(3),
                uniqueCard2 = getNonSeqUniqueCards(3);
        int dealValue1 = uniqueCard1.stream().map(Card::getNumberValue).max(Integer::compareTo).orElse(-1),
                dealValue2 = uniqueCard2.stream().map(Card::getNumberValue).max(Integer::compareTo).orElse(-1);
        CardDeal deal1 = new CardDeal(uniqueCard1),
                deal2 = new CardDeal(uniqueCard2);
        if (dealValue1 > dealValue2)
            assertThat(deal1.getScore())
                    .as("Check if single deal %s is greater than %s single deal", deal1, deal2)
                    .isGreaterThan(deal2.getScore());
        else if (dealValue1 == dealValue2)
            assertThat(deal1.getScore())
                    .as("Check if single deal %s is same as %s single deal", deal1, deal2)
                    .isEqualTo(deal2.getScore());
        else
            assertThat(deal1.getScore())
                    .as("Check if single deal %s is smaller than %s single deal", deal1, deal2)
                    .isLessThan(deal2.getScore());
    }

    @RepeatedTest(50)
    public void DoubleCardVsDoubleCardTest() {

        List<Card> dealCards1 = getNonSeqUniqueCards(2),
                dealCards2 = getNonSeqUniqueCards(2);
        int duplicateVal1 = dealCards1.get(0).getFaceValue(),
                duplicateVal2 = dealCards2.get(0).getFaceValue();
        dealCards1.add(duplicateCard(dealCards1.get(0)));
        dealCards2.add(duplicateCard(dealCards2.get(0)));
        CardDeal deal1 = new CardDeal(dealCards1),
                deal2 = new CardDeal(dealCards2);
        if (duplicateVal1 > duplicateVal2)
            assertThat(deal1.getScore())
                    .as("Check if double deal %s is greater than %s double deal", deal1, deal2)
                    .isGreaterThan(deal2.getScore());
        else if (duplicateVal1 == duplicateVal2)
            assertThat(deal1.getScore())
                    .as("Check if double deal %s is same as %s double deal", deal1, deal2)
                    .isEqualTo(deal2.getScore());
        else
            assertThat(deal1.getScore())
                    .as("Check if double deal %s is smaller than %s double deal", deal1, deal2)
                    .isLessThan(deal2.getScore());
    }

    @RepeatedTest(50)
    public void tripleCardVsTripleCardTest() {

        List<Card> dealCards1 = getNonSeqUniqueCards(1),
                dealCards2 = getNonSeqUniqueCards(1);
        int duplicateVal1 = dealCards1.get(0).getFaceValue(),
                duplicateVal2 = dealCards2.get(0).getFaceValue();
        dealCards1.add(duplicateCard(dealCards1.get(0)));
        dealCards1.add(duplicateCard(dealCards1.get(0)));
        dealCards2.add(duplicateCard(dealCards2.get(0)));
        dealCards2.add(duplicateCard(dealCards2.get(0)));
        CardDeal deal1 = new CardDeal(dealCards1),
                deal2 = new CardDeal(dealCards2);
        if (duplicateVal1 > duplicateVal2)
            assertThat(deal1.getScore())
                    .as("Check if triple deal %s is greater than %s triple deal", deal1, deal2)
                    .isGreaterThan(deal2.getScore());
        else if (duplicateVal1 == duplicateVal2)
            assertThat(deal1.getScore())
                    .as("Check if triple deal %s is same as %s triple deal", deal1, deal2)
                    .isEqualTo(deal2.getScore());
        else
            assertThat(deal1.getScore())
                    .as("Check if triple deal %s is smaller than %s triple deal", deal1, deal2)
                    .isLessThan(deal2.getScore());
    }

    @RepeatedTest(50)
    public void sequenceVsSequenceTest() {

        List<Card> dealCards1 = getCardsInSequence(3),
                dealCards2 = getCardsInSequence(3);
        int duplicateVal1 = dealCards1.get(0).getFaceValue(),
                duplicateVal2 = dealCards2.get(0).getFaceValue();
        dealCards1.add(duplicateCard(dealCards1.get(0)));
        dealCards1.add(duplicateCard(dealCards1.get(0)));
        dealCards2.add(duplicateCard(dealCards2.get(0)));
        dealCards2.add(duplicateCard(dealCards2.get(0)));
        CardDeal deal1 = new CardDeal(dealCards1),
                deal2 = new CardDeal(dealCards2);
        if (duplicateVal1 > duplicateVal2)
            assertThat(deal1.getScore())
                    .as("Check if sequence deal %s is greater than %s sequence deal", deal1, deal2)
                    .isGreaterThan(deal2.getScore());
        else if (duplicateVal1 == duplicateVal2)
            assertThat(deal1.getScore())
                    .as("Check if sequence deal %s is same as %s sequence deal", deal1, deal2)
                    .isEqualTo(deal2.getScore());
        else
            assertThat(deal1.getScore())
                    .as("Check if sequence deal %s is smaller than %s sequence deal", deal1, deal2)
                    .isLessThan(deal2.getScore());
    }

    @RepeatedTest(50)
    public void singleVsDoubleVsSequenceVsTripleTest() {

        List<Card> tripleCards = getNonSeqUniqueCards(1),
                cardsInSequence = getCardsInSequence(3),
                doubleCards = getNonSeqUniqueCards(2),
                singleCards = getNonSeqUniqueCards(3);
        tripleCards.add(duplicateCard(tripleCards.get(0)));
        tripleCards.add(duplicateCard(tripleCards.get(0)));
        doubleCards.add(duplicateCard(doubleCards.get(0)));

        CardDeal tripleDeal = new CardDeal(tripleCards),
                sequenceDeal = new CardDeal(cardsInSequence),
                doubleDeal = new CardDeal(doubleCards),
                singleDeal = new CardDeal(singleCards);
        assertThat(tripleDeal.getScore())
                .as("Compare all four types of deal against each other" +
                                "\ntripleDeal %s\nsequenceDeal %s\ndoubleDeal %s\nsingleDeal %s\n",
                        tripleDeal, sequenceDeal, doubleDeal, singleDeal)
                .isGreaterThan(sequenceDeal.getScore())
                .isGreaterThan(doubleDeal.getScore())
                .isGreaterThan(singleDeal.getScore());
    }

}
