package com.redit.models;

import org.junit.BeforeClass;
import org.junit.Test;

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

    @BeforeClass
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
        // Unique cards
        Set<Integer> randomNumVal = new HashSet<>();
        while (randomNumVal.size() < numCards)
            randomNumVal.add(valueGen.next());
        return randomNumVal.stream().map(CardDealTests::getCardFromNumericValue).collect(Collectors.toList());
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
        assertThat(existingDeck).isEmpty();
        assertThat(deal).isEqualTo(new CardDeal(new ArrayList<>(Collections.singletonList(randomCard))));
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
                .hasSize(completeDeck.size() - dealSize)
                .containsAnyOf(completeDeck.toArray(new Card[2]));
        assertThat(deal.showDealtCards())
                .hasSize(dealSize)
                .containsAnyOf(completeDeck.toArray(new Card[3]));
    }

    @Test
    public void dealShowCardsTest() {

        int genCardsSize = 5;
        List<Card> genCards = IntStream.range(0, genCardsSize)
                .mapToObj(_i -> generateRandomCard())
                .collect(Collectors.toList());
        CardDeal cardDeal = new CardDeal(genCards);
        assertThat(cardDeal.showDealtCards())
                .hasSize(genCards.size())
                .containsExactlyInAnyOrder(genCards.toArray(new Card[genCardsSize]));
    }

    @Test
    public void singleCardVsSingleCardTest() {

        List<Card> uniqueCard1 = getUniqueCards(3),
                uniqueCard2 = getUniqueCards(3);
        int dealValue1 = uniqueCard1.stream().map(Card::getNumberValue).max(Integer::compareTo).orElse(-1),
                dealValue2 = uniqueCard2.stream().map(Card::getNumberValue).max(Integer::compareTo).orElse(-1);
        CardDeal deal1 = new CardDeal(uniqueCard1),
                deal2 = new CardDeal(uniqueCard2);
        if (dealValue1 > dealValue2)
            assertThat(deal1.getScore()).isGreaterThan(deal2.getScore());
        else if (dealValue1 == dealValue2)
            assertThat(deal1.getScore()).isEqualTo(deal2.getScore());
        else
            assertThat(deal1.getScore()).isLessThan(deal2.getScore());
    }

    @Test
    public void DoubleCardVsDoubleCardTest() {

        List<Card> dealCards1 = getUniqueCards(2),
                dealCards2 = getUniqueCards(2);
        int duplicateVal1 = dealCards1.get(0).getFaceValue(),
                duplicateVal2 = dealCards2.get(0).getFaceValue();
        dealCards1.add(duplicateCard(dealCards1.get(0)));
        dealCards2.add(duplicateCard(dealCards2.get(0)));
        CardDeal deal1 = new CardDeal(dealCards1),
                deal2 = new CardDeal(dealCards2);
        if (duplicateVal1 > duplicateVal2)
            assertThat(deal1.getScore()).isGreaterThan(deal2.getScore());
        else if (duplicateVal1 == duplicateVal2)
            assertThat(deal1.getScore()).isEqualTo(deal2.getScore());
        else
            assertThat(deal1.getScore()).isLessThan(deal2.getScore());
    }

    @Test
    public void tripleCardVsTripleCardTest() {

        List<Card> dealCards1 = getUniqueCards(1),
                dealCards2 = getUniqueCards(1);
        int duplicateVal1 = dealCards1.get(0).getFaceValue(),
                duplicateVal2 = dealCards2.get(0).getFaceValue();
        dealCards1.add(duplicateCard(dealCards1.get(0)));
        dealCards1.add(duplicateCard(dealCards1.get(0)));
        dealCards2.add(duplicateCard(dealCards2.get(0)));
        dealCards2.add(duplicateCard(dealCards2.get(0)));
        CardDeal deal1 = new CardDeal(dealCards1),
                deal2 = new CardDeal(dealCards2);
        if (duplicateVal1 > duplicateVal2)
            assertThat(deal1.getScore()).isGreaterThan(deal2.getScore());
        else if (duplicateVal1 == duplicateVal2)
            assertThat(deal1.getScore()).isEqualTo(deal2.getScore());
        else
            assertThat(deal1.getScore()).isLessThan(deal2.getScore());
    }

    @Test
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
            assertThat(deal1.getScore()).isGreaterThan(deal2.getScore());
        else if (duplicateVal1 == duplicateVal2)
            assertThat(deal1.getScore()).isEqualTo(deal2.getScore());
        else
            assertThat(deal1.getScore()).isLessThan(deal2.getScore());
    }
}
