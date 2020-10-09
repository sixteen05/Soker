package com.redit.models;

import com.redit.util.CoreUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class GameTests {

    @Test
    public void testInitialCardDeck() {

        List<Player> players = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            players.add(CoreUtils.generatePlayer());
        }
        Game game = new Game(players);
        List<Card> completeDeck = game.getCurrentDeck();

        assertThat(completeDeck.size()).isEqualTo(52);
        assertThat(completeDeck).asList().doesNotHaveDuplicates();
        assertThat(completeDeck).asList().doesNotHaveDuplicates();
    }

}
