package com.redit.util;

import com.redit.models.Player;

import java.util.Random;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class CoreUtils {

    private static final Random random = new Random();

    public static Player generatePlayer() {
        // Generate A-Z
        StringBuilder name = new StringBuilder();
        name.append(random.ints(65, 91));
        name.append(random.ints(65, 91));
        name.append(random.ints(65, 91));
        return new Player(name.toString());
    }

}
