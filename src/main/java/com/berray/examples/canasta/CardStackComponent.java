package com.berray.examples.canasta;

import com.berray.GameObject;
import com.berray.assets.Asset;
import com.berray.assets.SpriteSheet;
import com.berray.components.core.Component;
import com.berray.math.Color;
import com.berray.math.Rect;
import com.berray.math.Vec2;

import static com.raylib.Raylib.DrawTextureRec;

public class CardStackComponent extends Component {

  private CardStack cards;

  public CardStackComponent(CardStack cards) {
    super("CardStack");
    this.cards = cards;
  }

  @Override
  public void add(GameObject gameObject) {
    super.add(gameObject);
    registerBoundProperty("cards", this::getCards, this::setCards);
    registerGetter("size", this::getSize);
    registerGetter("render" , () -> true);
  }

  @Override
  public void draw() {
    Asset asset = getAssetManager().getAsset("cards");
    SpriteSheet spriteSheet = asset.getAsset();

    gameObject.get("isDropActive");

    int y = 0;
    for (Card card : cards.getCards()) {
      int frame = card.getCardSuit().ordinal() * 14 + card.getCardValue().ordinal() + 1;
      Rect frameRect = spriteSheet.getFrame(frame);
      DrawTextureRec(spriteSheet.getTexture(), frameRect.toRectangle(), new Vec2(0, y).toVector2(), Color.WHITE.toRaylibColor());
      y += 20;
    }
  }

  public Vec2 getSize() {
    if (cards.getCards().isEmpty()) {
      return new Vec2(1,1);
    }
    return new Vec2(65, 80 + (cards.getCards().size() - 1) * 20);
  }

  public CardStack getCards() {
    return cards;
  }

  public void setCards(CardStack cards) {
    this.cards = cards;
  }

  public static CardStackComponent cardStack(CardStack cards) {
    return new CardStackComponent(cards);
  }
}
