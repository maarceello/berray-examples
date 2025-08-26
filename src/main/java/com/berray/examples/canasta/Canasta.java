package com.berray.examples.canasta;


import com.berray.BerrayApplication;
import com.berray.GameObject;
import com.berray.assets.CoreAssetShortcuts;
import com.berray.assets.SpriteSheet;
import com.berray.components.CoreComponentShortcuts;
import com.berray.components.core.AnchorType;
import com.berray.event.CoreEvents;
import com.berray.event.Event;
import com.berray.event.MouseEvent;
import com.berray.event.UpdateEvent;
import com.berray.math.Color;
import com.berray.math.Vec2;

import java.util.Collections;
import java.util.List;

public class Canasta extends BerrayApplication implements CoreComponentShortcuts, CoreAssetShortcuts, CoreEvents {
    private AnimationManager animationManager;

    private Vec2 cardSize = new Vec2(56, 80);

    @Override
    public void initWindow() {
        width(2000);
        height(768);
        background(Color.GRAY);
        title("Canasta Card Game");
    }

    @Override
    public void game() {
        loadSpriteSheet("cards", "resources/kerenel_Cards.png", SpriteSheet.spriteSheet().sliceX(14).sliceY(6));

        this.animationManager = new AnimationManager();

        List<Card> deck = new CardDeck().fullDeck();
        Collections.shuffle(deck);



        game.on("update", (UpdateEvent event) -> {
            if (event.getSource() == null) {
            animationManager.animationUpdate(event);
            }
        });

        for (int x = 0; x < 14; x++) {
            Card card = deck.get(x);
            int frame = card.getCardSuit().ordinal() * 14 + card.getCardValue().ordinal() + 1;

            GameObject cardObject = add(
                    rect(cardSize).fill(false),
                    color(Color.GOLD),
                    anchor(AnchorType.TOP_LEFT),
                    property("card", card),
                    pos(x * 65+33, 80),
                    area(),
                    mouse()
            );

            GameObject cardSprite = cardObject.add(
                    "cardSprite",
                    anchor(AnchorType.CENTER),
                    pos(cardSize.scale(0.5f)),
                    sprite("cards").frame(frame),
                    scale(1.0f),
                    new FlipComponent(frame, 3*14)
            );
            cardObject.on(CoreEvents.HOVER_ENTER, this::onHoverEnter);
            cardObject.on(CoreEvents.HOVER_LEAVE, this::onHoverLeave);
            cardObject.on(CoreEvents.DRAG_START, this::onDragStart);
            cardObject.on(CoreEvents.DRAGGING, this::onDragging);
        }
    }

    private void onHoverEnter(Event event) {
        GameObject source = event.getSource();
        GameObject cardSprite = source.getChild("cardSprite");

        if (cardSprite != null) {
            AnimationData<Float> animationData = new AnimationData<>(
                    "sideVisible",
                    cardSprite.<Float>get("sideVisible"),
                    -1.0f,
                    0.2f,
                    (f, scale) -> f * scale,
                    (a, b) -> a + b,
                    (a, b) -> a - b
            );

            animationManager.addAnimation(cardSprite, animationData);
        }
    }

    private void onHoverLeave(Event event) {
        GameObject source = event.getSource();
        GameObject cardSprite = source.getChild("cardSprite");

        if (cardSprite != null) {
            AnimationData<Float> animationData = new AnimationData<>(
                    "sideVisible",
                    cardSprite.<Float>get("sideVisible"),
                    1.0f,
                    0.2f,
                    (f, scale) -> f * scale,
                    (a, b) -> a + b,
                    (a, b) -> a - b
            );

            animationManager.addAnimation(cardSprite, animationData);
        }
    }

    private void onDragStart(MouseEvent event) {
        GameObject source = event.getSource();
        source.setProperty("dragDelta", source.<Vec2>get("pos").sub(event.getWindowPos()));
    }

    private void onDragging(MouseEvent event) { {
        GameObject source = event.getSource();
        Vec2 dragDelta = source.getProperty("dragDelta");
        source.set("pos", event.getWindowPos().add(dragDelta));
    }}

    public static void main(String[] args) {
        new Canasta().runGame();
    }
}
