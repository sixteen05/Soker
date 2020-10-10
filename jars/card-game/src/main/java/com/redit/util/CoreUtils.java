package com.redit.util;

import com.redit.models.Card;
import com.redit.models.Player;

import java.util.List;
import java.util.Random;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 */

public class CoreUtils {

    private static final Random random = new Random();

    public static Player generatePlayer() {
        // Generate A-Z
        int[] randomInt = random.ints(3, 65, 91).toArray();
        String name = String.valueOf((char) randomInt[0]) + (char) randomInt[1] + (char) randomInt[2];
        return new Player(name);
    }

    public static Card randomChoose(List<Card> cardDeck) {
        int cardIndex = random.nextInt(cardDeck.size());
        return cardDeck.remove(cardIndex);
    }

}
