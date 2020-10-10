package com.redit.core;

import com.redit.exceptions.InvalidStateException;
import com.redit.models.Game;
import com.redit.models.GameState;
import com.redit.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created By : Lalit Umbarkar
 * Created On : 08/10/2020
 */

public class Main {

    public static void main(String[] args) {

        List<Player> allPlayers = new ArrayList<>();
        boolean isCliGame = true;
        if (args != null && args.length > 0) {
            for (String name : args) {
                allPlayers.add(new Player(name));
            }
            isCliGame = false;
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("There are four players in the game. Enter each of their names");
            for (int eachPlayer = 0; eachPlayer < 4; eachPlayer++) {
                System.out.print("Player " + (eachPlayer + 1) + ": ");
                String playerName = scanner.nextLine();
                allPlayers.add(new Player(playerName));
            }
        }
        Game game = new Game(allPlayers);
        if (isCliGame)
            System.out.println("All four will be dealt 3 cards now.");
        try {
            while (game.getState() != GameState.END) {
                if (isCliGame) {
                    System.out.print("Dealing cards");
                    for (int i = 0; i < 3; i++) {
                        //noinspection BusyWait
                        Thread.sleep(500);
                        System.out.print(".");
                    }
                    System.out.println();
                }
                game.dealCards();
                if (isCliGame && game.getWinner().isPresent()) {
                    System.out.println(game.getWinner().get().getName() + " won the game");
                } else if (isCliGame) {
                    System.out.println("There was a draw in " + game.getPlayingPlayers());
                }
            }
            if (isCliGame) {
                System.out.println("The dealt cards to players were: ");
                game.showPlayerCards();
            }
        } catch (InvalidStateException e) {
            System.err.println("Some internal error occurred.");
        } catch (InterruptedException e) {
            System.err.println("Looks like game was stopped.");
        }
    }
}



