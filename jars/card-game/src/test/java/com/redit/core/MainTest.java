package com.redit.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MainTest {

    @BeforeAll
    public static void beforeClass() {
        System.out.println("Before test start");
    }

    @Test
    public void sanityTest() {
        Main.main(new String[]{"Player1", "Player2", "Player3", "Player4"});
    }


}
