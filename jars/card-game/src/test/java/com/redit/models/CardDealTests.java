package com.redit.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class CardDealTests {

    public static Card generateRandomCard() {
        Random random = new Random();
        CardSuite suite = CardSuite.getAllSuites()[random.nextInt(CardSuite.getAllSuites().length)];
        CardType type = CardType.getAllTypes()[random.nextInt(CardType.getAllTypes().length)];
        return new Card(suite, type);
    }

    @Test
    public void createDealTest() {

        List<Card> existingDeck = new ArrayList<>();
        Card randomCard = generateRandomCard();
        existingDeck.add(randomCard);
        CardDeal deal = CardDeal.createDeal(1, existingDeck);
        assertThat(existingDeck).isEmpty();
        assertThat(deal.getDrawScore()).isEqualTo(randomCard.getNumberValue());
    }

}
