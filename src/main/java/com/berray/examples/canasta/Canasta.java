package com.berray.examples.canasta;


import com.berray.*;
import com.berray.assets.CoreAssetShortcuts;
import com.berray.assets.SpriteSheet;
import com.berray.components.CoreComponentShortcuts;
import com.berray.components.core.AnchorType;
import com.berray.event.CoreEvents;
import com.berray.event.Event;
import com.berray.event.UpdateEvent;
import com.berray.math.Color;
import com.berray.math.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.berray.examples.canasta.DragComponent.draggable;

public class Canasta extends BerrayApplication implements CoreComponentShortcuts, CoreAssetShortcuts, CoreEvents {

    public  static final Vec2 cardSize = new Vec2(56, 80);

    @Override
    public void initWindow() {
        width(1500);
        height(768);
        background(new Color(0,255.0f/100,0));
        title("Canasta Card Game");
    }

    @Override
    public void game() {
        layers(Game.DEFAULT_LAYER, "dragLayer");

        loadSpriteSheet("cards", "resources/kerenel_Cards.png", SpriteSheet.spriteSheet().sliceX(14).sliceY(6));

        List<Card> deck = fullDeck();
        Collections.shuffle(deck);
        game.on("update", (UpdateEvent event) -> {
            if (event.getSource() == null) {
                game.getAnimationManager().animationUpdate(event);
            }
        });

        GameObject handStack = add(
            pos(width() / 2, height() / 2),
            rect(600, 300).fill(false),
            anchor(AnchorType.CENTER),
            new HandStack()
        );


        CardStack stack = new CardStack("Ablagestapel");

        for (int x = 14; x < 28; x++) {
            handStack.doAction("addCard", deck.get(x), 0);
        }

        for (int x = 0; x < 14; x++) {
            Card card = deck.get(x);
            stack.addCard(card);

            int frame = card.getCardSuit().ordinal() * 14 + card.getCardValue().ordinal() + 1;

            GameObject cardObject = add(
                    "card",
                    rect(cardSize).fill(false),
                    color(Color.GOLD),
                    anchor(AnchorType.TOP_LEFT),
                    property("card", card),
                    pos(x * 65+33, 80),
                    layer(Game.DEFAULT_LAYER),
                    area(),
                    mouse(),
                    draggable()
            );

            cardObject.add(
                    "cardSprite",
                    anchor(AnchorType.CENTER),
                    pos(cardSize.scale(0.5f)),
                    sprite("cards").frame(frame),
                    scale(1.0f),
                    new FlipComponent(frame, 3*14)
            );

//            cardObject.on(CoreEvents.MOUSE_CLICK, (event) -> {
//                GameObject source = event.getSource();
//                Card localCard = source.getProperty("card");
//                handStack.doAction("addCard", localCard, 0);
//            });
        }

//        add(
//            pos(530, 240),
//            cardStack(stack),
//            area(),
//            mouse(),
//            draggable(),
//            dropTarget("card")
//        );

        // Draw the arc of cards at the bottom of the screen
        int screenCenter = width() / 2;
        int arcRadius = 300;
        float arcAngle = 60; // degrees

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
                    EasingFunctions.EASE_OUT_ELASTIC,
                    (f, scale) -> f * scale,
                    (a, b) -> a + b,
                    (a, b) -> a - b
            );

            game.getAnimationManager().addAnimation(cardSprite, animationData);
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
                    EasingFunctions.EASE_OUT_ELASTIC,
                    (f, scale) -> f * scale,
                    (a, b) -> a + b,
                    (a, b) -> a - b
            );

            game.getAnimationManager().addAnimation(cardSprite, animationData);
        }
    }

    public List<Card> fullDeck() {
        List<Card> result = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank value : CardRank.values()) {
                result.add(new Card(suit, value));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        new Canasta().runGame();
    }
}
