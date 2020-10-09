package com.redit.models;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class Player {

    // TODO: Implement score board for players
    public int wins;
    public int totalGames;
    public int winningStreak;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
