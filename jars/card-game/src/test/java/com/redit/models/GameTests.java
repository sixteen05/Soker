package com.redit.models;

import com.redit.exceptions.InvalidInitializationException;
import com.redit.exceptions.InvalidStateException;
import com.redit.util.CoreUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 */

public class GameTests {

    private static Random random;

    @BeforeAll
    public static void setupRandom() {
        random = new Random(System.currentTimeMillis());
    }

    @Test
    public void testInitialCardDeck() {

        int numPlayers = random.nextInt(4) + 2;
        List<Player> players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++)
            players.add(CoreUtils.generatePlayer());
        Game game = new Game(players);
        List<Card> completeDeck = game.getCurrentDeck();

        assertThat(completeDeck.size()).isEqualTo(52);
        assertThat(completeDeck).asList().doesNotHaveDuplicates();
        assertThat(completeDeck).asList().doesNotHaveDuplicates();
    }

    @Test
    public void constructorTests() {
        // Negative to positive values
        int[] numPlayerPool = {random.nextInt(10) * -1, 0, 1, 2, random.nextInt(4) + 2},
                dealSizePool = {random.nextInt(10) * -1, 0, 1, 2, random.nextInt(4) + 2};
        for (int numPlayers : numPlayerPool) {
            for (int dealSize : dealSizePool) {
                List<Player> players = new ArrayList<>();
                for (int i = 0; i < numPlayers; i++)
                    players.add(CoreUtils.generatePlayer());
                if (numPlayers < 2) {
                    try {
                        new Game(players);
                    } catch (InvalidInitializationException iie) {
                        assertThat(iie)
                                .as("Throw exception when players are less than 2 with single arg constructor")
                                .hasMessage("Player size cannot be less than 2");
                    }
                    try {
                        new Game(players, dealSize);
                    } catch (InvalidInitializationException iie) {
                        assertThat(iie)
                                .as("Throw exception when players are less than 2 with two arg constructor")
                                .hasMessage("Player size cannot be less than 2");
                    }
                } else if (dealSize < 2) {
                    try {
                        new Game(players, dealSize);
                    } catch (InvalidInitializationException iie) {
                        assertThat(iie)
                                .as("Throw exception when dealSize is less than 2")
                                .hasMessage("Deal size cannot be less than 2");
                    }
                } else {
                    assertThat(new Game(players)).isNotNull();
                    assertThat(new Game(players, dealSize)).isNotNull();
                }
            }
        }
    }

    @RepeatedTest(100)
    public void basicGameStateTests() {

        // Get random players between 2 to 5
        int numPlayers = random.nextInt(4) + 2;
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++)
            players.add(CoreUtils.generatePlayer());
        Game game = new Game(players);

        /// CREATED state conditioning
        assertThat(game)
                .as("Game must satisfy above conditions just after creating it" +
                        "\nGame %s", game)
                .satisfies(g -> assertThat(g.getState() == GameState.CREATED))
                .satisfies(g -> assertThat(g.getCurrentDeck().size() == 52))
                .satisfies(g -> assertThat(g.getPlayingPlayers().size() == numPlayers))
                .satisfies(g -> assertThat(!g.getWinner().isPresent()));
        try {
            game.dealCards();
        } catch (InvalidStateException e) {
            assertThat(e).doesNotThrowAnyException();
        }
        assertThat(game.getState())
                .isIn(GameState.DRAW, GameState.END);
        int remainingDeckSize = 52 - (numPlayers * 3);

        /// DRAW state conditioning
        while (game.getState() == GameState.DRAW) {
            int finalRemainingDeckSize = remainingDeckSize;
            assertThat(game)
                    .as("Game must satisfy above conditions when in DRAW state" +
                            "\nGame %s", game)
                    .satisfies(g ->
                            assertThat(g.getCurrentDeck().size() == finalRemainingDeckSize))
                    .satisfies(g ->
                            assertThat(g.getPlayingPlayers().size())
                                    .isGreaterThan(1)
                                    .isLessThanOrEqualTo(numPlayers))
                    .satisfies(g ->
                            assertThat(!g.getWinner().isPresent()));
            try {
                game.dealCards();
            } catch (InvalidStateException e) {
                assertThat(e).doesNotThrowAnyException();
            }
            remainingDeckSize -= numPlayers;
        }

        /// END state conditioning
        int finalRemainingDeckSize = remainingDeckSize;
        assertThat(game)
                .as("Game must satisfy above conditions when in END state" +
                        "\nGame %s", game)
                .satisfies(g -> assertThat(g.getState() == GameState.END))
                .satisfies(g -> assertThat(g.getCurrentDeck().size() == finalRemainingDeckSize))
                .satisfies(g -> assertThat(g.getPlayingPlayers().size()).isEqualTo(1))
                .satisfies(g -> assertThat(g.getWinner().isPresent()))
                .satisfies(g -> assertThat(g.getPlayingPlayers().get(0)).isEqualTo(g.getWinner().orElse(null)));

        try {
            game.dealCards();
        } catch (InvalidStateException e) {
            assertThat(e).hasMessage("Game has ended. Cannot deal more cards");
        }
    }

    @Test
    public void testToStringForCompleteCoverage() {
        Player one = new Player("ONE");
        Player two = new Player("TWO");
        List<Player> players = new ArrayList<>();
        players.add(one);
        players.add(two);
        assertThat(new Game(players).toString())
                .startsWith("Game{players=[Player{name='ONE'}, Player{name='TWO'}], playerDeals={" +
                        "Player{name='ONE'}=[], Player{name='TWO'}=[]}, dealSize=3, " +
                        "winner=Optional.empty, state=CREATED");
    }

}
