package com.redit.core;

import org.junit.BeforeClass;
import org.junit.Test;

public class MainTest {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before test start");
    }

    @Test
    public void sanityTest() {
        Main.main(null);
    }


}
