package com.berray.examples.canasta;

import java.util.ArrayList;
import java.util.List;

public class CardStack {

  private String name;
  private List<Card> cards = new ArrayList<>();

  public CardStack(String name) {
    this.name = name;
  }

  public void addCard(Card card) {
    cards.add(card);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> cards) {
    this.cards = cards;
  }
}
