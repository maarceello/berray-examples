package com.berray.examples.canasta;

import java.util.ArrayList;
import java.util.List;

public class CardDeck {

    public List<Card> fullDeck() {
        List<Card> result = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank value : CardRank.values()) {
                result.add(new Card(suit, value));
            }
        }
        return result;
    }


}
