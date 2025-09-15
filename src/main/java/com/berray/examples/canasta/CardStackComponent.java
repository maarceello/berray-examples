package com.berray.examples.canasta;

import com.berray.EasingFunctions;
import com.berray.GameObject;
import com.berray.assets.Asset;
import com.berray.assets.SpriteSheet;
import com.berray.components.core.Component;
import com.berray.event.AnimationEvent;
import com.berray.event.CoreEvents;
import com.berray.examples.canasta.events.DragEvent;
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

    on(DragEvent.EVENT_DRAG_DROP, this::onDrop);
  }

  private void onDrop(DragEvent event) {
    GameObject source = event.getSource();
    if (source.is("card")) {
      Card card = source.getProperty("card");
      source.setZ(1);

      // move card to end of stack
      Vec2 endOfStack = gameObject.get("pos", Vec2.origin()).add(new Vec2(0, 20).scale(cards.getCards().size()));

      // move the card to the end of the stack
      source.animate("pos", endOfStack, 0.5f, EasingFunctions.EASE_IN_OUT_EXPONENTIAL);

      // when the animation is done, remove the card from the table and add it to the stack
      source.on(CoreEvents.ANIMATION_END, (AnimationEvent e) -> {
        cards.addCard(card);
        gameObject.getGame().destroy(source);
        gameObject.setTransformDirty();
      });
    }
  }

  @Override
  public void draw() {
    Asset asset = getAssetManager().getAsset("cards");
    SpriteSheet spriteSheet = asset.getAsset();

    boolean dropHovered = gameObject.get("dropHovered", Boolean.FALSE);

    Color color = dropHovered ? Color.GRAY : Color.WHITE;

    int y = 0;
    for (Card card : cards.getCards()) {
      int frame = card.getCardSuit().ordinal() * 14 + card.getCardValue().ordinal() + 1;
      Rect frameRect = spriteSheet.getFrame(frame);
      DrawTextureRec(spriteSheet.getTexture(), frameRect.toRectangle(), new Vec2(0, y).toVector2(), color.toRaylibColor());
      y += 20;
    }
  }

  public Vec2 getSize() {
    if (cards.getCards().isEmpty()) {
      return new Vec2(1,1);
    }
    return new Vec2(56, 80 + (cards.getCards().size() - 1) * 20);
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
