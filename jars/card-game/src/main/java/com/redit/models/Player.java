package com.redit.models;

import java.util.Objects;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class Player {

    private String name;
    // TODO: Implement score board for players
    public int wins;
    public int totalGames;
    public int winningStreak;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void gameWon() {
        this.gamePlayed();
        this.wins += 1;
    }

    public void gamePlayed() {
        this.totalGames += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
