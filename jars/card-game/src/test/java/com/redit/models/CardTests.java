package com.redit.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created By : Lalit Umbarkar
 * Created On : 09/10/20
 * Organisation: CustomerXPs Software Private Ltd.
 */

public class CardTests {

    @Test
    public void aceTests() {
        for (CardSuite suite : CardSuite.getAllSuites()) {
            assertThat(new Card(suite, CardType.ACE).getFaceValue()).isEqualTo(14);
            assertThat(new Card(suite, CardType.ACE).getNumberValue()).isEqualTo(1);
        }
    }


}
