package com.redit.models;

import com.redit.exceptions.InvalidInitializationException;
import com.redit.exceptions.InvalidStateException;

import java.util.*;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class Game {

    private List<Player> players = new ArrayList<>();
    private List<Player> playingPlayers = new ArrayList<>();
    private Map<Player, List<CardDeal>> playerDeals = new HashMap<>();
    private int dealSize;
    private int numberOfDeals;
    private Optional<Player> winner = Optional.empty();
    private GameState state;
    private List<Card> currentDeck = new ArrayList<>();

    public Game(List<Player> players) {
        if (players.size() <= 1)
            throw new InvalidInitializationException("Player size cannot be less than 2");
        this.players.addAll(players);
        this.playingPlayers.addAll(players);
        for (Player eachPlayer : players)
            playerDeals.put(eachPlayer, new ArrayList<>());
        this.dealSize = 3;
        this.numberOfDeals = 0;
        this.state = GameState.CREATED;
        createDeck();
    }

    public Game(List<Player> players, int dealSize) {
        this(players);
        if (dealSize <= 1)
            throw new InvalidInitializationException("Deal size cannot be less than 2");
        this.dealSize = dealSize;
    }

    public GameState getState() {
        return state;
    }

    public List<Card> getCurrentDeck() {
        return new ArrayList<>(currentDeck);
    }

    public void dealCards() throws InvalidStateException {
        if (state == GameState.END)
            throw new InvalidStateException("Game has ended. Cannot deal more cards");
        this.numberOfDeals += 1;
        // Used to deal cards to all players
        for (Player eachPlayer : playingPlayers)
            playerDeals.get(eachPlayer).add(CardDeal.createDeal(this.dealSize, this.currentDeck));
        checkForWinner();
    }

    public void showPlayerCards() {
        for (Player eachPlayer : this.players) {
            System.out.println(eachPlayer.getName() + " had:");
            System.out.println(playerDeals.get(eachPlayer));
        }
    }

    public Optional<Player> getWinner() {
        if (state != GameState.END)
            return Optional.empty();
        return winner;
    }

    public List<Player> getPlayingPlayers() {
        return new ArrayList<>(playingPlayers);
    }

    private void checkForWinner() {
        int maxScore = -1;
        List<Player> winners = new ArrayList<>();
        for (Player eachPlayer : playingPlayers) {
            List<CardDeal> allDeals = playerDeals.get(eachPlayer);
            // If player has been dealt the last deal only then check
            int playerScore = numberOfDeals > 1 ?
                    allDeals.get(numberOfDeals - 1).getDrawScore() :
                    allDeals.get(numberOfDeals - 1).getScore();
            if (playerScore > maxScore) {
                maxScore = playerScore;
                winners.clear();
                winners.add(eachPlayer);
            } else if (playerScore == maxScore) {
                winners.add(eachPlayer);
            }
        }
        playingPlayers.clear();
        playingPlayers.addAll(winners);
        if (winners.size() == 1) {
            this.winner = Optional.of(winners.get(0));
            this.state = GameState.END;
            for (Player eachPlayer : players) {
                if (eachPlayer.equals(this.winner.get()))
                    eachPlayer.gameWon();
                else
                    eachPlayer.gamePlayed();
            }
        } else {
            this.winner = Optional.empty();
            this.state = GameState.DRAW;
        }
    }

    private void createDeck() {
        currentDeck = new ArrayList<>();
        for (CardSuite eachSuite : CardSuite.getAllSuites()) {
            for (CardType eachType : CardType.getAllTypes()) {
                currentDeck.add(new Card(eachSuite, eachType));
            }
        }
    }

}
