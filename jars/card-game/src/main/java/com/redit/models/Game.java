package com.redit.models;

import java.util.*;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class Game {

    private List<Player> players = new ArrayList<>();
    private Map<Player, List<CardDeal>> playerDeals = new HashMap<>();
    private int dealSize;
    private Optional<Player> winner = Optional.empty();
    private GameState state;
    private List<Card> currentDeck = new ArrayList<>();

    public Game(List<Player> players) {
        this.players.addAll(players);
        this.dealSize = 3;
        this.state = GameState.CREATED;
        createDeck();
    }

    public Game(List<Player> players, int dealSize) {
        this(players);
        this.dealSize = dealSize;
    }

    public GameState getState() {
        return state;
    }

    public List<Card> getCurrentDeck() {
        return new ArrayList<>(currentDeck);
    }

    public void dealCards() {
        // Used to deal cards to all players
    }

    public Optional<Player> getWinner() {
        if (state != GameState.END)
            return Optional.empty();
        return winner;
    }

    private void setWinner(Player winner) {
        this.winner = Optional.of(winner);
        this.state = GameState.END;
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
